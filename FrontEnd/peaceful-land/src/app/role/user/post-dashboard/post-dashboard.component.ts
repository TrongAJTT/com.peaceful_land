import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { firstValueFrom, Subject } from 'rxjs';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../core/services/post.service';
import { UserRequestService } from '../../../core/services/user-request.service';
import { Router } from '@angular/router';
import { DataTablesModule } from 'angular-datatables';
import { User } from '../../../dto/user';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-post-dashboard',
  standalone: true,
  imports: [DataTablesModule,CommonModule],
  templateUrl: './post-dashboard.component.html',
  styleUrl: './post-dashboard.component.css'
})
export class PostDashboardComponent implements OnInit, AfterViewInit, OnDestroy{
  headerList = ['Mã bài đăng','Trạng thái','Tiêu đề','Địa chỉ','Ngày yêu cầu'
    ,'Ngày hết hạn yêu cầu','Hiển thị'];
  dtTrigger: Subject<any> = new Subject<any>();
  postList!: any[];
  user!:User;
  userId = -1;

  
  constructor(
    private authService: AuthService,
    private postService:PostService,
    private userRequestService:UserRequestService,
    private router:Router,
    private cdr: ChangeDetectorRef,
  ){}

  private dtOptions: any = {
    pagingType: 'full_numbers',
    pageLength: 5,
    processing: true,
    lengthMenu: [5, 10, 25, 50, 100],
    searching: true,
    language: {
      search: "Tìm kiếm:",
      searchPlaceholder: "Tìm kiếm",
      lengthMenu: "Hiển thị _MENU_ mục",
      info: "Hiển thị _START_ đến _END_ của _TOTAL_ mục",
      infoEmpty: "Không có dữ liệu",
      paginate: {
        first: "Đầu",
        last: "Cuối",
        next: "Tiếp",
        previous: "Trước"
      },
      emptyTable: "Không có dữ liệu trong bảng"
    }
  };

  async ngOnInit(): Promise<void> {
    if (typeof localStorage !== 'undefined') {
      if(this.authService.getAuthStatus()){
        this.user = (this.authService.getUserDetails());
        this.userId = this.user.id
      }
    }
    try{
      this.postList = await firstValueFrom(this.postService.getAllPostByUserId(this.userId));
    }catch(e){
      this.postList=[]
    }
    this.cdr.detectChanges();
  }

  async ngAfterViewInit(): Promise<void> {
    await (this.ngOnInit())
    this.initializeDataTable();
    this.cdr.detectChanges();
  }

  initializeDataTable(): void {
    $('#dataTablePostOfUser').DataTable({
      ...this.dtOptions,
      destroy: true 
    });

    this.dtTrigger.next(true);
  }

  ngOnDestroy(): void {
    $('#dataTablePostOfUser').DataTable({
      destroy: true 
    });
  }

  trackById(id: number, post: any): number{
    return post.id
  }

  goToHandle(postId: number){
    this.router.navigate([`admin/post_pending/handle/${postId}`])
  }

}
