import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { firstValueFrom, Subject } from 'rxjs';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../core/services/post.service';
import { UserRequestService } from '../../../core/services/user-request.service';
import { Router } from '@angular/router';
import { DataTablesModule } from 'angular-datatables';
import { User } from '../../../dto/user';
import { AuthService } from '../../../core/services/auth.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { response } from 'express';
import { SnackBarService } from '../../../core/services/snack-bar.service';

@Component({
  selector: 'app-post-dashboard',
  standalone: true,
  imports: [DataTablesModule,CommonModule,ReactiveFormsModule],
  templateUrl: './post-dashboard.component.html',
  styleUrl: './post-dashboard.component.css'
})
export class PostDashboardComponent implements OnInit, AfterViewInit, OnDestroy{
  @ViewChildren('modalElement') modalElements!: QueryList<ElementRef>;
  @ViewChildren('modalOveralElement') modalOveralElement!: QueryList<ElementRef>;
  headerList = ['Mã bài đăng','Hình ảnh','Tiêu đề','Giá','Địa chỉ','Ngày tạo'
    ,'Ngày hết hạn yêu cầu'];
  dtTrigger: Subject<any> = new Subject<any>();
  postList!: any[];
  user!:User;
  userId = -1;
  modalImageSrc = '';
  isModalOpen = false;  
  isPriceDisabled: boolean = false;
  isRentalPeriodDisabled: boolean = false;
  actionInfo: string = '';
  today: any;
  // Chọn ảnh thumbnail
  selectedFile!: File;
  imagePreview: string | ArrayBuffer | null = null;
  updateOption: (keyof typeof this.dictOption)[] = [];

  postForm = new FormGroup({
    action: new FormControl(''),
    price: new FormControl(0),
    rentalPeriod: new FormControl(''),
    actionInfo: new FormControl(''),
  })

  postOveralForm = new FormGroup({
    title: new FormControl(''),
    description: new FormControl(''),
  })

  dictOption = {
    'sold': 'Đã bán',
    'rented': 'Đã cho thuê',
    're-sale': 'Bán lại',
    're-rent': 'Cho thuê lại',
    'rental-period': 'Gia hạn hợp đồng',
    'price': 'Điều chỉnh giá',
    'offer': 'Chuyển đổi hình thức',
  }

  constructor(
    private authService: AuthService,
    private postService:PostService,
    private userRequestService:UserRequestService,
    private router:Router,
    private snackbarService: SnackBarService,
    private cdr: ChangeDetectorRef,
  ){
    type DictOptionKeys = keyof typeof this.dictOption;
    let updateOption!: DictOptionKeys;
  }

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
     // Today
     const now = new Date(); 
     const month = ('0' + (now.getMonth() + 1)).slice(-2); 
     const day = ('0' + now.getDate()).slice(-2); 
     this.today = `${now.getFullYear()}-${month}-${day}`;
    if (typeof localStorage !== 'undefined') {
      if(this.authService.getAuthStatus()){
        this.user = (this.authService.getUserDetails());
        this.userId = this.user.id
      }
    }
    await (this.loadPostList())
    this.cdr.detectChanges();
  }

  async loadPostList():Promise<void>{
    try{
      this.postList = await firstValueFrom(this.postService.getAllPostByUserId(this.userId));
      const loadImagePromises = this.postList.map((post) => 
        post.thumbnUrl = `http://localhost:8080/api/images?path=${post.thumbnUrl}`
      );
      await Promise.all(loadImagePromises);
    }catch(e){
      this.postList=[]
    }
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
  openModal(imageSrc: string | ArrayBuffer | null) {
    if (typeof imageSrc === 'string') {
      this.modalImageSrc = imageSrc;
      this.isModalOpen = true;
    }
  }

  // Hàm đóng modal khi người dùng click ra ngoài ảnh
  closeModal(event: MouseEvent) {
    this.isModalOpen = false;
  }

  
  onImageError(post_log: any): void {
    post_log.thumbnail_url = '/assets/img/house/house-demo.jpg';
  }

  async openPostModal(post: any): Promise<void> {
    try{
      const stateUpdate = await firstValueFrom (this.postService.getUpdatePermission(post.id,this.userId))
      this.updateOption = stateUpdate.actions.filter((action:any) => action!='post'&&action!='discount');
    }catch(response:any){
      this.snackbarService.notifyErrorUser(response.error.message)
      return
    }

    const futureDate = new Date(); 
    futureDate.setMonth(futureDate.getMonth() + 3);
    const futureMonth = ('0' + (futureDate.getMonth() + 1)).slice(-2);
    const futureDay = ('0' + futureDate.getDate()).slice(-2);
    const rentalPeriod = `${futureDate.getFullYear()}-${futureMonth}-${futureDay}`;
    
    this.postForm.get('price')?.setValue(post.property.price)
    this.postForm.get('rentalPeriod')?.setValue(rentalPeriod)
    this.postForm.get('price')?.disable()
    this.postForm.get('rentalPeriod')?.disable()
    
    const modalElement = this.modalElements.find(element => element.nativeElement.id === `postModal${post.id}`);
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
  
  closePostModal(postId:any) {
    const modalElement = this.modalElements.find(element => element.nativeElement.id === `postModal${postId}`);
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

  submitPost(postId:any,event:Event){
    const postValues = this.postForm.value
    const currAction = postValues.action
    const currPrice = postValues.price
    const currRentalPeriod = postValues.rentalPeriod

    if(currAction == ""){
      this.snackbarService.notifyWarningUser("Vui lòng chọn hành động");
    }else if((currAction != 'sold' && currAction != 'rented' && currAction != 'offer') && (currPrice! < 0 || !Number.isInteger(currPrice))){
      this.snackbarService.notifyWarningUser("Giá tiền không hợp lệ");
    }else{
      this.postService.updateDetailPost(this.userId,postId,currAction!,currPrice,currRentalPeriod)
        .subscribe({
          next: async (response:any) => {
            this.snackbarService.notifySuccessUser(response)
            await (this.loadPostList());
            this.closePostModal(postId)
            this.reloadPostRequest()
            this.cdr.detectChanges()
          },
          error: (response: any) => this.snackbarService.notifyErrorUser(response.error.message)
        })
    }
  }

  reloadPostRequest(){
    this.postForm.get('price')?.disable();
    this.postForm.get('rentalPeriod')?.disable();
    this.postForm.get('action')?.setValue('');
    this.postForm.get('actionInfo')?.setValue('');
  }

  onActionChange(post: any) {
    const action = this.postForm.get('action')?.value;
    // Reset tất cả ô nhập liệu
    this.isPriceDisabled = false;
    this.isRentalPeriodDisabled = false;

    switch(action) {
      case 'sold':
      case 'rented':
        this.isPriceDisabled = true;
        this.isRentalPeriodDisabled = true;
        this.actionInfo = action === 'sold' ? 'Đã bán bất động sản.' : 'Đã cho thuê bất động sản.';
        break;
      case 're-sale':
      case 're-rent':
        this.isPriceDisabled = false;
        this.isRentalPeriodDisabled = action === 're-rent' ? false : true;
        this.actionInfo = action === 're-sale' ? 'Người mua bán lại bất động sản.' : 'Hết hợp đồng thuê, cho thuê lại bất động sản.';
        break;
      case 'rental-period':
        this.isPriceDisabled = false;
        this.isRentalPeriodDisabled = false;
        this.actionInfo = 'Chỉnh sửa, gia hạn hợp đồng cho thuê.';
        break;
      case 'price':
        this.isPriceDisabled = false;
        this.isRentalPeriodDisabled = true;
        this.actionInfo = 'Điều chỉnh giá của bất động sản.';
        break;
      case 'offer':
        this.actionInfo = post.property.offer ? 'Chỉnh sửa hình thức từ cho thuê thành mua.' : 'Chuyển đổi hình thức từ mua thành cho thuê.';
        if(post.property.offer){
          this.isPriceDisabled = false;
          this.isRentalPeriodDisabled = true;
        }else{
          this.isPriceDisabled = false;
          this.isRentalPeriodDisabled = false;
        }
        break;
      default:
        this.actionInfo = '';
    }

    if(this.isPriceDisabled){
      this.postForm.get('price')?.disable();
    }else{
      this.postForm.get('price')?.enable();
    }
    if(this.isRentalPeriodDisabled){
      this.postForm.get('rentalPeriod')?.disable();
    }else{
      this.postForm.get('rentalPeriod')?.enable();
    }

    // Cập nhật lại giá trị của actionInfo trong form
    this.postForm.get('actionInfo')?.setValue(this.actionInfo);
    this.cdr.detectChanges()
  }

  // Chọn thumbnail
  onImageSelected(event: any): void {
    this.selectedFile = event.target.files[0]; 
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
        this.cdr.detectChanges(); // Trigger change detection
      };
      reader.readAsDataURL(file);
    }
  }

  // Modal của tiêu đề, mô tả, hình nền
  
  async openPostOveralModal(post: any): Promise<void> {
    try{
      const stateUpdate = await firstValueFrom(this.postService.getUpdatePermission(post.id, this.userId));
      const postAction = stateUpdate.actions.find((action: any) => action === 'post');
      
      if (!postAction) {
          return;  // Exit the function if 'post' action is not found
      }
    }catch(response:any){
      this.snackbarService.notifyErrorUser(response.error.message)
      return
    }

    this.postOveralForm.get('title')?.setValue(post.title)
    this.postOveralForm.get('description')?.setValue(post.description)

    
    const modalElement = this.modalOveralElement.find(element => element.nativeElement.id === `postModalOveral${post.id}`);
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

  closePostOveralModal(postId: any){
    const modalElement = this.modalOveralElement.find(element => element.nativeElement.id === `postModalOveral${postId}`);
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


  submitPostOveral(postId: any,event: Event){
    const postOveralValue = this.postOveralForm.value 
    const title = postOveralValue.title?.trim();
    const description = postOveralValue.description?.trim();

    if(title == ""){
      this.snackbarService.notifyWarningUser("Vui lòng nhập tiêu đề")
    }else if(description == ""){
      this.snackbarService.notifyWarningUser("Vui lòng nhập mô tả")
    }else{
      this.postService.updateOveralPost(this.userId,postId,'post',title!,description!,this.selectedFile)
      .subscribe({
        next: async (response:any) => {
          this.snackbarService.notifySuccessUser(response)
          await (this.loadPostList());
          this.closePostOveralModal(postId)
          this.reloadPostRequest()
          this.cdr.detectChanges()
        },
        error: (response: any) => this.snackbarService.notifyErrorUser(response.error.message)
      })
    }
  }

  reloadPostOveralRequest(){
    this.postOveralForm.get('title')?.setValue('');
    this.postOveralForm.get('description')?.setValue('');
  }

  gotToDetailPost(post:any){
    this.router.navigate([`/user/post_detail/${post.id}`])
  }
}
