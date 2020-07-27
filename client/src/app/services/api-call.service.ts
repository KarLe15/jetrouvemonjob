import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ApiCallService {

  constructor(private http: HttpClient) { }

  public async getResponseApi(filepath: string, data: any): Promise<any> {
    return await this.http.get(
      filepath,
      {
        responseType: 'json',
        params: data
      }
    ).toPromise();
  }
  public async postResponseApi(filepath: string, data: any): Promise<any> {
    return await this.http.post(
      filepath, {},
      {
        responseType: 'json',
        params: data
      }
    ).toPromise();
  }
  public async putResponseApi(filepath: string, data: any): Promise<any> {
    return await this.http.put(
      filepath,{},
      {
        responseType: 'json',
        params: data
      }
    ).toPromise();
  }
  public async deleteResponseApi(filepath: string, data: any): Promise<any> {
    return await this.http.delete(
      filepath,
      {
        responseType: 'json',
        params: data
      }
    ).toPromise();
  }

  public async optionsResponseApi(filepath: string, data: any, user: string, key: string): Promise<any> {
    return await this.http.put(
      filepath, {
        data
      },
      {
        responseType: 'json',
        params: { username: user, sessionkey: key }
      }
    ).toPromise();
  }

}
