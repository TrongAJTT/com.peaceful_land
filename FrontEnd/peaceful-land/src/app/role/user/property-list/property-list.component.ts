import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PostService } from '../../../core/services/post.service';
import { AuthService } from '../../../core/services/auth.service';
import { ImageService } from '../../../core/services/image.service';
import { SnackBarService } from '../../../core/services/snack-bar.service';
import { firstValueFrom } from 'rxjs/internal/firstValueFrom';
import { User } from '../../../dto/user';

@Component({
  selector: 'app-property-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './property-list.component.html',
  styleUrl: './property-list.component.css'
})
export class PropertyListComponent implements OnInit,AfterViewInit {
  currentPage: number = 1;  // Trang mặc định là trang 1
  sizePage: number = 2;
  user!: User;
  userId: number = -1;
  postsImages: { [key: number]: string } = {};

  topK = 6;
  postList: any[] = [];


  constructor(
    private postService: PostService,
    private authService: AuthService,
    private imgService: ImageService,
    private snackbarService: SnackBarService,
    private cdr: ChangeDetectorRef
  ){}

  async ngOnInit(): Promise<void> {
    await this.loadPost();
  }

  async loadPost(): Promise<void>{
    if (typeof localStorage !== 'undefined') {
      if(this.authService.getAuthStatus()){
        this.user = (this.authService.getUserDetails());
        this.userId = this.user.id
      }
  
      const postList = await firstValueFrom(this.postService.searchPostByPage(this.userId,this.currentPage-1,this.sizePage))
      this.postList = postList.list_data
      console.log(postList)

      for(var post of this.postList){
        this.changeToImg(post.data.thumbnUrl,post.data.id);
      }
      this.cdr.detectChanges();
    }
  }

  async changeToImg(thumbnUrl: string, postId: number): Promise<void> {
    try {
      const response = await firstValueFrom(this.imgService.changeToImgBase64(thumbnUrl));
      this.postsImages[postId] = response;  // Lưu ảnh vào biến
    } catch (error:any) {
      this.postsImages[postId] = '/assets/img/house/house-demo.jpg';  // Đặt ảnh mặc định
    }
  }

  async ngAfterViewInit(): Promise<void> {
      await this.ngOnInit()
      this.cdr.detectChanges()
  }

  loadPage(page: number): void {
    this.currentPage = page;  // Cập nhật trang hiện tại
    this.loadPost();
  }


}
