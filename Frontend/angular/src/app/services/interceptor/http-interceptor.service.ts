import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthenticationResponse } from '../../models/authentication-response';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorService implements HttpInterceptor {

  constructor() { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    if(token)
      {
        const authResp : AuthenticationResponse = JSON.parse(token);
       const authReq = req.clone(
        {
          
          headers: new HttpHeaders({
            Authorization:"Bearer "+authResp.token
          })
        }
       )
        return next.handle(authReq)
      }
    return next.handle(req);
  }  
}
