import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/domain/services/authentication.service';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})

export class NavbarComponent {
    constructor(private authenticationService: AuthenticationService, private router: Router) { }

    logout() {
        this.authenticationService.logout();
    };

    redirectToMenuComponent() {
        // Use o router para navegar para o componente de menu desejado
        this.router.navigate(['/dash/cardapio']);
      }
}
