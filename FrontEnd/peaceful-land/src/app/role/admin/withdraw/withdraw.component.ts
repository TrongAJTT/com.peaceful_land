import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, QueryList, ViewChildren, ViewEncapsulation } from '@angular/core';
import { DataTablesModule } from 'angular-datatables';
import { UserRequestService } from '../../../core/services/user-request.service';
import { Router } from '@angular/router';
import { firstValueFrom, Subject } from 'rxjs';
import { response } from 'express';
import { SnackBarService } from '../../../core/services/snack-bar.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-withdraw',
  standalone: true,
  imports: [DataTablesModule,CommonModule,ReactiveFormsModule],
  templateUrl: './withdraw.component.html',
  styleUrl: './withdraw.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.Emulated,
})
export class WithdrawComponent  implements OnInit, AfterViewInit, OnDestroy{
  headerList = ['Mã yêu cầu','Trạng thái','Mã người dùng','Họ tên',
    'Số tiền','Thông tin từ chối','Ngày yêu cầu'];
  dtTrigger: Subject<any> = new Subject<any>();
  withdrawList!: any[];
  @ViewChildren('modalElement') modalElements!: QueryList<ElementRef>;

  rejectForm = new FormGroup({
    message: new FormControl(''),
  })

  constructor(
    private userRequestService:UserRequestService,
    private router:Router,
    private snackbarService:SnackBarService,
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
      this.withdrawList = await firstValueFrom(this.userRequestService.getWithdrawReq());
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
    $('#dataTableWithdrawUserReq').DataTable({
      destroy: true 
    });
  }

  trackById(id: number, post: any): number{
    return post.id
  }

  
  acceptReq(withdraw: any){
    if(withdraw.status != 'Đang chờ xử lý'){
      this.snackbarService.notifyErrorUser("Yêu cầu đã được xử lý");
      return
    }

    this.userRequestService.rejectOrApproveWithdraw(withdraw.id,'approved','')
      .subscribe({
        next: async (response:any) =>{
          this.snackbarService.notifySuccessUser(response);
          this.withdrawList = await firstValueFrom(this.userRequestService.getWithdrawReq());
          this.cdr.detectChanges()
        },
        error: (response:any) => {
          this.snackbarService.notifyErrorUser(response.error.message)
        }
      })
  }

  openRejectModal(withdraw: any){
    if(withdraw.status != 'Đang chờ xử lý'){
      this.snackbarService.notifyErrorUser("Yêu cầu đã được xử lý");
      return
    }else{
      const modalElement = this.modalElements.find(element => element.nativeElement.id === `withdrawModal${withdraw.id}`);
      if(modalElement){
        const modalNativeElement = modalElement.nativeElement as HTMLElement
        const backdrop = document.createElement('div');
        backdrop.className = 'modal-backdrop fade';
        document.body.appendChild(backdrop); // Add the backdrop
        modalNativeElement.style.display = 'block';
        setTimeout(() => {
          modalNativeElement.classList.add('show');
          backdrop.classList.add('show');
        }, 10); // Delay slightly to allow for style change before adding 'show'
      
        document.body.classList.add('modal-open');
      }
    }
  }


  closeRejectModal(withdrawId:any) {
    const modalElement = this.modalElements.find(element => element.nativeElement.id === `withdrawModal${withdrawId}`);
    if(modalElement){
      const modalNativeElement = modalElement.nativeElement as HTMLElement

      const modalBackdrop = document.querySelector('.modal-backdrop') as HTMLElement;
      // Fade out the modal and backdrop
      modalNativeElement.classList.remove('show');
      modalBackdrop?.classList.remove('show');
      setTimeout(() => {
        modalNativeElement.style.display = 'none';
        modalBackdrop?.remove();
      }, 300); // Match the duration of the fade-out transition
      document.body.classList.remove('modal-open');
      document.body.style.overflow = ''; // Reset overflow to allow scrolling
    }
  }

  submitReject(withdrawId:any,event:Event){
    const message = this.rejectForm.get('message')!.value

    if(message?.trim()==""){
      this.snackbarService.notifyWarningUser("Vui lòng nhập lý do từ chối");
    }else{
      this.userRequestService.rejectOrApproveWithdraw(withdrawId,'rejected',message!)
      .subscribe({
        next: async (response) => {
          this.snackbarService.notifySuccessUser(response)
          this.closeRejectModal(withdrawId);
          this.withdrawList = await firstValueFrom(this.userRequestService.getWithdrawReq());
          this.cdr.detectChanges()
          this.cdr.detectChanges()
        },
        error: (response) =>{
          this.snackbarService.notifyErrorUser(response)
        }
      })
    }
  }
}
