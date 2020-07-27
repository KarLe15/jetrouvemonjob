import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ApiCallService} from './api-call.service';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  baseUrl = 'http://localhost:8080/Serveur_war_exploded';

  constructor(
    private http: HttpClient,
    private apiCall: ApiCallService
  ) { }


  searchOnApi(data: any) {
    return this.apiCall.getResponseApi(`${this.baseUrl}/offres`, data);
  }

  getFilters(type: string | Array<string> ) {
    return this.apiCall.getResponseApi(`${this.baseUrl}/filters`, {datas: type});
  }

  getCommentaires(ident: string) {
    return this.apiCall.getResponseApi(`${this.baseUrl}/offres`, {id: ident});
  }

  like(id: string, user: string, sessionKey: string) {
    return this.apiCall.putResponseApi(`${this.baseUrl}/offres`, {idcomm: id, username: user, sessionkey: sessionKey});
  }

  disLike(id: string, user: string, sessionKey: string) {
    return this.apiCall.deleteResponseApi(`${this.baseUrl}/offres`, {idcomm: id, username: user, sessionkey: sessionKey});
  }

  addComment(id: string, user: string, sessionKey: string, commentText: string) {
    return this.apiCall.postResponseApi(`${this.baseUrl}/offres`,
      {idOffre: id, username: user, sessionkey: sessionKey, contentMessage: commentText});
  }
}
