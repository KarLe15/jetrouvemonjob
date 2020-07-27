import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthenticationService} from './authentication.service';
import {TokenService} from './token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private token: TokenService,
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    this.token.loggedIn()
      .then(
        (respones) => {
          if (respones.status === 'accepted') {
            this.router.navigate(['/']);
            return false;
          } else {
            console.log('Profile probleme ', respones.error);
          }
        }
      )
      .catch( err => {
        return true;
      });
    return  true;
  }

}
