import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewEncapsulation } from '@angular/core';
import { PostService } from '../../../core/services/post.service';
import { firstValueFrom, Subject } from 'rxjs';
import { UserRequestService } from '../../../core/services/user-request.service';
import { DataTablesModule } from 'angular-datatables';
import { Router } from '@angular/router';
// import 'datatables.net';   

@Component({
  selector: 'app-post-pending',
  standalone: true,
  imports: [DataTablesModule,CommonModule],
  templateUrl: './post-pending.component.html',
  styleUrl: './post-pending.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.Emulated,
})
export class PostPendingComponent implements OnInit, AfterViewInit{
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
