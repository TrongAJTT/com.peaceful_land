import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { User } from '../../../../dto/user';
import { SnackBarService } from '../../../../core/services/snack-bar.service';
import { PostService } from '../../../../core/services/post.service';
import { PropertyService } from '../../../../core/services/property.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ImageService } from '../../../../core/services/image.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { firstValueFrom } from 'rxjs';
import { UserRequestService } from '../../../../core/services/user-request.service';

@Component({
  selector: 'app-handle-post-pending',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './handle-post-pending.component.html',
  styleUrl: './handle-post-pending.component.css',
  encapsulation: ViewEncapsulation.Emulated,
})
export class HandlePostPendingComponent implements OnInit, AfterViewInit{
  @ViewChild('rejectModal') rejectModal: any; 

  currPost: any;
  user!: User;
  userId: number = -1;
  today: any;
  postId!: number
  postsThumbImage: string = "";
  activeImageIndex = 0;  // Chỉ số của hình ảnh hiện tại
  currChildImg = 0;
  imageList = [
    { src: '/assets/img/house/house-demo.jpg' },
    { src: '/assets/img/house/house-demo-1.jpg' },
    { src: '/assets/img/house/house-demo-2.jpg' },
    { src: '/assets/img/house/house-demo-3.jpg' },
  ];
  modalImageSrc = '';
  isModalOpen = false;  // Trạng thái của modal
  itemsToShow = 3;  // Số lượng ảnh hiển thị một lần

  rejectForm = new FormGroup({
    message: new FormControl(''),
  })

  constructor(
    private cdr: ChangeDetectorRef,
    private snackbarService:SnackBarService,
    private postService: PostService,
    private propertyService: PropertyService,
    private activeRoute: ActivatedRoute,
    private authService: AuthService,
    private imgService: ImageService,
    private userRequestService: UserRequestService,
    private modalService: NgbModal,
    private router:Router,
  ){}


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

    (this.activeRoute.params.subscribe(params => {
      this.postId = +params['id'];
    }))
    await this.loadPost()
    await this.loadProImages()
    this.cdr.detectChanges();
  }

  async ngAfterViewInit(): Promise<void> {
    await this.ngOnInit();
    this.cdr.detectChanges();
  }

  async loadPost() : Promise<void>{
    this.currPost = await firstValueFrom(this.userRequestService.getPostById(this.postId))
    this.changeToImg(this.currPost.data.thumbnUrl);
  }

  async loadProImages() : Promise<void>{
    const proImages = await firstValueFrom(this.propertyService.getProImages(this.currPost.data.property.id))
  }

  async changeToImg(thumbUrl: string): Promise<void> {
    try {
      const response = await firstValueFrom(this.imgService.changeToImgBase64(thumbUrl));
      this.postsThumbImage = response;  // Lưu ảnh vào biến
    } catch (error:any) {
      this.postsThumbImage = '/assets/img/house/house-demo.jpg';  // Đặt ảnh mặc định
    }
  }

  
  // Hàm quay lại ảnh trước
  prevImage() {
    if(this.activeImageIndex!=0){
      if(this.activeImageIndex==2){
        this.activeImageIndex=1;
      }else{
        if(this.currChildImg==1){
          this.activeImageIndex=0;
        }
      }
      this.currChildImg--;
    }
    
  }

  // Hàm chuyển đến ảnh tiếp theo
  nextImage() {
    if(this.activeImageIndex!=2){
      if(this.activeImageIndex==0){
        this.activeImageIndex=1;
      }else{
        if(this.currChildImg==this.imageList.length-2){
          this.activeImageIndex=2;
        }
      }
      this.currChildImg++;
    }
  }

  // Hàm lấy ra danh sách ảnh cần hiển thị
  get visibleImages() {
    let posStart = 0;

    if(this.imageList.length>3){
      if(this.currChildImg>0 && this.currChildImg<this.imageList.length-1 ){
        posStart = this.currChildImg-1;
      }else if(this.currChildImg==this.imageList.length-1){
        posStart=this.currChildImg-2;
      }
    }
    return this.imageList.slice(posStart, posStart + this.itemsToShow);  // Trả về 3 ảnh bắt đầu từ activeImageIndex
  }

  // Hàm để đặt ảnh hiện tại khi người dùng click vào một ảnh
  setActiveImage(index: number,imageSrc: string) {
    this.openModal(imageSrc); 
    // Xét theo vị trí ban dầu
    if(this.activeImageIndex==0){
      this.currChildImg+=index
    }else if(this.activeImageIndex==1){
      if(index==0){
        this.currChildImg--;
      }else if(index==1){

      }else{
        this.currChildImg++;
      }
    }else{
      if(index==0){
        this.currChildImg-=2;
      }else if(index==1){
        this.currChildImg--;
      }else{

      }
    }

    this.activeImageIndex=index
    if(index!=1){
      // Lấy ảnh giữa khi chọn ảnh đầu ảnh cuối
      this.activeImageIndex = 1; 
      if(index==0 && this.currChildImg==0){
        // Lấy ảnh dầu
        this.activeImageIndex = 0
      }else
      if(index==2 && this.currChildImg==this.imageList.length-1){
        // Lấy ảnh cuối
        this.activeImageIndex = 2
      }
    }
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

  approvePost(){
    this.userRequestService.rejectOrApprovePost(this.postId,'approved','')
    .subscribe({
      next: (response) => {
        this.snackbarService.notifySuccessUser(response)
        this.cdr.detectChanges()
        this.router.navigate(['admin/post_pending'])
      },
      error: (response) =>{
        this.snackbarService.notifyErrorUser(response)
      }
    })
  }

  rejectPost(event: Event){
    event.preventDefault();
    this.openRejectModal()
  }

  submitReject(event:Event){
    const message = this.rejectForm.get("message")?.value?.trim() || ""; 

    if (message===""){
      this.snackbarService.notifyWarningUser("Vui lòng nhập lời nhắn");

    }else{
      this.userRequestService.rejectOrApprovePost(this.postId,'rejected',message)
      .subscribe({
        next: (response) => {
          this.snackbarService.notifySuccessUser(response)
          this.closeRejectModal();
          this.cdr.detectChanges()
          this.router.navigate(['admin/post_pending'])
        },
        error: (response) =>{
          this.snackbarService.notifyErrorUser(response)
        }
      })
    }
  }

  openRejectModal() {
    const modalElement = this.rejectModal.nativeElement as HTMLElement;
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
  
  closeRejectModal() {
    const modalElement = this.rejectModal.nativeElement as HTMLElement;
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



}
