import { NgModule, OnInit } from '@angular/core';
import { Router, RouterModule, Routes } from '@angular/router';
import { AuthAdminGuard } from './core/guards/auth_admin.guard';
import { AuthService } from './core/services/auth.service';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';


const routes: Routes = [
  { path: 'user', loadChildren: () => import('./role/user/user.module').then(m => m.UserModule) },
  {
    path: 'admin',
    loadChildren: () => import('./role/admin/admin.module').then(m => m.AdminModule),
    canActivate: [AuthAdminGuard]
  },
  { path: '', redirectTo: 'user', pathMatch: 'full' },
  { path: '**', redirectTo: 'user' }
];



@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true ,onSameUrlNavigation: 'reload'})],
  exports: [RouterModule],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy }  // Sử dụng HashLocationStrategy
  ]
})
export class AppRoutingModule { 
}
