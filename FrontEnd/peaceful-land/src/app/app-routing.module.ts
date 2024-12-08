import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
// import { AuthAdminGuard } from './core/guards/auth_admin.guard';
// import { AuthStaffGuard } from './core/guards/auth_staff.guard';


const routes: Routes = [
  { path: 'user', loadChildren: () => import('./role/user/user.module').then(m => m.UserModule) },
  {
    path: 'admin',
    loadChildren: () => import('./role/admin/admin.module').then(m => m.AdminModule),
    // canActivate: [AuthAdminGuard]
  },
//   { path: 'staff', loadChildren: () => import('./Modules/staff/staff.module').then(m => m.StaffModule), canActivate: [AuthStaffGuard] },
  { path: '', redirectTo: 'user', pathMatch: 'full' },
  { path: '**', redirectTo: 'user' }
];



@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule { }
