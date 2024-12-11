import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { firstValueFrom, Subject } from 'rxjs';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../core/services/post.service';
import { UserRequestService } from '../../../core/services/user-request.service';
import { Router } from '@angular/router';
import { DataTablesModule } from 'angular-datatables';
import { User } from '../../../dto/user';
import { AuthService } from '../../../core/services/auth.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-post-dashboard',
  standalone: true,
  imports: [DataTablesModule,CommonModule,ReactiveFormsModule],
  templateUrl: './post-dashboard.component.html',
  styleUrl: './post-dashboard.component.css'
})
export class PostDashboardComponent implements OnInit, AfterViewInit, OnDestroy{
  @ViewChild('postModal') postModal: any; 
  headerList = ['Mã bài đăng','Hình ảnh','Tiêu đề','Giá','Địa chỉ','Ngày tạo'
    ,'Ngày hết hạn yêu cầu'];
  dtTrigger: Subject<any> = new Subject<any>();
  postList!: any[];
  user!:User;
  userId = -1;
  modalImageSrc = '';
  isModalOpen = false;  

  postForm = new FormGroup({
    name: new FormControl(''),
    phone: new FormControl(''),
    email: new FormControl(''),
    message: new FormControl(''),
    interest_level: new FormControl(true),
  })

  
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
      const loadImagePromises = this.postList.map((post) => 
        post.thumbnUrl = `http://localhost:8080/api/images?path=${post.thumbnUrl}`
      );
      await Promise.all(loadImagePromises);
    }catch(e){
      this.postList=[]
    }
    console.log(this.postList)
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

  // Hàm mở modal với ảnh được chọn
  openModal(imageSrc: string) {
    this.modalImageSrc = imageSrc;
    this.isModalOpen = true;
  }

  // Hàm đóng modal khi người dùng click ra ngoài ảnh
  closeModal(event: MouseEvent) {
    this.isModalOpen = false;
  }

  
  onImageError(post_log: any): void {
    post_log.thumbnail_url = '/assets/img/house/house-demo.jpg';
  }

  openPostModal() {
    const modalElement = this.postModal.nativeElement as HTMLElement;
    const backdrop = document.createElement('div');
    backdrop.className = 'modal-backdrop fade';
    document.body.appendChild(backdrop); // Add the backdrop
    modalElement.style.display = 'block';
    setTimeout(() => {
      modalElement.classList.add('show');
      backdrop.classList.add('show');
    }, 10); // Delay slightly to allow for style change before adding 'show'
  
    document.body.classList.add('modal-open');
  }
  
  closePostModal() {
    const modalElement = this.postModal.nativeElement as HTMLElement;
    const modalBackdrop = document.querySelector('.modal-backdrop') as HTMLElement;
    // Fade out the modal and backdrop
    modalElement.classList.remove('show');
    modalBackdrop?.classList.remove('show');
    setTimeout(() => {
      modalElement.style.display = 'none';
      modalBackdrop?.remove();
    }, 300); // Match the duration of the fade-out transition
    document.body.classList.remove('modal-open');
    document.body.style.overflow = ''; // Reset overflow to allow scrolling
  }

  submitPost(event:Event){

  }
}
