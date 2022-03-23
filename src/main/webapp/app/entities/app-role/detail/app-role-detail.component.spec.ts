import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppRoleDetailComponent } from './app-role-detail.component';

describe('AppRole Management Detail Component', () => {
  let comp: AppRoleDetailComponent;
  let fixture: ComponentFixture<AppRoleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AppRoleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ appRole: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AppRoleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AppRoleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load appRole on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.appRole).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
