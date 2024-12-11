import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { DataTablesModule } from 'angular-datatables';
import { firstValueFrom, Subject } from 'rxjs';
import { UserRequestService } from '../../../core/services/user-request.service';
import { PostService } from '../../../core/services/post.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-approved',
  standalone: true,
  imports: [CommonModule,DataTablesModule],
  templateUrl: './post-approved.component.html',
  styleUrl: './post-approved.component.css'
})
export class PostApprovedComponent implements OnInit, AfterViewInit, OnDestroy{
  headerList = ['Mã yêu cầu','Trạng thái','Tiêu đề','Địa chỉ','Ngày yêu cầu'
    ,'Ngày hết hạn yêu cầu','Hiển thị'];
  dtTrigger: Subject<any> = new Subject<any>();
  reqPostList!: any[];

  constructor(
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
    try{
      this.reqPostList = await firstValueFrom(this.userRequestService.getAllPostUserReq('pending'));
    }catch(e){
      this.reqPostList = []
    }
    this.cdr.detectChanges();
  }


  async ngAfterViewInit(): Promise<void> {
    await (this.ngOnInit())
    this.initializeDataTable();
    this.cdr.detectChanges();
  }

  initializeDataTable(): void {
    $('#dataTablePostUserReqApproved').DataTable({
      ...this.dtOptions,
      destroy: true 
    });

    this.dtTrigger.next(true);
  }

  ngOnDestroy(): void {
    $('#dataTablePostUserReqApproved').DataTable({
      destroy: true 
    });
  }

  trackById(id: number, post: any): number{
    return post.id
  }

  goToDetail(reqPostId: number){
    this.router.navigate([`admin/post_approved/detail/${reqPostId}`])
  }
}
