import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SaveSpaceComponent } from './save-space.component';

describe('SaveSpaceComponent', () => {
  let component: SaveSpaceComponent;
  let fixture: ComponentFixture<SaveSpaceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SaveSpaceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SaveSpaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
