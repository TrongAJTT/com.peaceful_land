import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailPostRejectedComponent } from './detail-post-rejected.component';

describe('DetailPostRejectedComponent', () => {
  let component: DetailPostRejectedComponent;
  let fixture: ComponentFixture<DetailPostRejectedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailPostRejectedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailPostRejectedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
