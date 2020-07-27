import { Component, OnInit } from '@angular/core';

import { first } from 'rxjs/operators';
import {AuthenticationService} from '../../services/authentication.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenService} from '../../services/token.service';
import {log} from 'util';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  form = {
    username: null,
    password: null
  };

  public errors = null;
  private returnUrl: string;
  loading = false;

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private token: TokenService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  submit() {
    console.log(this.form);
    this.loading = true;
    this.authService.login(this.form)
      .then(response => {
        console.log('reponse login', response);
        if (response.status === 'rejected') {
          this.handleError(response.error);
        } else {
          this.handleResponse(response.results);
          this.router.navigate(['/']);
        }

      })
      .catch(error => console.log(error));
   }

  handleError(err) {
    this.errors = err.message;
  }

  private handleResponse(data) {
    this.token.handle(data, this.form.username);
  }
}
