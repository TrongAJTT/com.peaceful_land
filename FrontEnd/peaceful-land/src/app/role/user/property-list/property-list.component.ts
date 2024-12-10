import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component,  OnChanges,  OnInit, SimpleChanges } from '@angular/core';
import { PostService } from '../../../core/services/post.service';
import { AuthService } from '../../../core/services/auth.service';
import { ImageService } from '../../../core/services/image.service';
import { SnackBarService } from '../../../core/services/snack-bar.service';
import { firstValueFrom } from 'rxjs/internal/firstValueFrom';
import { User } from '../../../dto/user';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-property-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './property-list.component.html',
  styleUrl: './property-list.component.css'
})
export class PropertyListComponent implements OnInit,AfterViewInit  {
  currentPage: number = 1;  // Trang mặc định là trang 1
  sizePage: number = 2;
  user!: User;
  userId: number = -1;
  postsImages: { [key: number]: string } = {};
  propertyType: string | null = null;
  private routeSub: Subscription | undefined;

  topK = 6;
  postList: any[] = [];


  constructor(
    private postService: PostService,
    private authService: AuthService,
    private imgService: ImageService,
    private snackbarService: SnackBarService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ){
  }

  async ngOnInit(): Promise<void> {
    await this.loadParamRoute();
    await this.loadPost();
    this.cdr.detectChanges()

    this.routeSub = this.route.paramMap.subscribe(async (params) => {
      this.propertyType = params.get('type');
      this.currentPage = 1
      await this.loadPost();  // Re-load posts when the type changes
      this.cdr.detectChanges();
    });
  }


  async loadParamRoute(): Promise<void>{
    const params = await firstValueFrom(this.route.paramMap)
    this.propertyType = params.get('type');
  }

  async loadPost(): Promise<void>{
    if (typeof localStorage !== 'undefined') {
      if(this.authService.getAuthStatus()){
        this.user = (this.authService.getUserDetails());
        this.userId = this.user.id
      }

      let offer,status; 
      switch(this.propertyType){
        case 'rent':
          offer = true;
          status = null;
          break;
        case 'sale':
          offer = false;
          status = null;
          break;
        default:
          offer = null;
          status = true
      }
      
  
      const postList = await firstValueFrom(this.postService.searchPostByPage(this.userId,offer!,status!,this.currentPage-1,this.sizePage))
      this.postList = postList.list_data

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
    this.cdr.detectChanges()
  }

  goToDetailPost(postId: number){
    this.router.navigate([`/user/post_detail/${postId}`])
  }
}
