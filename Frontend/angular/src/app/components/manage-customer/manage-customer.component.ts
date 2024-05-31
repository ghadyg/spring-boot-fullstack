import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CustomerRegistrationRequest } from '../../models/Customer-registration-request';

@Component({
  selector: 'app-manage-customer',
  templateUrl: './manage-customer.component.html',
  styleUrl: './manage-customer.component.scss'
})
export class ManageCustomerComponent implements OnInit{
onCancel() {
   this.cancel.emit();
}

  ngOnInit(): void {
    this.title= this.operation ==='update'? 'Update Customer':'Create Customer' 
  }
  onSubmit() {
    this.submit.emit(this.customer);
  }
  get isCustomerValid():boolean {
      return this.isValid(this.customer.name)&&this.isValid(this.customer.email)
      && this.customer.age !== undefined && this.customer.age >0
        && (this.operation==='update' ||
          this.isValid(this.customer.password) &&this.isValid(this.customer.gender))
        
  }

  @Input()
  customer: CustomerRegistrationRequest = {};

  @Input()
  operation: 'update'| 'create'='create';

  @Output()
  submit: EventEmitter<CustomerRegistrationRequest> = new EventEmitter<CustomerRegistrationRequest>;

  @Output()
  cancel: EventEmitter<void> = new EventEmitter<void>;

  title = 'New Customer';

  private isValid( toBeValidated: string | undefined): boolean {
    return toBeValidated !== null && toBeValidated !== undefined && toBeValidated.length > 0;
  }
}
