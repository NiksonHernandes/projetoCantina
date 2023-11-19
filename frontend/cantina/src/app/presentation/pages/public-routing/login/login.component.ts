import { Component, ElementRef, Renderer2 } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { first } from 'rxjs';
import { AuthenticationService } from 'src/app/domain/services/authentication.service';

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

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authenticationService: AuthenticationService,
    private renderer: Renderer2, private elementRef: ElementRef
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
          console.log("ERRRO", error)

          this.loading = false;
        }
      });
  };



}
