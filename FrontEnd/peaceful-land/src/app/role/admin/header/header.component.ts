import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{
  @ViewChild('dropDownLi') dropDownLi!: ElementRef;
  @ViewChild('dropDownMenu') dropDownMenu!: ElementRef;
  userName = "";

  constructor(private authService: AuthService,private router:Router){}


  ngOnInit(): void {
      this.userName = this.authService.getUserDetails()?.name ?? "";
  }


  toggleDropdown() {
    if(!this.dropDownLi.nativeElement.classList.contains("show")){
      this.dropDownLi.nativeElement.classList.add("show");
      this.dropDownMenu.nativeElement.classList.add("show");
    }else {
      this.dropDownLi.nativeElement.classList.remove("show");
      this.dropDownMenu.nativeElement.classList.remove("show");
    }
  }

  handleLogout(){
    this.authService.logout();
    this.router.navigate([''])
  }
}
