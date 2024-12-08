import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import { DefaultComponent } from './default/default.component';
import { HeaderComponent } from './layout/header/header.component';
import { FooterComponent } from './layout/footer/footer.component';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { provideClientHydration } from '@angular/platform-browser';
import { LoginAndRegisterComponent } from './login-and-register/login-and-register.component';


@NgModule({
  declarations: [
    DefaultComponent,
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    LoginAndRegisterComponent,
    CommonModule,
    UserRoutingModule
  ],
  exports: [
    LoginAndRegisterComponent
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient(withFetch())
  ]
})
export class UserModule { }
