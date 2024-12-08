import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-default',
  templateUrl: './default.component.html',
  styleUrl: './default.component.css'
})
export class DefaultComponent {
  // constructor(
  //   private route: ActivatedRoute,
  //   private router: Router) {
  //   let url = sessionStorage.getItem('currentUrl');
  //   if (url) {
  //     this.router.navigate(url.replace('#/', '').split('/'));
  //     sessionStorage.removeItem('currentUrl');
  //   }
  // }
}
