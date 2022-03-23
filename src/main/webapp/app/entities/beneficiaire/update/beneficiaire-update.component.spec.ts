import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BeneficiaireService } from '../service/beneficiaire.service';
import { IBeneficiaire, Beneficiaire } from '../beneficiaire.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IEnfant } from 'app/entities/enfant/enfant.model';
import { EnfantService } from 'app/entities/enfant/service/enfant.service';
import { IMotifRefus } from 'app/entities/motif-refus/motif-refus.model';
import { MotifRefusService } from 'app/entities/motif-refus/service/motif-refus.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

import { BeneficiaireUpdateComponent } from './beneficiaire-update.component';

describe('Beneficiaire Management Update Component', () => {
  let comp: BeneficiaireUpdateComponent;
  let fixture: ComponentFixture<BeneficiaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let beneficiaireService: BeneficiaireService;
  let appUserService: AppUserService;
  let enfantService: EnfantService;
  let motifRefusService: MotifRefusService;
  let provinceService: ProvinceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BeneficiaireUpdateComponent],
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
      .overrideTemplate(BeneficiaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BeneficiaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    beneficiaireService = TestBed.inject(BeneficiaireService);
    appUserService = TestBed.inject(AppUserService);
    enfantService = TestBed.inject(EnfantService);
    motifRefusService = TestBed.inject(MotifRefusService);
    provinceService = TestBed.inject(ProvinceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppUser query and add missing value', () => {
      const beneficiaire: IBeneficiaire = { id: 456 };
      const appUser: IAppUser = { id: 73722 };
      beneficiaire.appUser = appUser;

      const appUserCollection: IAppUser[] = [{ id: 9789 }];
      jest.spyOn(appUserService, 'query').mockReturnValue(of(new HttpResponse({ body: appUserCollection })));
      const additionalAppUsers = [appUser];
      const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
      jest.spyOn(appUserService, 'addAppUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ beneficiaire });
      comp.ngOnInit();

      expect(appUserService.query).toHaveBeenCalled();
      expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(appUserCollection, ...additionalAppUsers);
      expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Enfant query and add missing value', () => {
      const beneficiaire: IBeneficiaire = { id: 456 };
      const enfant: IEnfant = { id: 54524 };
      beneficiaire.enfant = enfant;

      const enfantCollection: IEnfant[] = [{ id: 56925 }];
      jest.spyOn(enfantService, 'query').mockReturnValue(of(new HttpResponse({ body: enfantCollection })));
      const additionalEnfants = [enfant];
      const expectedCollection: IEnfant[] = [...additionalEnfants, ...enfantCollection];
      jest.spyOn(enfantService, 'addEnfantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ beneficiaire });
      comp.ngOnInit();

      expect(enfantService.query).toHaveBeenCalled();
      expect(enfantService.addEnfantToCollectionIfMissing).toHaveBeenCalledWith(enfantCollection, ...additionalEnfants);
      expect(comp.enfantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MotifRefus query and add missing value', () => {
      const beneficiaire: IBeneficiaire = { id: 456 };
      const motifRefus: IMotifRefus = { id: 13435 };
      beneficiaire.motifRefus = motifRefus;

      const motifRefusCollection: IMotifRefus[] = [{ id: 48981 }];
      jest.spyOn(motifRefusService, 'query').mockReturnValue(of(new HttpResponse({ body: motifRefusCollection })));
      const additionalMotifRefuses = [motifRefus];
      const expectedCollection: IMotifRefus[] = [...additionalMotifRefuses, ...motifRefusCollection];
      jest.spyOn(motifRefusService, 'addMotifRefusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ beneficiaire });
      comp.ngOnInit();

      expect(motifRefusService.query).toHaveBeenCalled();
      expect(motifRefusService.addMotifRefusToCollectionIfMissing).toHaveBeenCalledWith(motifRefusCollection, ...additionalMotifRefuses);
      expect(comp.motifRefusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Province query and add missing value', () => {
      const beneficiaire: IBeneficiaire = { id: 456 };
      const province: IProvince = { id: 27091 };
      beneficiaire.province = province;

      const provinceCollection: IProvince[] = [{ id: 11661 }];
      jest.spyOn(provinceService, 'query').mockReturnValue(of(new HttpResponse({ body: provinceCollection })));
      const additionalProvinces = [province];
      const expectedCollection: IProvince[] = [...additionalProvinces, ...provinceCollection];
      jest.spyOn(provinceService, 'addProvinceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ beneficiaire });
      comp.ngOnInit();

      expect(provinceService.query).toHaveBeenCalled();
      expect(provinceService.addProvinceToCollectionIfMissing).toHaveBeenCalledWith(provinceCollection, ...additionalProvinces);
      expect(comp.provincesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const beneficiaire: IBeneficiaire = { id: 456 };
      const appUser: IAppUser = { id: 52804 };
      beneficiaire.appUser = appUser;
      const enfant: IEnfant = { id: 35932 };
      beneficiaire.enfant = enfant;
      const motifRefus: IMotifRefus = { id: 3563 };
      beneficiaire.motifRefus = motifRefus;
      const province: IProvince = { id: 8029 };
      beneficiaire.province = province;

      activatedRoute.data = of({ beneficiaire });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(beneficiaire));
      expect(comp.appUsersSharedCollection).toContain(appUser);
      expect(comp.enfantsSharedCollection).toContain(enfant);
      expect(comp.motifRefusesSharedCollection).toContain(motifRefus);
      expect(comp.provincesSharedCollection).toContain(province);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Beneficiaire>>();
      const beneficiaire = { id: 123 };
      jest.spyOn(beneficiaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beneficiaire }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(beneficiaireService.update).toHaveBeenCalledWith(beneficiaire);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Beneficiaire>>();
      const beneficiaire = new Beneficiaire();
      jest.spyOn(beneficiaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beneficiaire }));
      saveSubject.complete();

      // THEN
      expect(beneficiaireService.create).toHaveBeenCalledWith(beneficiaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Beneficiaire>>();
      const beneficiaire = { id: 123 };
      jest.spyOn(beneficiaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(beneficiaireService.update).toHaveBeenCalledWith(beneficiaire);
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
