import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticationResponse } from '../../models/authentication-response';
import { JwtHelperService } from '@auth0/angular-jwt';


@Injectable({
  providedIn: 'root'
})
class AccessGuardService {

  constructor(private router: Router) {}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const storedToken = localStorage.getItem('token');
    if(storedToken)
      {
        const authResp : AuthenticationResponse = JSON.parse(storedToken);
        const token = authResp.token;
        if(token)
          {
            const jwtHelper = new JwtHelperService();
            return !jwtHelper.isTokenExpired(token);
          }
      }
      this.router.navigate([""]);
      return false;
      
}
}

export const AuthGuard: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  return inject(AccessGuardService).canActivate(next, state);
}