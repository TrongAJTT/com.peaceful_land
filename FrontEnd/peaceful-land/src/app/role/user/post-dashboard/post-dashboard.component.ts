import { ChangeDetectorRef, Component } from '@angular/core';
import { firstValueFrom, Subject } from 'rxjs';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../core/services/post.service';
import { UserRequestService } from '../../../core/services/user-request.service';
import { Router } from '@angular/router';
import { DataTablesModule } from 'angular-datatables';

@Component({
  selector: 'app-post-dashboard',
  standalone: true,
  imports: [DataTablesModule,CommonModule],
  templateUrl: './post-dashboard.component.html',
  styleUrl: './post-dashboard.component.css'
})
export class PostDashboardComponent {
  headerList = ['Mã bài đăng','Trạng thái','Tiêu đề','Địa chỉ','Ngày yêu cầu'
    ,'Ngày hết hạn yêu cầu','Hiển thị'];
  dtTrigger: Subject<any> = new Subject<any>();
  postList!: any[];

  
  constructor(
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
    this.postList = await firstValueFrom(this.userRequestService.getAllPost('pending'));
    this.cdr.detectChanges();
  }

  async ngAfterViewInit(): Promise<void> {
    await (this.ngOnInit())
    this.initializeDataTable();
    this.cdr.detectChanges();
  }

  initializeDataTable(): void {
    $('#dataTablePostUserReq').DataTable({
      ...this.dtOptions,
      destroy: true 
    });

    this.dtTrigger.next(true);
  }

  trackById(id: number, post: any): number{
    return post.id
  }

  goToHandle(postId: number){
    this.router.navigate([`admin/post_pending/handle/${postId}`])
  }

}
