import { Component } from '@angular/core';
import { AuthenticationService } from 'src/app/domain/services/authentication.service';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})

export class NavbarComponent {
    constructor(private authenticationService: AuthenticationService) { }

    logout() {
        this.authenticationService.logout();
    };
}
