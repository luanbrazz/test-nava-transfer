import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_URL = "http://localhost:8080/api/transfers";

@Injectable({
  providedIn: 'root',
})
export class TransferService {
  constructor(private http: HttpClient) {}

  private createAuthorizationHeader(): HttpHeaders {
    const jwtToken = localStorage.getItem('jwt');
    const headers = new HttpHeaders();
    return jwtToken ? headers.set('Authorization', 'Bearer ' + jwtToken) : headers;
  }

  createTransfer(transferRequest: any): Observable<any> {
    const headers = this.createAuthorizationHeader();
    return this.http.post(BASE_URL, transferRequest, { headers });
  }

  getAllTransfers(): Observable<any> {
    const headers = this.createAuthorizationHeader();
    return this.http.get(BASE_URL, { headers });
  }

  getTransferById(id: number): Observable<any> {
    const headers = this.createAuthorizationHeader();
    return this.http.get(`${BASE_URL}/${id}`, { headers });
  }

  cancelTransferById(id: number): Observable<any> {
    const headers = this.createAuthorizationHeader();
    return this.http.patch(`${BASE_URL}/${id}`, {}, { headers });
  }
}
