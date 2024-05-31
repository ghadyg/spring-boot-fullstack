import { Component } from '@angular/core';
import { AuthenticationRequest } from '../../models/authentication-request';
import { Message } from 'primeng/api';
import { CustomerRegistrationRequest } from '../../models/Customer-registration-request';
import { Router } from '@angular/router';
import { CustomerServiceService } from '../../services/customers/customer-service.service';
import { AuthenticationService } from '../../services/authentication/authentication.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.scss'
})
export class RegistrationComponent {

  constructor(
    private router:Router,
    private customerService: CustomerServiceService,
    private authentication : AuthenticationService
  ){}

goLogin() {
    this.router.navigate(["login"]);
}
Signup() {
    this.customerService.registerCustomer(this.customer)
      .subscribe({
        next:()=>{
          this.authentication.login({
            username: this.customer.email,
            password: this.customer.password
          })
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
          })
        }
      })
}
  message: Message[] = [];
  customer: CustomerRegistrationRequest = {};

}
