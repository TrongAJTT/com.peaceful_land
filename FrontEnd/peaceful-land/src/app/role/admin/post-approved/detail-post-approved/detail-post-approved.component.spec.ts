import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailPostApprovedComponent } from './detail-post-approved.component';

describe('DetailPostApprovedComponent', () => {
  let component: DetailPostApprovedComponent;
  let fixture: ComponentFixture<DetailPostApprovedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailPostApprovedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailPostApprovedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
