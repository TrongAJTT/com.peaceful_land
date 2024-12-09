import { AfterViewInit, ChangeDetectorRef, Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { PostService } from '../../../core/services/post.service';
import { AuthService } from '../../../core/services/auth.service';
import { User } from '../../../dto/user';
import { CommonModule } from '@angular/common';
import { SnackBarService } from '../../../core/services/snack-bar.service';
import { error } from 'console';
import { firstValueFrom } from 'rxjs';
import { ImageService } from '../../../core/services/image.service';
import { response } from 'express';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit, AfterViewInit, OnChanges{
  new6Post: any[] = [];
  userId = -1;
  topK = 6;
  user!: User;

  constructor(
    private postService: PostService,
    private authService: AuthService,
    private imgService: ImageService,
    private snackbarService: SnackBarService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ){}

  async ngOnInit(): Promise<void> {
    if (typeof localStorage !== 'undefined') {
      if(this.authService.getAuthStatus()){
        this.user = (this.authService.getUserDetails());
        this.userId = this.user.id
      }
  
      const postList = await firstValueFrom(this.postService.getPostTopK(this.topK,this.userId))
      this.new6Post = postList

      for(var post of this.new6Post){
        this.changeToImg(post.data.thumbnUrl,post.data.id);
      }
      this.cdr.detectChanges();
    }
  }

  async ngAfterViewInit(): Promise<void> {
    await this.ngOnInit()
    this.cdr.detectChanges();
  }

  postsImages: { [key: number]: string } = {};  // Lưu trữ ảnh cho từng post

  async changeToImg(thumbnUrl: string, postId: number): Promise<void> {
    try {
      const response = await firstValueFrom(this.imgService.changeToImgBase64(thumbnUrl));
      this.postsImages[postId] = response;  // Lưu ảnh vào biến
    } catch (error:any) {
      this.postsImages[postId] = '/assets/img/house/house-demo.jpg';  // Đặt ảnh mặc định
    }
  }

  goToPropertyList(){
    this.router.navigate(['user/property_list'])
  }
  
  
  ngOnChanges(changes: SimpleChanges): void {
    this.cdr.detectChanges()
  }
}
