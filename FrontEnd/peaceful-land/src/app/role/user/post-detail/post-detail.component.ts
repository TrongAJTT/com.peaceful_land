import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SnackBarService } from '../../../core/services/snack-bar.service';
import { PostService } from '../../../core/services/post.service';
import { AuthService } from '../../../core/services/auth.service';
import { User } from '../../../dto/user';
import { ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ImageService } from '../../../core/services/image.service';

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.css'
})
export class PostDetailComponent implements OnInit, AfterViewInit{
  currPost: any;
  user!: User;
  userId: number = -1;
  postId!: number
  postsImages: { [key: number]: string } = {};

  constructor(
    private cdr: ChangeDetectorRef,
    private snackbarService:SnackBarService,
    private postService: PostService,
    private activeRoute: ActivatedRoute,
    private authService: AuthService,
    private imgService: ImageService,
  ){}

  async ngOnInit(): Promise<void> {
    if (typeof localStorage !== 'undefined') {
      if(this.authService.getAuthStatus()){
        this.user = (this.authService.getUserDetails());
        this.userId = this.user.id
      }
    }

    (this.activeRoute.params.subscribe(params => {
      this.postId = +params['id'];
    }))
    await this.loadPost()
    this.cdr.detectChanges();
  }

  async loadPost() : Promise<void>{
    this.currPost = await firstValueFrom(this.postService.getPostById(this.userId,this.postId))
  }

  async ngAfterViewInit(): Promise<void> {
    await this.ngOnInit();
    this.cdr.detectChanges();
  }

  async changeToImg(thumbnUrl: string, postId: number): Promise<void> {
    try {
      const response = await firstValueFrom(this.imgService.changeToImgBase64(thumbnUrl));
      this.postsImages[postId] = response;  // Lưu ảnh vào biến
    } catch (error:any) {
      this.postsImages[postId] = '/assets/img/house/house-demo.jpg';  // Đặt ảnh mặc định
    }
  }


}
