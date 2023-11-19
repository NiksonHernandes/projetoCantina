import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthenticationService } from 'src/app/domain/services/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) { }

  canActivate(routerState: RouterStateSnapshot) {
    if (this.authenticationService.currentUser) {
      return true;
    }

    this.router.navigate(['/login'], { queryParams: { returnUrl: routerState.url } });
    return false;
  }

}
