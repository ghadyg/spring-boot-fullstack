import { Component } from '@angular/core';
import { AuthenticationRequest } from '../../models/authentication-request';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { Message } from 'primeng/api';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
SignUp() {
    this.router.navigate(["signup"]);
}
message: Message[] = [];
  constructor(private authenticationService: AuthenticationService,
    private router:Router
  ){}

  errorMsg:string = "";

  login() {
    this.message =[];
  this.authenticationService.login(this.authentication)
  .subscribe({
    next: (authenticationResponse)=>{
      localStorage.setItem('token',JSON.stringify(authenticationResponse));
      this.router.navigate(["customer"]);   
    },
    error: (err)=>{
      if(err.error.statusCode===401)
        {
          
          this.message = [{
            detail:'Username or password are incorrect',
            severity:"error"
          }]
        }
    }
  }
)
  ;
  }

  authentication: AuthenticationRequest = {};

}
