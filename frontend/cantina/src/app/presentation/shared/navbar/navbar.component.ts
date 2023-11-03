import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/domain/services/authentication.service';
import { CarrinhoService } from 'src/app/domain/services/carrinho.service';
import { ToastService } from 'src/app/domain/services/toast.service';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})

export class NavbarComponent {

    isAdmin?: boolean;

    constructor(private authenticationService: AuthenticationService, private router: Router, private carrinhoService: CarrinhoService, private toastService: ToastService) { }

    ngOnInit(): void {
        this.isAdminVerificar();
    }

    logout() {
        this.authenticationService.logout();
    };

    verificaIsCarrinhoExiste() {
        this.carrinhoService.verificaIsCarrinhoExiste().subscribe({
            next: (data) => {
                console.log(" carrinho existe", data);
                this.router.navigate([`/dash/carrinho-open/${data.carrinhoId}`]);
            },
            error: (error) => {
                //alert(error);
                this.toastMessage(error, 2);
                console.log("Carrinho não existe = erro", error);
            }
        });

    }

    isAdminVerificar() {
        this.isAdmin = this.authenticationService.verificaPermissaoAdmin();
    }

    toastMessage(message: string, type: number) {
        const successToast = 1;
        const dangerToast = 2;

        if (type == dangerToast)
            this.toastService.show(message, { classname: 'bg-danger text-light', delay: 3000 });

        else if (type == successToast)
            this.toastService.show(message, { classname: 'bg-primary text-light', delay: 2000 });
    }
}
