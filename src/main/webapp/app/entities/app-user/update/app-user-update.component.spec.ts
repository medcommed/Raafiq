import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AppUserService } from '../service/app-user.service';
import { IAppUser, AppUser } from '../app-user.model';
import { IAppRole } from 'app/entities/app-role/app-role.model';
import { AppRoleService } from 'app/entities/app-role/service/app-role.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

import { AppUserUpdateComponent } from './app-user-update.component';

describe('AppUser Management Update Component', () => {
  let comp: AppUserUpdateComponent;
  let fixture: ComponentFixture<AppUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appUserService: AppUserService;
  let appRoleService: AppRoleService;
  let provinceService: ProvinceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AppUserUpdateComponent],
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
      .overrideTemplate(AppUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appUserService = TestBed.inject(AppUserService);
    appRoleService = TestBed.inject(AppRoleService);
    provinceService = TestBed.inject(ProvinceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppRole query and add missing value', () => {
      const appUser: IAppUser = { id: 456 };
      const appRole: IAppRole = { id: 18242 };
      appUser.appRole = appRole;

      const appRoleCollection: IAppRole[] = [{ id: 1663 }];
      jest.spyOn(appRoleService, 'query').mockReturnValue(of(new HttpResponse({ body: appRoleCollection })));
      const additionalAppRoles = [appRole];
      const expectedCollection: IAppRole[] = [...additionalAppRoles, ...appRoleCollection];
      jest.spyOn(appRoleService, 'addAppRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      expect(appRoleService.query).toHaveBeenCalled();
      expect(appRoleService.addAppRoleToCollectionIfMissing).toHaveBeenCalledWith(appRoleCollection, ...additionalAppRoles);
      expect(comp.appRolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Province query and add missing value', () => {
      const appUser: IAppUser = { id: 456 };
      const province: IProvince = { id: 64595 };
      appUser.province = province;

      const provinceCollection: IProvince[] = [{ id: 14651 }];
      jest.spyOn(provinceService, 'query').mockReturnValue(of(new HttpResponse({ body: provinceCollection })));
      const additionalProvinces = [province];
      const expectedCollection: IProvince[] = [...additionalProvinces, ...provinceCollection];
      jest.spyOn(provinceService, 'addProvinceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      expect(provinceService.query).toHaveBeenCalled();
      expect(provinceService.addProvinceToCollectionIfMissing).toHaveBeenCalledWith(provinceCollection, ...additionalProvinces);
      expect(comp.provincesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appUser: IAppUser = { id: 456 };
      const appRole: IAppRole = { id: 85252 };
      appUser.appRole = appRole;
      const province: IProvince = { id: 84301 };
      appUser.province = province;

      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(appUser));
      expect(comp.appRolesSharedCollection).toContain(appRole);
      expect(comp.provincesSharedCollection).toContain(province);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AppUser>>();
      const appUser = { id: 123 };
      jest.spyOn(appUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appUser }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(appUserService.update).toHaveBeenCalledWith(appUser);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AppUser>>();
      const appUser = new AppUser();
      jest.spyOn(appUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appUser }));
      saveSubject.complete();

      // THEN
      expect(appUserService.create).toHaveBeenCalledWith(appUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AppUser>>();
      const appUser = { id: 123 };
      jest.spyOn(appUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appUserService.update).toHaveBeenCalledWith(appUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAppRoleById', () => {
      it('Should return tracked AppRole primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAppRoleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackProvinceById', () => {
      it('Should return tracked Province primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProvinceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
