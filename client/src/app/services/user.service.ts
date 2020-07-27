import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ApiCallService} from './api-call.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  baseUrl = 'http://localhost:8080/Serveur_war_exploded';

  constructor(
    private http: HttpClient,
    private apiCall: ApiCallService
  ) { }

  /* */
  signup(data) {
    return this.apiCall.putResponseApi(`${this.baseUrl}/user`, data);
  }

  updateCV(data, username, sessionkey) {
    return this.apiCall.optionsResponseApi(`${this.baseUrl}/user`, data, username, sessionkey);
  }

  /* */
  delete(user) {
    return this.http.delete(`${this.baseUrl}/login`, user);
  }
}
