import { Component, OnInit } from '@angular/core';
import {User} from '../../structures/user';
import {AuthenticationService} from '../../services/authentication.service';
import {error} from 'util';
import { first } from 'rxjs/operators';
import {Router} from '@angular/router';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {
  form: User = {
    nom: '',
    confirmedPassword: '',
    mail: '',
    password: '',
    prenom: '',
    username: '',
  };
  errors: any;
  private loading = false;

  constructor(
    private authService: AuthenticationService,
    private userService: UserService,
    private router: Router,
    ) {
    // if (this.authService.currentUserValue) {
    //   this.router.navigate(['/']);
    // }
  }

  ngOnInit() {
  }

  submit() {
    console.log(this.form );
    this.loading = true;
    this.userService.signup(this.form)
      .then(
        (response ) => {
          console.log(response);
          if (response.status === 'accepted') {
            console.log('Registration successful', true);
            this.router.navigate(['/login']);
          } else {
            this.loading = false;
            console.log('Registration successful', false);
        }
      })
      // tslint:disable-next-line:no-shadowed-variable
      .catch( error => {
        console.log(error);
      })
    ;
  }
}
