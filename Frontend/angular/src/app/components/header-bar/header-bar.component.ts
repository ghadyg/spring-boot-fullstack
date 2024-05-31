import { Component, Input } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { AuthenticationResponse } from '../../models/authentication-response';
import { Token } from '@angular/compiler';
import { Router } from '@angular/router';


@Component({
  selector: 'app-header-bar',
  templateUrl: './header-bar.component.html',
  styleUrl: './header-bar.component.scss'
})
export class HeaderBarComponent {
  constructor(
    private router:Router
  ){}
items: MenuItem[] = [
  {
    label:"Profile",
    icon: "pi pi-user"
  },
  {
    label:"Settings",
    icon: "pi pi-cog"
  },
  {
    separator: true
  },
  {
    label:"Sign out",
    icon: "pi pi-signout",
    command: ()=>{
      localStorage.clear();
      this.router.navigate(["login"]);
    }
    
  }
];

@Input()
get username():string{
  const storedUser = localStorage.getItem('token');
  if(storedUser)
    {
      const authResp: AuthenticationResponse = JSON.parse(storedUser);
      if(authResp && authResp.customerDTO && authResp.customerDTO.username){
        return authResp.customerDTO.username;
      }
    }
  return '--';
}

@Input()
get userRole():string{
  const storedUser = localStorage.getItem('token');
  if(storedUser)
    {
      const authResp: AuthenticationResponse = JSON.parse(storedUser);
      if(authResp && authResp.customerDTO && authResp.customerDTO.roles){
        return authResp.customerDTO.roles.join(" ");
      }
    }
  return '--';
}

}
