import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { CustomerDTO } from '../../models/customerDTO';
import { AuthenticationResponse } from '../../models/authentication-response';
import { environment } from '../../../environments/environment.development';
import { CustomerRegistrationRequest } from '../../models/Customer-registration-request';
import { CustomerUpdateRequest } from '../../models/customer-update-request';
@Injectable({
  providedIn: 'root'
})
export class CustomerServiceService  {

  constructor(
    private http: HttpClient
  ) { }

  private readonly customerUrl = `${environment.api.baseUrl}${environment.api.customerUrl}`

  findAll():Observable<CustomerDTO[]> 
  {

    return this.http.get<CustomerDTO[]>(
        this.customerUrl
    )   
      
    return new Observable();
  }

  registerCustomer(customer:CustomerRegistrationRequest): Observable<void>{

    return this.http.post<void>(this.customerUrl, customer);
  }
  
  deleteCustomer(customerId : number | undefined): Observable<void>{
    return this.http.delete<void>(`${this.customerUrl}/${customerId}`);
  }
  updateCustomer(customerId:number | undefined, customer:CustomerUpdateRequest):Observable<void>{
      return this.http.put<void>(`${this.customerUrl}/${customerId}`,customer);
  }
    

}
