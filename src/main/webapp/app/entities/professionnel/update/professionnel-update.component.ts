import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProfessionnel, Professionnel } from '../professionnel.model';
import { ProfessionnelService } from '../service/professionnel.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IEnfant } from 'app/entities/enfant/enfant.model';
import { EnfantService } from 'app/entities/enfant/service/enfant.service';
import { IMotifRefus } from 'app/entities/motif-refus/motif-refus.model';
import { MotifRefusService } from 'app/entities/motif-refus/service/motif-refus.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

@Component({
  selector: 'jhi-professionnel-update',
  templateUrl: './professionnel-update.component.html',
})
export class ProfessionnelUpdateComponent implements OnInit {
  isSaving = false;

  appUsersSharedCollection: IAppUser[] = [];
  enfantsSharedCollection: IEnfant[] = [];
  motifRefusesSharedCollection: IMotifRefus[] = [];
  provincesSharedCollection: IProvince[] = [];

  editForm = this.fb.group({
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
    lieuTravailProfessionnel: [null, [Validators.maxLength(255)]],
    specialiteProfessionnel: [],
    appUser: [],
    enfant: [],
    motifRefus: [],
    province: [],
  });

  constructor(
    protected professionnelService: ProfessionnelService,
    protected appUserService: AppUserService,
    protected enfantService: EnfantService,
    protected motifRefusService: MotifRefusService,
    protected provinceService: ProvinceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ professionnel }) => {
      if (professionnel.id === undefined) {
        const today = dayjs().startOf('day');
        professionnel.dateCreation = today;
        professionnel.dateModification = today;
      }

      this.updateForm(professionnel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const professionnel = this.createFromForm();
    if (professionnel.id !== undefined) {
      this.subscribeToSaveResponse(this.professionnelService.update(professionnel));
    } else {
      this.subscribeToSaveResponse(this.professionnelService.create(professionnel));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfessionnel>>): void {
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

  protected updateForm(professionnel: IProfessionnel): void {
    this.editForm.patchValue({
      id: professionnel.id,
      adresse: professionnel.adresse,
      benef2019: professionnel.benef2019,
      benef2020: professionnel.benef2020,
      cin: professionnel.cin,
      dateCreation: professionnel.dateCreation ? professionnel.dateCreation.format(DATE_TIME_FORMAT) : null,
      dateModification: professionnel.dateModification ? professionnel.dateModification.format(DATE_TIME_FORMAT) : null,
      dateNaissance: professionnel.dateNaissance,
      email: professionnel.email,
      etat: professionnel.etat,
      explicationRefus: professionnel.explicationRefus,
      nbrEnfants: professionnel.nbrEnfants,
      niveauScolarite: professionnel.niveauScolarite,
      nom: professionnel.nom,
      nomFr: professionnel.nomFr,
      numeroDossier: professionnel.numeroDossier,
      prenom: professionnel.prenom,
      prenomFr: professionnel.prenomFr,
      profession: professionnel.profession,
      selectionner: professionnel.selectionner,
      sexe: professionnel.sexe,
      telephone: professionnel.telephone,
      lieuTravailProfessionnel: professionnel.lieuTravailProfessionnel,
      specialiteProfessionnel: professionnel.specialiteProfessionnel,
      appUser: professionnel.appUser,
      enfant: professionnel.enfant,
      motifRefus: professionnel.motifRefus,
      province: professionnel.province,
    });

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing(
      this.appUsersSharedCollection,
      professionnel.appUser
    );
    this.enfantsSharedCollection = this.enfantService.addEnfantToCollectionIfMissing(this.enfantsSharedCollection, professionnel.enfant);
    this.motifRefusesSharedCollection = this.motifRefusService.addMotifRefusToCollectionIfMissing(
      this.motifRefusesSharedCollection,
      professionnel.motifRefus
    );
    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing(
      this.provincesSharedCollection,
      professionnel.province
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

  protected createFromForm(): IProfessionnel {
    return {
      ...new Professionnel(),
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
      lieuTravailProfessionnel: this.editForm.get(['lieuTravailProfessionnel'])!.value,
      specialiteProfessionnel: this.editForm.get(['specialiteProfessionnel'])!.value,
      appUser: this.editForm.get(['appUser'])!.value,
      enfant: this.editForm.get(['enfant'])!.value,
      motifRefus: this.editForm.get(['motifRefus'])!.value,
      province: this.editForm.get(['province'])!.value,
    };
  }
}
