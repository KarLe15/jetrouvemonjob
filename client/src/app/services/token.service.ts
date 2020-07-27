import { Injectable } from '@angular/core';
import {ApiCallService} from './api-call.service';
import {BehaviorSubject, Observable} from 'rxjs';
import {User} from '../structures/user';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  baseUrl = 'http://localhost:8080/Serveur_war_exploded';

  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  constructor(
    private apiCall: ApiCallService
  ) { }

  handle(sessionkey, username) {
    this.set(sessionkey, username);
  }

  set(sessionkey, username) {
    localStorage.setItem('sessionkey', sessionkey);
    localStorage.setItem('username', username);
  }

  getSessionKey() {
    return localStorage.getItem('sessionkey');
  }

  getUsername() {
    return localStorage.getItem('username');
  }

  remove() {
    localStorage.removeItem('sessionkey');
    localStorage.removeItem('username');
  }

  loggedIn() {
    return this.apiCall.getResponseApi(`${this.baseUrl}/user`, {sessionkey: this.getSessionKey(), username: this.getUsername()});
  }
}
