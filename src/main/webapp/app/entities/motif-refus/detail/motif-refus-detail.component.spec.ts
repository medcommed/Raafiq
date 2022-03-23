import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MotifRefusDetailComponent } from './motif-refus-detail.component';

describe('MotifRefus Management Detail Component', () => {
  let comp: MotifRefusDetailComponent;
  let fixture: ComponentFixture<MotifRefusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MotifRefusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ motifRefus: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MotifRefusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MotifRefusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load motifRefus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.motifRefus).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
