import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AppRoleService } from '../service/app-role.service';
import { IAppRole, AppRole } from '../app-role.model';

import { AppRoleUpdateComponent } from './app-role-update.component';

describe('AppRole Management Update Component', () => {
  let comp: AppRoleUpdateComponent;
  let fixture: ComponentFixture<AppRoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appRoleService: AppRoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AppRoleUpdateComponent],
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
      .overrideTemplate(AppRoleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppRoleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appRoleService = TestBed.inject(AppRoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const appRole: IAppRole = { id: 456 };

      activatedRoute.data = of({ appRole });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(appRole));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AppRole>>();
      const appRole = { id: 123 };
      jest.spyOn(appRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appRole }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(appRoleService.update).toHaveBeenCalledWith(appRole);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AppRole>>();
      const appRole = new AppRole();
      jest.spyOn(appRoleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appRole }));
      saveSubject.complete();

      // THEN
      expect(appRoleService.create).toHaveBeenCalledWith(appRole);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AppRole>>();
      const appRole = { id: 123 };
      jest.spyOn(appRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appRoleService.update).toHaveBeenCalledWith(appRole);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
