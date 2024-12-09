import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SafePipe } from '../core/pipes/safe.pipe';



@NgModule({
  declarations: [SafePipe],
  imports: [
    CommonModule
  ],
  exports: [SafePipe],
})
export class ShareModule { }
