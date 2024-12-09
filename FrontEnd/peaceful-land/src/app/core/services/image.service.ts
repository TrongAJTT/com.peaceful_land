import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private apiUrl = 'http://localhost:8080/api/images';

  constructor(
    private http: HttpClient
  ) { }

  changeToImgBase64(nameImg: string): Observable<any>{
    return this.http.get<any>(`${this.apiUrl}/base64?path=${nameImg}`);
  }
}
