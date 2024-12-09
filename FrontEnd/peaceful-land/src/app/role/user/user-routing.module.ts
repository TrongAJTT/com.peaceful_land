import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DefaultComponent } from './default/default.component';
import { HomeComponent } from './home/home.component';
import { LoginAndRegisterComponent } from './login-and-register/login-and-register.component';
import { PostPropertyComponent } from './post-property/post-property.component';
import { PropertyListComponent } from './property-list/property-list.component';

const routes: Routes = [
  {path: "", component: DefaultComponent,children: [
    { path: "home", component: HomeComponent, pathMatch: "full" },
    { path: 'login_and_register', component: LoginAndRegisterComponent},
    { path: 'property_list', component: PropertyListComponent},
    { path: 'post_property', component: PostPropertyComponent},
    { path: "**", redirectTo: "home", pathMatch: "full"},
  ]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
