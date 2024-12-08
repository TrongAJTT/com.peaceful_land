import { AfterViewInit, Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements AfterViewInit{
  isLoggedIn: boolean = false;
  @ViewChild('nav_header') navBarElement!: ElementRef;
  @ViewChild('navbar_collapse') navbarCollapse!: ElementRef;

  constructor(
    private authService: AuthService
  ){}

  ngAfterViewInit(): void {
    this.isLoggedIn = this.authService.getAuthStatus();
  }
  
  @HostListener('window:scroll', ['$event'])
  onScroll () {
    if (window.scrollY > 45) {
      this.navBarElement.nativeElement.classList.add('sticky-top');
    } else {
      this.navBarElement.nativeElement.classList.remove('sticky-top');
    }
  }

  toggleNavbarCollapse(){
    if(this.navbarCollapse.nativeElement.classList.contains("show")){
      this.navbarCollapse.nativeElement.classList.remove("show")
    }else{
      this.navbarCollapse.nativeElement.classList.add("show")
    }
  }
}
