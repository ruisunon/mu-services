import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SaloonComponent } from './saloon.component';

describe('SaloonComponent', () => {
  let component: SaloonComponent;
  let fixture: ComponentFixture<SaloonComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SaloonComponent]
    });
    fixture = TestBed.createComponent(SaloonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
