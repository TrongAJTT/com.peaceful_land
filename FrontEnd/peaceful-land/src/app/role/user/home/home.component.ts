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
    private cdr: ChangeDetectorRef
  ){}

  async ngOnInit(): Promise<void> {
    if (typeof localStorage !== 'undefined') {
      if(this.authService.getAuthStatus()){
        this.user = (this.authService.getUserDetails());
        this.userId = this.user.id
      }
  
      const postList = await firstValueFrom(this.postService.getPostTopK(this.topK,this.userId))
      this.new6Post = postList
      this.cdr.detectChanges();
    }
  }

  async ngAfterViewInit(): Promise<void> {
    await this.ngOnInit()
    this.cdr.detectChanges();
  }

  changeToImg(thumbnUrl: string){
    this.imgService.changeToImgBase64(thumbnUrl)
      .subscribe({
        next: (response: any) => {
          console.log(response)
          return "";
        },
        error: (response:any) => {
          this.snackbarService.notifyErrorUser(response.error.message);
          return ""
        }
      })
    return ""
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.cdr.detectChanges()
  }
}
