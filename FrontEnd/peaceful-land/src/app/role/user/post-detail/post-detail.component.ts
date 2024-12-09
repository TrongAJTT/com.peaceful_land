import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SnackBarService } from '../../../core/services/snack-bar.service';
import { PostService } from '../../../core/services/post.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.css'
})
export class PostDetailComponent implements OnInit, AfterViewInit{
  currPost: any;


  constructor(
    private cdr: ChangeDetectorRef,
    private snackbarService:SnackBarService,
    private postService: PostService,
    private authService: AuthService
  ){}

  async ngOnInit(): Promise<void> {
    await this.loadPost()
  }

  loadPost(){

  }

  async ngAfterViewInit(): Promise<void> {
    await this.ngOnInit();
  }


}
