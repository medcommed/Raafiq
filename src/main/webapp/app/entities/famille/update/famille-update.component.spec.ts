import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FamilleService } from '../service/famille.service';
import { IFamille, Famille } from '../famille.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IEnfant } from 'app/entities/enfant/enfant.model';
import { EnfantService } from 'app/entities/enfant/service/enfant.service';
import { IMotifRefus } from 'app/entities/motif-refus/motif-refus.model';
import { MotifRefusService } from 'app/entities/motif-refus/service/motif-refus.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

import { FamilleUpdateComponent } from './famille-update.component';

describe('Famille Management Update Component', () => {
  let comp: FamilleUpdateComponent;
  let fixture: ComponentFixture<FamilleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let familleService: FamilleService;
  let appUserService: AppUserService;
  let enfantService: EnfantService;
  let motifRefusService: MotifRefusService;
  let provinceService: ProvinceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FamilleUpdateComponent],
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
      .overrideTemplate(FamilleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FamilleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    familleService = TestBed.inject(FamilleService);
    appUserService = TestBed.inject(AppUserService);
    enfantService = TestBed.inject(EnfantService);
    motifRefusService = TestBed.inject(MotifRefusService);
    provinceService = TestBed.inject(ProvinceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppUser query and add missing value', () => {
      const famille: IFamille = { id: 456 };
      const appUser: IAppUser = { id: 72531 };
      famille.appUser = appUser;

      const appUserCollection: IAppUser[] = [{ id: 91728 }];
      jest.spyOn(appUserService, 'query').mockReturnValue(of(new HttpResponse({ body: appUserCollection })));
      const additionalAppUsers = [appUser];
      const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
      jest.spyOn(appUserService, 'addAppUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      expect(appUserService.query).toHaveBeenCalled();
      expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(appUserCollection, ...additionalAppUsers);
      expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Enfant query and add missing value', () => {
      const famille: IFamille = { id: 456 };
      const enfant: IEnfant = { id: 72534 };
      famille.enfant = enfant;

      const enfantCollection: IEnfant[] = [{ id: 30854 }];
      jest.spyOn(enfantService, 'query').mockReturnValue(of(new HttpResponse({ body: enfantCollection })));
      const additionalEnfants = [enfant];
      const expectedCollection: IEnfant[] = [...additionalEnfants, ...enfantCollection];
      jest.spyOn(enfantService, 'addEnfantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      expect(enfantService.query).toHaveBeenCalled();
      expect(enfantService.addEnfantToCollectionIfMissing).toHaveBeenCalledWith(enfantCollection, ...additionalEnfants);
      expect(comp.enfantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MotifRefus query and add missing value', () => {
      const famille: IFamille = { id: 456 };
      const motifRefus: IMotifRefus = { id: 8997 };
      famille.motifRefus = motifRefus;

      const motifRefusCollection: IMotifRefus[] = [{ id: 70455 }];
      jest.spyOn(motifRefusService, 'query').mockReturnValue(of(new HttpResponse({ body: motifRefusCollection })));
      const additionalMotifRefuses = [motifRefus];
      const expectedCollection: IMotifRefus[] = [...additionalMotifRefuses, ...motifRefusCollection];
      jest.spyOn(motifRefusService, 'addMotifRefusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      expect(motifRefusService.query).toHaveBeenCalled();
      expect(motifRefusService.addMotifRefusToCollectionIfMissing).toHaveBeenCalledWith(motifRefusCollection, ...additionalMotifRefuses);
      expect(comp.motifRefusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Province query and add missing value', () => {
      const famille: IFamille = { id: 456 };
      const province: IProvince = { id: 39879 };
      famille.province = province;

      const provinceCollection: IProvince[] = [{ id: 83367 }];
      jest.spyOn(provinceService, 'query').mockReturnValue(of(new HttpResponse({ body: provinceCollection })));
      const additionalProvinces = [province];
      const expectedCollection: IProvince[] = [...additionalProvinces, ...provinceCollection];
      jest.spyOn(provinceService, 'addProvinceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      expect(provinceService.query).toHaveBeenCalled();
      expect(provinceService.addProvinceToCollectionIfMissing).toHaveBeenCalledWith(provinceCollection, ...additionalProvinces);
      expect(comp.provincesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const famille: IFamille = { id: 456 };
      const appUser: IAppUser = { id: 29531 };
      famille.appUser = appUser;
      const enfant: IEnfant = { id: 18178 };
      famille.enfant = enfant;
      const motifRefus: IMotifRefus = { id: 7105 };
      famille.motifRefus = motifRefus;
      const province: IProvince = { id: 36851 };
      famille.province = province;

      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(famille));
      expect(comp.appUsersSharedCollection).toContain(appUser);
      expect(comp.enfantsSharedCollection).toContain(enfant);
      expect(comp.motifRefusesSharedCollection).toContain(motifRefus);
      expect(comp.provincesSharedCollection).toContain(province);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Famille>>();
      const famille = { id: 123 };
      jest.spyOn(familleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: famille }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(familleService.update).toHaveBeenCalledWith(famille);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Famille>>();
      const famille = new Famille();
      jest.spyOn(familleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: famille }));
      saveSubject.complete();

      // THEN
      expect(familleService.create).toHaveBeenCalledWith(famille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Famille>>();
      const famille = { id: 123 };
      jest.spyOn(familleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(familleService.update).toHaveBeenCalledWith(famille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAppUserById', () => {
      it('Should return tracked AppUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAppUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEnfantById', () => {
      it('Should return tracked Enfant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEnfantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMotifRefusById', () => {
      it('Should return tracked MotifRefus primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMotifRefusById(0, entity);
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
