import { Component, OnInit } from '@angular/core';
import { CustomerDTO } from '../../models/customerDTO';
import { CustomerServiceService } from '../../services/customers/customer-service.service';
import { CustomerRegistrationRequest } from '../../models/Customer-registration-request';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.scss'
})
export class CustomerComponent implements OnInit {
cancel() {
    this.display = false;
    this.customer = {};
    this.operation='create';
}
updateCustomer(c: CustomerDTO) {
  this.customer = c; 
  this.operation='update';   
  this.display=true;
      
}

  visible: boolean = false;
  display: boolean = false;
  customers: CustomerDTO[] = [];
  customer:CustomerRegistrationRequest = {};
  operation: 'update' | 'create' ='create';
  
deleteCustomer(customer: CustomerDTO) {
  this.confirmationService.confirm({
    header: 'Delete customer',
    message: 'are you sure you want to delete the customer?',
    accept: ()=>{
      this.customerService.deleteCustomer(customer.id)
      .subscribe(
        {
          next:()=>{
            this.findAllCustomers();
            this.messageService.add({ severity: 'success', summary: 'Customer Deleted', detail: 'Customer was successfully Deleted!' });
          }
        }
      )
    }
  })
}



  constructor(
    private customerService: CustomerServiceService,
    private messageService : MessageService,
    private confirmationService:ConfirmationService
  ){}

  ngOnInit(): void {
    this.findAllCustomers();
  }

  private findAllCustomers(){
    this.customerService.findAll()
      .subscribe({
        next: (data)=>{
          this.customers = data
          
        }
      })
  }

  changeDisplay() {
    this.operation='create';
    this.customer={};  
    this.display = true;
  }
  save(customerReg: CustomerRegistrationRequest) {
    if(customerReg){
      if(this.operation=='create')
        {
      this.customerService.registerCustomer(customerReg)
      .subscribe({
        next: ()=>{
          this.findAllCustomers();
          this.display= false;
          this.customer = {};
          this.messageService.add({ severity: 'success', summary: 'Customer Saved', detail: 'Customer was successfully added!' });
        }
      });
    }
    else{
      this.customerService.updateCustomer(customerReg.id,this.customer)
        .subscribe({
          next: ()=>{
            this.findAllCustomers();
          this.display= false;
          this.customer = {};
          this.messageService.add({ severity: 'success', summary: 'Customer updated', detail: 'Customer was successfully Updated!' });
          }
        })
    }
      
    }
}
 
}
