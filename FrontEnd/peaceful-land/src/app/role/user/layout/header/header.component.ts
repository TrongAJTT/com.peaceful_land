import { Component, ElementRef, HostListener, ViewChild } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  @ViewChild('nav_header') navBarElement!: ElementRef;
  @ViewChild('navbar_collapse') navbarCollapse!: ElementRef;

  
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
