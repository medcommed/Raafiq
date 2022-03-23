import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MotifRefusService } from '../service/motif-refus.service';
import { IMotifRefus, MotifRefus } from '../motif-refus.model';

import { MotifRefusUpdateComponent } from './motif-refus-update.component';

describe('MotifRefus Management Update Component', () => {
  let comp: MotifRefusUpdateComponent;
  let fixture: ComponentFixture<MotifRefusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let motifRefusService: MotifRefusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MotifRefusUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MotifRefusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MotifRefusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    motifRefusService = TestBed.inject(MotifRefusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const motifRefus: IMotifRefus = { id: 456 };

      activatedRoute.data = of({ motifRefus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(motifRefus));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MotifRefus>>();
      const motifRefus = { id: 123 };
      jest.spyOn(motifRefusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motifRefus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: motifRefus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(motifRefusService.update).toHaveBeenCalledWith(motifRefus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MotifRefus>>();
      const motifRefus = new MotifRefus();
      jest.spyOn(motifRefusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motifRefus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: motifRefus }));
      saveSubject.complete();

      // THEN
      expect(motifRefusService.create).toHaveBeenCalledWith(motifRefus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MotifRefus>>();
      const motifRefus = { id: 123 };
      jest.spyOn(motifRefusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motifRefus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(motifRefusService.update).toHaveBeenCalledWith(motifRefus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
