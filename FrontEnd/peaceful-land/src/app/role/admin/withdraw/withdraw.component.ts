import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { DataTablesModule } from 'angular-datatables';
import { UserRequestService } from '../../../core/services/user-request.service';
import { Router } from '@angular/router';
import { firstValueFrom, Subject } from 'rxjs';

@Component({
  selector: 'app-withdraw',
  standalone: true,
  imports: [DataTablesModule,CommonModule],
  templateUrl: './withdraw.component.html',
  styleUrl: './withdraw.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.Emulated,
})
export class WithdrawComponent  implements OnInit, AfterViewInit, OnDestroy{
  headerList = ['Mã yêu cầu','Trạng thái','Mã người dùng','Số tiền',
    'Họ tên','Thông tin từ chối','Ngày yêu cầu'];
  dtTrigger: Subject<any> = new Subject<any>();
  withdrawList!: any[];
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
      this.withdrawList = await firstValueFrom(this.userRequestService.getAllPostUserReq('pending'));
    }catch(e){
      this.withdrawList = []
    }
    this.cdr.detectChanges();
  }

  async ngAfterViewInit(): Promise<void> {
    await (this.ngOnInit())
    this.initializeDataTable();
    this.cdr.detectChanges();
  }

  initializeDataTable(): void {
    $('#dataTableWithdrawUserReq').DataTable({
      ...this.dtOptions,
      destroy: true 
    });

    this.dtTrigger.next(true);
  }

  
  ngOnDestroy(): void {
    $('#dataTablePostUserReq').DataTable({
      destroy: true 
    });
  }

  trackById(id: number, post: any): number{
    return post.id
  }
}
