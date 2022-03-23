import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProfessionnelDetailComponent } from './professionnel-detail.component';

describe('Professionnel Management Detail Component', () => {
  let comp: ProfessionnelDetailComponent;
  let fixture: ComponentFixture<ProfessionnelDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfessionnelDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ professionnel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProfessionnelDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProfessionnelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load professionnel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.professionnel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
