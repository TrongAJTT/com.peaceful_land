import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostPendingComponent } from './post-pending.component';

describe('PostPendingComponent', () => {
  let component: PostPendingComponent;
  let fixture: ComponentFixture<PostPendingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostPendingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostPendingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
