import {Component, OnChanges, OnInit} from '@angular/core';
import {AuthenticationService} from '../../services/authentication.service';
import {User} from '../../structures/user';
import {TokenService} from '../../services/token.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit, OnChanges {
  /* Utiloisateur courrant */
  private currentUser = null;
  private login = false;

  constructor(
    private router: Router,
    private token: TokenService,
    private auth: AuthenticationService
  ) {
  }

  ngOnInit() {
    this.token.loggedIn()
      .then(
        (respones) => {
          if (respones.status === 'accepted') {
            console.log('user ', respones);
            this.currentUser = respones.result;
            this.login = true;
          }
        }
      )
      .catch( error => {
        console.log(error);
      })
    ;
  }

  logout() {
    this.auth.logout(this.token.getSessionKey(), this.token.getUsername());
    this.token.remove();
    this.login = false;
    this.router.navigate(['/']);
  }

  ngOnChanges() {
    console.log('change');
  }
}
