import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Usuario } from 'src/app/domain/models/usuario.model';
import { AuthenticationService } from 'src/app/domain/services/authentication.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  currentUser: Usuario = new Usuario();

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) { }

  ngOnInit() {
    if (this.authenticationService.currentUser) {
      this.currentUser = this.authenticationService.currentUser;
    }
  };
    
}
