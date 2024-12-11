import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HandlePostPendingComponent } from './handle-post-pending.component';

describe('HandlePostPendingComponent', () => {
  let component: HandlePostPendingComponent;
  let fixture: ComponentFixture<HandlePostPendingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HandlePostPendingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HandlePostPendingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
