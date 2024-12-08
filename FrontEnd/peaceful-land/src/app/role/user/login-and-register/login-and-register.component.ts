import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { SnackBarService } from '../../../core/services/snack-bar.service';

@Component({
  selector: 'app-login-and-register',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './login-and-register.component.html',
  styleUrl: './login-and-register.component.css'
})
export class LoginAndRegisterComponent {
  @ViewChild('container') container!: ElementRef;
  user: any;
  loginForm = new FormGroup({
    userId_login: new FormControl(''),
    password_login: new FormControl(''),
  })

  registerForm = new FormGroup({
    name: new FormControl(''),
    phone: new FormControl(''),
    email: new FormControl(''),
    password: new FormControl(''),
    gender: new FormControl(true)
  })

  constructor(
    private snackbarService:SnackBarService,
    public authService:AuthService,
    private router:Router,
  ){}

  ngOnInit(): void {
    this.user = this.authService.getUserDetails();
  }

  handleLogin(event:Event){
    event.preventDefault();
    const userId_login = this.loginForm.get("userId_login")?.value?.trim() || ""; 
    const password_login = this.loginForm.get("password_login")?.value?.trim() || ""; 
    if (userId_login==="") {
      this.snackbarService.notifyWarningUser("Vui lòng nhập email hoặc mật khẩu");
    }else if (password_login===""){
      this.snackbarService.notifyWarningUser("Vui lòng nhập mật khẩu");
    }else{
      this.authService.login(userId_login,password_login)
        .subscribe({
          next: (response:any) => {
            window.location.reload();
          },
          error: (response:any) => {
            this.snackbarService.notifyErrorUser(response.error.message)
          }
        })
    }
  }

  handleRegister(event:Event){
    event.preventDefault();
    const emailPattern= /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    const email = this.registerForm.get("email")?.value?.trim() || ""; 
    const password = this.registerForm.get("password")?.value?.trim() || ""; 
    const phone = this.registerForm.get("phone")?.value?.trim() || ""; 
    const name = this.registerForm.get("name")?.value?.trim() || ""; 
    const gender = this.registerForm.get("gender")?.value; 
    
    if(name == ""){
      this.snackbarService.notifyWarningUser("Vui lòng nhập họ tên");
    }else if(phone=="") {
      this.snackbarService.notifyWarningUser("Vui lòng nhập số điện thoại");
    }else if (email==="") {
      this.snackbarService.notifyWarningUser("Vui lòng nhập email");
    }else if(!emailPattern.test(email)){
      this.snackbarService.notifyWarningUser("Email không hợp lệ");
    }else if (password===""){
      this.snackbarService.notifyWarningUser("Vui lòng nhập mật khẩu");
    }else{
      this.authService.register(email,password,phone,name,gender!)
        .subscribe({
          next: (response:any) => {
            this.snackbarService.notifySuccessUser("Đăng kí thành công");
            this.registerForm.get("email")?.setValue("");
            this.registerForm.get("password")?.setValue("");
            this.registerForm.get("phone")?.setValue("");
            this.registerForm.get("name")?.setValue("");
            this.container.nativeElement.classList.remove("right-panel-active");
          },
          error: (response:any) => {
            console.log(response);
            this.snackbarService.notifyErrorUser(response.error.message)
          }
        })
    }
  }

  toLogin(){
    this.container.nativeElement.classList.remove("right-panel-active");
  }

  toRegister(){
    this.container.nativeElement.classList.add("right-panel-active");
  }

  logout(){
    this.authService.logout();
    this.snackbarService.notifySuccessUser("Đăng xuất thành công");
  }
}
