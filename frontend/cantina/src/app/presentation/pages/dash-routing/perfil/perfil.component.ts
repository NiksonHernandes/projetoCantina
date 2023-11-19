import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CarrinhoService } from 'src/app/domain/services/carrinho.service';
import { ToastService } from 'src/app/domain/services/toast.service';

@Component({
    selector: 'app-perfil',
    templateUrl: './perfil.component.html',
    styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent {

    userForm!: FormGroup;
    submitted = false;

    currentUser?: any;

    constructor(
        private carrinhoService: CarrinhoService, private modalService: NgbModal, private formBuilder: FormBuilder, private toastService: ToastService,
    ) { }

    async ngOnInit(): Promise<void> {
        await this.getCurrentUser();

        this.userForm = this.formBuilder.group({
            nomeCompleto: [this.currentUser?.nomeCompleto || '', Validators.required],
            cpf: [this.currentUser?.cpf || '', Validators.required],
            curso: [this.currentUser?.curso || '', Validators.required],
            rua: [this.currentUser?.rua || '', Validators.required],
            celular: [this.currentUser?.celular || '', Validators.required],
        });

    }

    get formField() { return this.userForm.controls; }

    getCurrentUser() {
        this.carrinhoService.getCurrentUser().subscribe({
            next: (data) => {
                this.currentUser = data;
                console.log("sucesso! - getCurrentUser()", data);
            },
            error: (error) => {
                alert(error);
                console.log("Erro -getCurrentUser()", error);
            }
        });

        return new Promise<void>((resolve, reject) => {
            

            this.carrinhoService.getCurrentUser().subscribe({
                next: (data) => {
                    this.currentUser = data;
                    console.log("sucesso! - getCurrentUser()", data);
                    resolve();
                },
                error: (error) => {
                    alert(error);
                    console.log("Erro -getCurrentUser()", error);
                    reject(error); 
                }
            });
        });
    }

    openScrollableContent(longContent: any) {
        this.modalService.open(longContent, { scrollable: true });
    }

    atualizarUser() {
        this.submitted = true;

        if (this.userForm.invalid) {
            return;
        }

        let userObj = {
            id: this.currentUser.id,
            nomeCompleto: this.formField['nomeCompleto'].value,
            email: this.currentUser.email,
            username: this.currentUser.username,
            cpf: this.formField['cpf'].value,
            sexo: this.currentUser.sexo,
            semestreAtual: this.currentUser.semestreAtual,
            curso: this.formField['curso'].value,
            rua: this.formField['rua'].value,
            bairro: this.currentUser.bairro,
            telefone: this.currentUser.telefone,
            celular: this.formField['celular'].value
        };

        this.carrinhoService.updateUser(userObj).subscribe({
            next: (data) => {
                this.toastMessage("Dados atualizados com sucesso", 1);
                console.log("sucesso! - updateUser", data);
                this.getCurrentUser();
                this.modalService.dismissAll();
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - updateUser", error);
            }
        });

        console.log("asdas", userObj)
    }

    toastMessage(message: string, type: number) {
        const successToast = 1;
        const dangerToast = 2;

        if (type == dangerToast)
            this.toastService.show(message, { classname: 'bg-danger text-light', delay: 3000 });

        else if (type == successToast)
            this.toastService.show(message, { classname: 'bg-success text-light', delay: 2000 });
    }


}
