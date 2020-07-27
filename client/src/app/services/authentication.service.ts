import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ApiCallService} from './api-call.service';
import { BehaviorSubject, Observable } from 'rxjs';
import {User} from '../structures/user';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  baseUrl = 'http://localhost:8080/Serveur_war_exploded';


  constructor(private http: HttpClient,
              private apiCall: ApiCallService
  ) { }

  /* Authentification */
  login(data) {
    return this.apiCall
      .putResponseApi(`${this.baseUrl}/login`, data);
  }

  logout(key, user) {
    return this.apiCall
      .deleteResponseApi(`${this.baseUrl}/login`, {sessionkey: key, username: user});
  }


}
