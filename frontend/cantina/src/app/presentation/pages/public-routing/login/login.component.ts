import { Component, ElementRef, Renderer2 } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { first } from 'rxjs';
import { AuthenticationService } from 'src/app/domain/services/authentication.service';
import { CarrinhoService } from 'src/app/domain/services/carrinho.service';
import { ToastService } from 'src/app/domain/services/toast.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent {

    loginForm!: FormGroup;
    loading = false;
    submitted = false;
    error = '';

    cadastroForm!: FormGroup;
    submitted2 = false;
    error2 = '';
    loading2 = false;

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private authenticationService: AuthenticationService,
        private renderer: Renderer2, private elementRef: ElementRef,
        private carrinhoService: CarrinhoService,
        private toastService: ToastService
    ) {
        if (this.authenticationService.currentUser) {
            this.router.navigate(['/dash']);
        }
    }

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            username: ['', Validators.required],
            senha: ['', Validators.required]
        });

        this.cadastroForm = this.formBuilder.group({
            nomeCompleto: ['', Validators.required],
            username: ['', Validators.required],
            senha: ['', Validators.required],
            senhaConfirmacao: ['', Validators.required]
        });

        const loginText = this.elementRef.nativeElement.querySelector(".title-text .login");
        const loginForm = this.elementRef.nativeElement.querySelector("form.login");
        const loginBtn = this.elementRef.nativeElement.querySelector("label.login");
        const signupBtn = this.elementRef.nativeElement.querySelector("label.signup");
        const signupLink = this.elementRef.nativeElement.querySelector("form .signup-link a");

        signupBtn.addEventListener('click', () => {
            this.renderer.setStyle(loginForm, 'marginLeft', '-50%');
            this.renderer.setStyle(loginText, 'marginLeft', '-50%');
        });

        loginBtn.addEventListener('click', () => {
            this.renderer.setStyle(loginForm, 'marginLeft', '0%');
            this.renderer.setStyle(loginText, 'marginLeft', '0%');
        });

        signupLink.addEventListener('click', (event: Event) => {
            signupBtn.click();
            event.preventDefault();
        });
    }

    get formField() { return this.loginForm.controls; }
    get formFieldCadastro() { return this.cadastroForm.controls; }

    onSubmit() {
        this.submitted = true;

        if (this.loginForm.invalid) {
            return;
        }

        this.error = '';
        this.loading = true;

        this.authenticationService.login(this.formField['username'].value, this.formField['senha'].value)
            .pipe(first())
            .subscribe({
                next: () => {
                    console.log("Entrou na navegacao")
                    this.router.navigate(["/dash"]);
                },
                error: error => {
                    console.log("ERRRO", error.error)
                    this.toastMessage(error.error, 2);
                    this.loading = false;
                }
            });
    };

    cadastrar() {
        this.submitted2 = true;

        if (this.cadastroForm.invalid) {
            return;
        }

        this.error2 = '';
        this.loading2 = true;

        console.log(this.formFieldCadastro['username'].value)

        let cardapio = {
            nomeCompleto: this.formFieldCadastro['nomeCompleto'].value,
            username: this.formFieldCadastro['username'].value,
            senha: this.formFieldCadastro['senha'].value,
            senhaConfirmacao: this.formFieldCadastro['senhaConfirmacao'].value,
        }

        this.carrinhoService.signUp(cardapio).subscribe({
            next: (data) => {
                this.toastMessage("Cadastro efetuado com sucesso! Agora faça o login.", 1);
                this.toastMessage("REDIRECIONANDO...", 1);
                console.log("signUp - ok! ", data);

                setTimeout(() => {
                    window.location.reload();
                }, 1500);
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - signUp ", error);
            }
        })
    }

    toastMessage(message: string, type: number) {
        const successToast = 1;
        const dangerToast = 2;

        if (type == dangerToast)
            this.toastService.show(message, { classname: 'bg-danger text-light', delay: 3000 });

        else if (type == successToast)
            this.toastService.show(message, { classname: 'bg-success text-light', delay: 2000 });
    };

}
