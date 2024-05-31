import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './components/customer/customer.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './services/guard/access-guard.service';
import { RegistrationComponent } from './components/registration/registration.component';



const routes: Routes = [
      {
        path: 'customer',
        component: CustomerComponent,
        canActivate: [AuthGuard]
      },
      {
        path: 'login',
        component: LoginComponent
      },
      {
        path: 'signup',
        component: RegistrationComponent
      },
      {
        path:'',
        redirectTo: 'login',
        pathMatch: "full"
      }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
