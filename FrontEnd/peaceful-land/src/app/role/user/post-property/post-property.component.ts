import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnChanges, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSelectModule } from '@angular/material/select';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MAT_DATE_LOCALE, MatOptionModule, provideNativeDateAdapter } from '@angular/material/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { User } from '../../../dto/user';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SnackBarService } from '../../../core/services/snack-bar.service';

interface Ward {
  Id: string;
  Name: string;
}

interface District {
  Id: string;
  Name: string;
  Wards: Ward[];
}

interface City {
  Id: string;
  Name: string;
  Districts: District[];
}

@Component({
  selector: 'app-post-property',
  standalone: true,
  imports: [MatSelectModule, MatFormFieldModule, MatInputModule,FormsModule, MatOptionModule,CommonModule,ReactiveFormsModule,MatProgressSpinnerModule],
  templateUrl: './post-property.component.html',
  providers: [
    provideNativeDateAdapter(),
    { provide: MAT_DATE_LOCALE, useValue: 'vi-VN' },
    {provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,useValue: { subscriptSizing: 'dynamic' },}
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./post-property.component.css']
})
export class PostPropertyComponent implements OnInit, OnChanges {
  cities: City[] = [];
  districts: District[] = [];
  user!: User;
  today: any;
  isLoading = true; 
  wards: Ward[] = [];
  selectedCity: string = '';
  selectedDistrict: string = '';
  selectedWard: string = '';
  maxExpByRole: number = 0;
  selectedFile!: File;
  imagePreview: string | ArrayBuffer | null = null;
  houseOrientations: string[] = ['Bắc', 'Đông Bắc', 'Đông', 'Đông Nam', 'Nam', 'Tây Nam', 'Tây', 'Tây Bắc'];
  balconyOrientations: string[] = ['Bắc', 'Đông Bắc', 'Đông', 'Đông Nam', 'Nam', 'Tây Nam', 'Tây', 'Tây Bắc'];
  categories: string[] = ['Nhà riêng', 'Nhà biệt thự, liền kề', 'Nhà mặt phố', 'Shophouse, nhà phố thương mại', 
                          'Chung cư mini, căn hộ dịch vụ', 'Condotel', 'Đất nền dự án', 'Bán đất', 'Trang trại, khu nghỉ dưỡng', 
                          'Kho, nhà xưởng', 'Bất động sản khác'];
  selectedHouseOrientation: string = '';
  selectedBalconyOrientation: string = '';
  selectedCategory: string = '';
  selectedOffer: number = 0;  // 0: Mua bán, 1: Cho thuê
  rentalPeriod: string = ''; 
  propertyForm!: FormGroup;



  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private authService:AuthService,
    private snackBarService:SnackBarService
  ) {}

  ngOnInit() {
    this.loadData();

    // Today
    const now = new Date(); 
    const month = ('0' + (now.getMonth() + 1)).slice(-2); 
    const day = ('0' + now.getDate()).slice(-2); 
    this.today = `${now.getFullYear()}-${month}-${day}`;
    this.loadCities();
    this.user = this.authService.getUserDetails();


    switch(this.user.role){
      case 0:
        this.maxExpByRole = 7
        break;
      case 1:
        this.maxExpByRole = 10
        break;
      case 2: 
        this.maxExpByRole = 14
    }
    // Khởi tạo form với FormGroup và FormControl
    this.propertyForm = new FormGroup({
      selectedCity: new FormControl(''),
      selectedDistrict: new FormControl(''),
      selectedWard: new FormControl(''),
      locationDetail: new FormControl(''),
      mapUrl: new FormControl(''),
      area: new FormControl(1),
      selectedCategory: new FormControl(''),
      price: new FormControl(1000000),
      bedrooms: new FormControl(1),
      toilets: new FormControl(1),
      entrance: new FormControl(1),
      frontage: new FormControl(1),
      selectedHouseOrientation: new FormControl(''),
      selectedBalconyOrientation: new FormControl(''),
      legal: new FormControl(''),
      image: new FormControl(''),
      description: new FormControl(''),
      topic: new FormControl(''),
      expiration: new FormControl(1),
      selectedOffer: new FormControl(0), // Mua bán (0) hoặc Cho thuê (1)
      rentalPeriod: new FormControl({value: '', disabled: true})  // Hạn cho thuê, mặc định trống
    });


    // Theo dõi sự thay đổi của selectedOffer để vô hiệu hóa rentalPeriod khi là Mua bán
    this.propertyForm.get('selectedOffer')?.valueChanges.subscribe(value => {
      const rentalPeriodControl = this.propertyForm.get('rentalPeriod');
      
      if (value === 0) {
        rentalPeriodControl?.setValue('');  // Nếu Mua bán, xóa giá trị rentalPeriod
        rentalPeriodControl?.disable();  // Vô hiệu hóa rentalPeriod
      } else {
        rentalPeriodControl?.enable();   // Nếu Cho thuê, kích hoạt rentalPeriod
      }
    });
  }

  loadData(): void {
    setTimeout(() => {
      this.isLoading = false; // Ẩn màn hình loading khi hoàn tất
      this.cdr.detectChanges()
    }, 2000); // Thời gian tải giả lập (2 giây)
  }

  ngOnChanges() {
    if (this.selectedOffer === 0) {
      this.rentalPeriod = ''; // Reset giá trị khi chuyển sang Mua bán
    }
  }

  loadCities() {
    const url = 'https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json';
    this.http.get<City[]>(url).subscribe(data => {
      this.cities = data;
    });
  }

  onCityChange() {
    // Reset district and ward values when city is changed
    this.propertyForm.get('selectedDistrict')?.reset();
    this.propertyForm.get('selectedWard')?.reset();
    this.districts = [];
    this.wards = [];
  
    const selectedCityId = this.propertyForm.get('selectedCity')?.value;
    if (selectedCityId) {
      const selectedCityData = this.cities.find(city => city.Id === selectedCityId);
      if (selectedCityData) {
        this.districts = selectedCityData.Districts;
      }
    }
  }
  
  onDistrictChange() {
    // Reset wards when district is changed
    this.propertyForm.get('selectedWard')?.reset();
    this.wards = [];
  
    const selectedCityId = this.propertyForm.get('selectedCity')?.value;
    const selectedDistrictId = this.propertyForm.get('selectedDistrict')?.value;
  
    if (selectedCityId && selectedDistrictId) {
      const selectedCityData = this.cities.find(city => city.Id === selectedCityId);
      if (selectedCityData) {
        const selectedDistrictData = selectedCityData.Districts.find(district => district.Id === selectedDistrictId);
        if (selectedDistrictData) {
          this.wards = selectedDistrictData.Wards;
        }
      }
    }
  }
  
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
  

  handleSubmitProperty() {
    const selectedCity = this.propertyForm.get("selectedCity")?.value?.trim()!;
    const selectedDistrict = this.propertyForm.get("selectedDistrict")?.value?.trim()!;
    const selectedWard = this.propertyForm.get("selectedWard")?.value?.trim()!;
    const location = selectedCity + " " + selectedDistrict + selectedWard;
    const locationDetail = this.propertyForm.get("locationDetail")?.value?.trim()!;
    const mapUrl = this.propertyForm.get("mapUrl")?.value?.trim()!;
    const area = this.propertyForm.get("area")?.value!;
    const selectedCategory = this.propertyForm.get("selectedCategory")?.value?.trim()!;
    const price = this.propertyForm.get("price")?.value!;
    const bedrooms = this.propertyForm.get("bedrooms")?.value!;
    const toilets = this.propertyForm.get("toilets")?.value!;
    const entrance = this.propertyForm.get("entrance")?.value!;
    const frontage = this.propertyForm.get("frontage")?.value!;
    const selectedHouseOrientation = this.propertyForm.get("selectedHouseOrientation")?.value?.trim()!;
    const selectedBalconyOrientation = this.propertyForm.get("selectedBalconyOrientation")?.value?.trim()!;
    const legal = this.propertyForm.get("legal")?.value?.trim()!;
    const image = this.propertyForm.get("image")?.value!;
    const description = this.propertyForm.get("description")?.value?.trim()!;
    const topic = this.propertyForm.get("topic")?.value?.trim()!;
    const expiration = this.propertyForm.get("expiration")?.value!;
    const selectedOffer = this.propertyForm.get("selectedOffer")?.value!;
    const rentalPeriod = this.propertyForm.get("rentalPeriod")?.value?.trim()!;

    if (selectedCity === "" || selectedCity === undefined) {
      this.snackBarService.notifyWarningUser("Vui lòng chọn thành phố!");
    } else if (selectedDistrict === "" || selectedDistrict === undefined) {
      this.snackBarService.notifyWarningUser("Vui lòng chọn quận/huyện!");
    } else if (selectedWard === "" || selectedWard === undefined) {
      this.snackBarService.notifyWarningUser("Vui lòng chọn phường/xã!");
    }  else if (locationDetail === "") {
      this.snackBarService.notifyWarningUser("Vui lòng nhập chi tiết vị trí!");
    } else if (mapUrl === "") {
      this.snackBarService.notifyWarningUser("Vui lòng chọn địa chỉ Map Google!");
    }else if (selectedCategory === "" || selectedCategory === undefined) {
      this.snackBarService.notifyWarningUser("Vui lòng chọn loại bất động sản!");
    } else if (price === "" || price <= 0) {
      this.snackBarService.notifyWarningUser("Vui lòng nhập giá hợp lệ!");
    } else if (bedrooms <= 0) {
      this.snackBarService.notifyWarningUser("Vui lòng nhập số phòng ngủ hợp lệ!");
    } else if (toilets <= 0) {
      this.snackBarService.notifyWarningUser("Vui lòng nhập số phòng tắm hợp lệ!");
    } else if (entrance <= 0) {
      this.snackBarService.notifyWarningUser("Vui lòng nhập số lối vào hợp lệ!");
    } else if (frontage <= 0) {
      this.snackBarService.notifyWarningUser("Vui lòng nhập chiều ngang hợp lệ!");
    } else if (selectedHouseOrientation === "" || selectedHouseOrientation === undefined) {
      this.snackBarService.notifyWarningUser("Vui lòng chọn hướng nhà!");
    } else if (selectedBalconyOrientation === ""|| selectedBalconyOrientation === undefined) {
      this.snackBarService.notifyWarningUser("Vui lòng chọn hướng ban công!");
    } else if (legal === "") {
      this.snackBarService.notifyWarningUser("Vui lòng nhập thông tin pháp lý!");
    } else if (this.selectedFile == undefined){
      this.snackBarService.notifyWarningUser("Vui lòng chọn ảnh đại diện bất động sản!");
    }else if (description === "") {
      this.snackBarService.notifyWarningUser("Vui lòng nhập mô tả!");
    } else if (topic === "") {
      this.snackBarService.notifyWarningUser("Vui lòng nhập chủ đề!");
    } else if (expiration === "" || expiration <= 0 || expiration > this.maxExpByRole) {
      this.snackBarService.notifyWarningUser(`Vui lòng chọn thời gian hết hạn hợp lệ! (Tối đa ${this.maxExpByRole} tháng)`);
    } else if (selectedOffer === 1 && rentalPeriod === "") {
      this.snackBarService.notifyWarningUser("Vui lòng nhập thời hạn cho thuê!");
    } else if (selectedOffer === 0 && rentalPeriod !== "") {
      // Nếu là Mua bán, reset giá trị rentalPeriod về chuỗi rỗng
      this.propertyForm.get("rentalPeriod")?.setValue('');
    } else {
      // Nếu không có lỗi, có thể tiếp tục xử lý
      this.snackBarService.notifySuccessUser("Thông tin hợp lệ, đang gửi...");
      // Thực hiện gửi dữ liệu hoặc các thao tác khác tại đây
    }
  }
}
