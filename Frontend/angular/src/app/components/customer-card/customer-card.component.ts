import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CustomerDTO } from '../../models/customerDTO';

@Component({
  selector: 'app-customer-card',
  templateUrl: './customer-card.component.html',
  styleUrl: './customer-card.component.scss'
})
export class CustomerCardComponent {
onUpdate() {
  this.Update.emit(this.customer);
}
onDelete() {
    this.Delete.emit(this.customer);
}

  

  @Input()
  customer: CustomerDTO = {};

  @Input()
  customerid = 0;

  @Output()
  Delete: EventEmitter<CustomerDTO> = new EventEmitter<CustomerDTO>();

  @Output()
  Update: EventEmitter<CustomerDTO> = new EventEmitter<CustomerDTO>();

}
