import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { SnackBarService } from '../../../core/services/snack-bar.service';
import { PostService } from '../../../core/services/post.service';
import { AuthService } from '../../../core/services/auth.service';
import { User } from '../../../dto/user';
import { ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ImageService } from '../../../core/services/image.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PropertyService } from '../../../core/services/property.service';
declare var bootstrap: any;

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.css'
})
export class PostDetailComponent implements OnInit, AfterViewInit{
  @ViewChild('contactModal') contactModal: any; 
  @ViewChild('scheduleModal') scheduleModal: any; 

  currPost: any;
  user!: User;
  userId: number = -1;
  today: any;
  imagesListRaw!: any;
  postId!: number
  postsThumbImage: string = "";
  activeImageIndex = 0;  // Chỉ số của hình ảnh hiện tại
  currChildImg = 0;
  imageList!: any[ ];
  modalImageSrc = '';
  isModalOpen = false;  
  itemsToShow = 3;  // Số lượng ảnh hiển thị một lần
  isPermitGiveRequestScheduleOrRequest = false;
  permitMessageError = "";
  
  showAllProLog = false; 
  displayedData!: any;  
  dataProLog!: any[ ];
  
  showAllPostLog=false;
  displayedDataPost!: any;  
  dataPostLog!: any[];


  appointmentForm = new FormGroup({
    expected_date: new FormControl(''),
    expected_hour: new FormControl(0),
    name: new FormControl(''),
    phone: new FormControl(''),
    email: new FormControl(''),
    type: new FormControl(true),
    interest_level: new FormControl(true),
  })

  contactForm = new FormGroup({
    name: new FormControl(''),
    phone: new FormControl(''),
    email: new FormControl(''),
    message: new FormControl(''),
    interest_level: new FormControl(true),
  })

  constructor(
    private cdr: ChangeDetectorRef,
    private snackbarService:SnackBarService,
    private postService: PostService,
    private propertyService: PropertyService,
    private activeRoute: ActivatedRoute,
    private authService: AuthService,
    private imgService: ImageService,
    private modalService: NgbModal,
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
    const proImagesList = await this.loadProImages()
    this.changeImagesToBase64(this.imagesListRaw)
    await this.loadLogPro();
    await this.loadLogPost();
    await this.loadPermitContact();
    await this.loadInfoUserFormGroup();
    this.cdr.detectChanges();
  }

  async loadLogPro(): Promise<void>{
    try{
      this.dataProLog = await firstValueFrom(this.postService.getLogsOfPro(this.postId));
      this.displayedData = this.dataProLog.slice(0, 5); 
    }catch(e){
      this.displayedData = []
    }
  }

  onImageError(post_log: any): void {
    post_log.thumbnail_url = '/assets/img/house/house-demo.jpg';
  }

  async loadLogPost(): Promise<void> {

    try{
      this.dataPostLog = await firstValueFrom(this.postService.getLogsOfPost(this.postId));
      this.displayedDataPost = this.dataPostLog.slice(0, 5); 
      const loadImagePromises = this.dataPostLog.map((post) => 
        post.thumbnail_url = `http://localhost:8080/api/images?path=${post.thumbnail_url}`
      );
      // Wait for all images to load
      await Promise.all(loadImagePromises);
    }catch(e){
      this.displayedDataPost = []
    }
  }
  
  
  async loadInfoUserFormGroup(): Promise<void>{
    if(this.userId==-1){
      return
    }
    this.appointmentForm.get("name")?.setValue(this.user.name);
    this.appointmentForm.get("phone")?.setValue(this.user.phone);
    this.appointmentForm.get("email")?.setValue(this.user.email);

    this.contactForm.get("name")?.setValue(this.user.name);
    this.contactForm.get("phone")?.setValue(this.user.phone);
    this.contactForm.get("email")?.setValue(this.user.email);
  }

  async loadPermitContact(): Promise<void>{
    try {
      const response = await firstValueFrom(this.postService.requestPermitContact(this.userId,this.postId));
      this.isPermitGiveRequestScheduleOrRequest = true
    } catch (response:any) {
      this.isPermitGiveRequestScheduleOrRequest = false
      this.permitMessageError =  response.error?.message
    }
  }

  async loadPost() : Promise<void>{
    this.currPost = await firstValueFrom(this.postService.getPostById(this.userId,this.postId))
    this.changeToImg(this.currPost.data.thumbnUrl);
  }

  async loadProImages() : Promise<void>{
    this.imagesListRaw = await firstValueFrom(this.propertyService.getProImages(this.currPost.data.property.id))
  }

  async ngAfterViewInit(): Promise<void> {
    await this.ngOnInit();
    this.cdr.detectChanges();
  }

  async changeImagesToBase64(proImages: string[]): Promise<void> {
    const imageList: { src: string }[] = [];
  
    // Loop through each image URL in the proImages array
    for (let i = 0; i < proImages.length; i++) {
      try {
        // Assuming imgService.changeToImgBase64 returns a base64 string for each image URL
        const base64Image = await firstValueFrom(this.imgService.changeToImgBase64(proImages[i]));
        
        // Push the base64 image into the imageList array
        imageList.push({ src: base64Image });
      } catch (error) {
        console.log(error)
      }
    }
    this.imageList = imageList;
  }

  async changeToImg(thumbUrl: string): Promise<void> {
    try {
      const response = await firstValueFrom(this.imgService.changeToImgBase64(thumbUrl));
      this.postsThumbImage = response;  // Lưu ảnh vào biến
    } catch (error:any) {
      this.postsThumbImage = '/assets/img/house/house-demo.jpg';  // Đặt ảnh mặc định
    }
  }


  // Chỉnh ảnh con


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
  toggleLike(post:any,event: Event) {
    // Ngừng sự kiện click của biểu tượng trái tim để không bị bắt bởi thẻ cha
    event.stopPropagation();
    if(this.userId==-1){
      this.snackbarService.notifyWarningUser("Vui lòng đăng nhập trước khi chọn quan tâm!")
      return
    }
    if(post.interested){
      post.interested = !post.interested;
      const currInterested = post.interested ? 1: 0 
      this.postService.changeInterested(post.data.id,this.userId,currInterested,0)
        .subscribe({
          next: (response) => this.snackbarService.notifySuccessUser(response),
          error: (response) => this.snackbarService.notifyWarningUser(response.error.message)
        })
    }
  }

  chooseOptionInterested(post: any,notification: number,event: Event){
    event.stopPropagation();
    // Đảo ngược trạng thái isLiked
    post.interested = !post.interested;

    const currInterested = post.interested ? 1: 0 

    this.postService.changeInterested(post.data.id,this.userId,currInterested,notification)
      .subscribe({
        next: (response) => this.snackbarService.notifySuccessUser(response),
        error: (response) => this.snackbarService.notifyWarningUser(response.error.message)
      })
  }

  toggleScheduleOrContact(event:Event){
    event.stopPropagation();

    if(this.userId==-1){
      this.snackbarService.notifyWarningUser("Vui lòng đăng nhập trước khi thực hiện liên hệ!")
      return
    }

    if(!this.isPermitGiveRequestScheduleOrRequest){
      this.snackbarService.notifyErrorUser(this.permitMessageError)
      return
    }
  }

  submitSchedule(event:Event){
    const emailPattern= /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    const expected_date = this.appointmentForm.get("expected_date")?.value?.trim() || ""; 
    const expected_hour = this.appointmentForm.get("expected_hour")?.value || 0; 
    const name = this.appointmentForm.get("name")?.value?.trim() || ""; 
    const phone = this.appointmentForm.get("phone")?.value?.trim() || ""; 
    const email = this.appointmentForm.get("email")?.value?.trim() || ""; 
    const type = this.appointmentForm.get("type")?.value; 
    const interest_level = this.appointmentForm.get("interest_level")?.value; 
    
    if(expected_date==""){
      this.snackbarService.notifyWarningUser("Vui lòng chọn ngày hẹn");
    
    }else if (expected_hour<0 || expected_hour>23) {
      this.snackbarService.notifyWarningUser("Giờ hẹn không hợp lệ");
    
    }else if (name===""){
      this.snackbarService.notifyWarningUser("Vui lòng nhập họ tên");

    }else if (phone==="") {
      this.snackbarService.notifyWarningUser("Vui lòng nhập số điện thoại");
    
    }else if (email==="") {
      this.snackbarService.notifyWarningUser("Vui lòng nhập email");
    
    }else if(!emailPattern.test(email)){
      this.snackbarService.notifyWarningUser("Email không hợp lệ");
    
    }else{
      const typeVal = type? 1:0
      const interestVal = interest_level? 1:0
      this.postService.makeSchedule(this.userId,this.postId,typeVal,expected_date,expected_hour,
        name,phone,email,interestVal
      ).subscribe({
        next: (response) => {
          this.snackbarService.notifySuccessUser(response)
          this.closeScheduleModal()
          this.loadPermitContact();
          this.cdr.detectChanges()
        },
        error: (response) =>{
          console.log(response)
          this.snackbarService.notifyErrorUser(response.error.message)
        }
      })
    }
  }

  submitContact(event:Event){
    const emailPattern= /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    const name = this.contactForm.get("name")?.value?.trim() || ""; 
    const phone = this.contactForm.get("phone")?.value?.trim() || ""; 
    const email = this.contactForm.get("email")?.value?.trim() || ""; 
    const message = this.contactForm.get("message")?.value?.trim() || ""; 
    const interest_level = this.contactForm.get("interest_level")?.value; 

    if (name===""){
      this.snackbarService.notifyWarningUser("Vui lòng nhập họ tên");

    }else if (phone==="") {
      this.snackbarService.notifyWarningUser("Vui lòng nhập số điện thoại");
    
    }else if (email==="") {
      this.snackbarService.notifyWarningUser("Vui lòng nhập email");
    
    }else if(!emailPattern.test(email)){
      this.snackbarService.notifyWarningUser("Email không hợp lệ");
    
    }else if(message==""){
      this.snackbarService.notifyWarningUser("Vui lòng nhập lời nhắn");
    
    }else{
      const interestVal = interest_level? 1:0
      this.postService.makeContact(this.userId,this.postId,name,phone,email,interestVal,message)
        .subscribe({
        next: (response) => {
          this.snackbarService.notifySuccessUser(response)
          this.closeContactModal()
          this.loadPermitContact();
          this.cdr.detectChanges()
        },
        error: (response) =>{
          this.snackbarService.notifyErrorUser(response.error.message)
        }
      })
    }
  }

  openContactModal() {
    const modalElement = this.contactModal.nativeElement as HTMLElement;
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
  
  closeContactModal() {
    const modalElement = this.contactModal.nativeElement as HTMLElement;
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

  openScheduleModal() {
    const modalElement = this.scheduleModal.nativeElement as HTMLElement;
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
  
  closeScheduleModal() {
    const modalElement = this.scheduleModal.nativeElement as HTMLElement;
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


  // Hàm để toggle giữa xem thêm và ẩn đi
  toggleViewPro() {
    this.showAllProLog = !this.showAllProLog;  // Đảo ngược trạng thái
    this.displayedData = this.showAllProLog ? this.dataProLog : this.dataProLog.slice(0, 5);
  }

  toggleViewPost() {
    this.showAllPostLog = !this.showAllPostLog;  // Đảo ngược trạng thái
    this.displayedDataPost = this.showAllPostLog ? this.dataPostLog : this.dataPostLog.slice(0, 5);
  }

}
