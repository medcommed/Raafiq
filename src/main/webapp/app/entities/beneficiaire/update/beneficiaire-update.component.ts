import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBeneficiaire, Beneficiaire } from '../beneficiaire.model';
import { BeneficiaireService } from '../service/beneficiaire.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IEnfant } from 'app/entities/enfant/enfant.model';
import { EnfantService } from 'app/entities/enfant/service/enfant.service';
import { IMotifRefus } from 'app/entities/motif-refus/motif-refus.model';
import { MotifRefusService } from 'app/entities/motif-refus/service/motif-refus.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

@Component({
  selector: 'jhi-beneficiaire-update',
  templateUrl: './beneficiaire-update.component.html',
})
export class BeneficiaireUpdateComponent implements OnInit {
  isSaving = false;

  appUsersSharedCollection: IAppUser[] = [];
  enfantsSharedCollection: IEnfant[] = [];
  motifRefusesSharedCollection: IMotifRefus[] = [];
  provincesSharedCollection: IProvince[] = [];

  editForm = this.fb.group({
    typeBeneficiare: [null, [Validators.required, Validators.maxLength(3)]],
    id: [null, [Validators.required]],
    adresse: [null, [Validators.maxLength(255)]],
    benef2019: [],
    benef2020: [],
    cin: [null, [Validators.required, Validators.maxLength(255)]],
    dateCreation: [],
    dateModification: [],
    dateNaissance: [],
    email: [null, [Validators.required, Validators.maxLength(255)]],
    etat: [],
    explicationRefus: [null, [Validators.maxLength(255)]],
    nbrEnfants: [null, [Validators.required]],
    niveauScolarite: [],
    nom: [null, [Validators.maxLength(255)]],
    nomFr: [null, [Validators.maxLength(255)]],
    numeroDossier: [null, [Validators.required, Validators.maxLength(255)]],
    prenom: [null, [Validators.maxLength(255)]],
    prenomFr: [null, [Validators.maxLength(255)]],
    profession: [null, [Validators.maxLength(255)]],
    selectionner: [],
    sexe: [],
    telephone: [null, [Validators.maxLength(255)]],
    autreBenef2019: [],
    autreBenef2020: [],
    relationFamiliale: [],
    lieuTravailProfessionnel: [null, [Validators.maxLength(255)]],
    specialiteProfessionnel: [],
    appUser: [],
    enfant: [],
    motifRefus: [],
    province: [],
  });

  constructor(
    protected beneficiaireService: BeneficiaireService,
    protected appUserService: AppUserService,
    protected enfantService: EnfantService,
    protected motifRefusService: MotifRefusService,
    protected provinceService: ProvinceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beneficiaire }) => {
      if (beneficiaire.id === undefined) {
        const today = dayjs().startOf('day');
        beneficiaire.dateCreation = today;
        beneficiaire.dateModification = today;
      }

      this.updateForm(beneficiaire);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const beneficiaire = this.createFromForm();
    if (beneficiaire.id !== undefined) {
      this.subscribeToSaveResponse(this.beneficiaireService.update(beneficiaire));
    } else {
      this.subscribeToSaveResponse(this.beneficiaireService.create(beneficiaire));
    }
  }

  trackAppUserById(index: number, item: IAppUser): number {
    return item.id!;
  }

  trackEnfantById(index: number, item: IEnfant): number {
    return item.id!;
  }

  trackMotifRefusById(index: number, item: IMotifRefus): number {
    return item.id!;
  }

  trackProvinceById(index: number, item: IProvince): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBeneficiaire>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(beneficiaire: IBeneficiaire): void {
    this.editForm.patchValue({
      typeBeneficiare: beneficiaire.typeBeneficiare,
      id: beneficiaire.id,
      adresse: beneficiaire.adresse,
      benef2019: beneficiaire.benef2019,
      benef2020: beneficiaire.benef2020,
      cin: beneficiaire.cin,
      dateCreation: beneficiaire.dateCreation ? beneficiaire.dateCreation.format(DATE_TIME_FORMAT) : null,
      dateModification: beneficiaire.dateModification ? beneficiaire.dateModification.format(DATE_TIME_FORMAT) : null,
      dateNaissance: beneficiaire.dateNaissance,
      email: beneficiaire.email,
      etat: beneficiaire.etat,
      explicationRefus: beneficiaire.explicationRefus,
      nbrEnfants: beneficiaire.nbrEnfants,
      niveauScolarite: beneficiaire.niveauScolarite,
      nom: beneficiaire.nom,
      nomFr: beneficiaire.nomFr,
      numeroDossier: beneficiaire.numeroDossier,
      prenom: beneficiaire.prenom,
      prenomFr: beneficiaire.prenomFr,
      profession: beneficiaire.profession,
      selectionner: beneficiaire.selectionner,
      sexe: beneficiaire.sexe,
      telephone: beneficiaire.telephone,
      autreBenef2019: beneficiaire.autreBenef2019,
      autreBenef2020: beneficiaire.autreBenef2020,
      relationFamiliale: beneficiaire.relationFamiliale,
      lieuTravailProfessionnel: beneficiaire.lieuTravailProfessionnel,
      specialiteProfessionnel: beneficiaire.specialiteProfessionnel,
      appUser: beneficiaire.appUser,
      enfant: beneficiaire.enfant,
      motifRefus: beneficiaire.motifRefus,
      province: beneficiaire.province,
    });

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing(
      this.appUsersSharedCollection,
      beneficiaire.appUser
    );
    this.enfantsSharedCollection = this.enfantService.addEnfantToCollectionIfMissing(this.enfantsSharedCollection, beneficiaire.enfant);
    this.motifRefusesSharedCollection = this.motifRefusService.addMotifRefusToCollectionIfMissing(
      this.motifRefusesSharedCollection,
      beneficiaire.motifRefus
    );
    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing(
      this.provincesSharedCollection,
      beneficiaire.province
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(
        map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing(appUsers, this.editForm.get('appUser')!.value))
      )
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));

    this.enfantService
      .query()
      .pipe(map((res: HttpResponse<IEnfant[]>) => res.body ?? []))
      .pipe(map((enfants: IEnfant[]) => this.enfantService.addEnfantToCollectionIfMissing(enfants, this.editForm.get('enfant')!.value)))
      .subscribe((enfants: IEnfant[]) => (this.enfantsSharedCollection = enfants));

    this.motifRefusService
      .query()
      .pipe(map((res: HttpResponse<IMotifRefus[]>) => res.body ?? []))
      .pipe(
        map((motifRefuses: IMotifRefus[]) =>
          this.motifRefusService.addMotifRefusToCollectionIfMissing(motifRefuses, this.editForm.get('motifRefus')!.value)
        )
      )
      .subscribe((motifRefuses: IMotifRefus[]) => (this.motifRefusesSharedCollection = motifRefuses));

    this.provinceService
      .query()
      .pipe(map((res: HttpResponse<IProvince[]>) => res.body ?? []))
      .pipe(
        map((provinces: IProvince[]) =>
          this.provinceService.addProvinceToCollectionIfMissing(provinces, this.editForm.get('province')!.value)
        )
      )
      .subscribe((provinces: IProvince[]) => (this.provincesSharedCollection = provinces));
  }

  protected createFromForm(): IBeneficiaire {
    return {
      ...new Beneficiaire(),
      typeBeneficiare: this.editForm.get(['typeBeneficiare'])!.value,
      id: this.editForm.get(['id'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      benef2019: this.editForm.get(['benef2019'])!.value,
      benef2020: this.editForm.get(['benef2020'])!.value,
      cin: this.editForm.get(['cin'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value
        ? dayjs(this.editForm.get(['dateCreation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dateModification: this.editForm.get(['dateModification'])!.value
        ? dayjs(this.editForm.get(['dateModification'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dateNaissance: this.editForm.get(['dateNaissance'])!.value,
      email: this.editForm.get(['email'])!.value,
      etat: this.editForm.get(['etat'])!.value,
      explicationRefus: this.editForm.get(['explicationRefus'])!.value,
      nbrEnfants: this.editForm.get(['nbrEnfants'])!.value,
      niveauScolarite: this.editForm.get(['niveauScolarite'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      numeroDossier: this.editForm.get(['numeroDossier'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      prenomFr: this.editForm.get(['prenomFr'])!.value,
      profession: this.editForm.get(['profession'])!.value,
      selectionner: this.editForm.get(['selectionner'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      autreBenef2019: this.editForm.get(['autreBenef2019'])!.value,
      autreBenef2020: this.editForm.get(['autreBenef2020'])!.value,
      relationFamiliale: this.editForm.get(['relationFamiliale'])!.value,
      lieuTravailProfessionnel: this.editForm.get(['lieuTravailProfessionnel'])!.value,
      specialiteProfessionnel: this.editForm.get(['specialiteProfessionnel'])!.value,
      appUser: this.editForm.get(['appUser'])!.value,
      enfant: this.editForm.get(['enfant'])!.value,
      motifRefus: this.editForm.get(['motifRefus'])!.value,
      province: this.editForm.get(['province'])!.value,
    };
  }
}
