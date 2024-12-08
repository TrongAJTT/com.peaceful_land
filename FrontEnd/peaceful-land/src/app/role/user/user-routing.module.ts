import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DefaultComponent } from './default/default.component';
import { HomeComponent } from './home/home.component';
import { LoginAndRegisterComponent } from './login-and-register/login-and-register.component';

const routes: Routes = [
  {path: "", component: DefaultComponent,children: [
    { path: "home", component: HomeComponent, pathMatch: "full" },
    { path: 'login_and_register', component: LoginAndRegisterComponent},
    { path: "**", redirectTo: "home", pathMatch: "full"},
  ]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
