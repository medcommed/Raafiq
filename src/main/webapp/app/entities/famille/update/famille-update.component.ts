import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFamille, Famille } from '../famille.model';
import { FamilleService } from '../service/famille.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IEnfant } from 'app/entities/enfant/enfant.model';
import { EnfantService } from 'app/entities/enfant/service/enfant.service';
import { IMotifRefus } from 'app/entities/motif-refus/motif-refus.model';
import { MotifRefusService } from 'app/entities/motif-refus/service/motif-refus.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

@Component({
  selector: 'jhi-famille-update',
  templateUrl: './famille-update.component.html',
})
export class FamilleUpdateComponent implements OnInit {
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
    autreBenef2019: [],
    autreBenef2020: [],
    relationFamiliale: [],
    appUser: [],
    enfant: [],
    motifRefus: [],
    province: [],
  });

  constructor(
    protected familleService: FamilleService,
    protected appUserService: AppUserService,
    protected enfantService: EnfantService,
    protected motifRefusService: MotifRefusService,
    protected provinceService: ProvinceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ famille }) => {
      if (famille.id === undefined) {
        const today = dayjs().startOf('day');
        famille.dateCreation = today;
        famille.dateModification = today;
      }

      this.updateForm(famille);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const famille = this.createFromForm();
    if (famille.id !== undefined) {
      this.subscribeToSaveResponse(this.familleService.update(famille));
    } else {
      this.subscribeToSaveResponse(this.familleService.create(famille));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamille>>): void {
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

  protected updateForm(famille: IFamille): void {
    this.editForm.patchValue({
      id: famille.id,
      adresse: famille.adresse,
      benef2019: famille.benef2019,
      benef2020: famille.benef2020,
      cin: famille.cin,
      dateCreation: famille.dateCreation ? famille.dateCreation.format(DATE_TIME_FORMAT) : null,
      dateModification: famille.dateModification ? famille.dateModification.format(DATE_TIME_FORMAT) : null,
      dateNaissance: famille.dateNaissance,
      email: famille.email,
      etat: famille.etat,
      explicationRefus: famille.explicationRefus,
      nbrEnfants: famille.nbrEnfants,
      niveauScolarite: famille.niveauScolarite,
      nom: famille.nom,
      nomFr: famille.nomFr,
      numeroDossier: famille.numeroDossier,
      prenom: famille.prenom,
      prenomFr: famille.prenomFr,
      profession: famille.profession,
      selectionner: famille.selectionner,
      sexe: famille.sexe,
      telephone: famille.telephone,
      autreBenef2019: famille.autreBenef2019,
      autreBenef2020: famille.autreBenef2020,
      relationFamiliale: famille.relationFamiliale,
      appUser: famille.appUser,
      enfant: famille.enfant,
      motifRefus: famille.motifRefus,
      province: famille.province,
    });

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing(this.appUsersSharedCollection, famille.appUser);
    this.enfantsSharedCollection = this.enfantService.addEnfantToCollectionIfMissing(this.enfantsSharedCollection, famille.enfant);
    this.motifRefusesSharedCollection = this.motifRefusService.addMotifRefusToCollectionIfMissing(
      this.motifRefusesSharedCollection,
      famille.motifRefus
    );
    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing(
      this.provincesSharedCollection,
      famille.province
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

  protected createFromForm(): IFamille {
    return {
      ...new Famille(),
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
      appUser: this.editForm.get(['appUser'])!.value,
      enfant: this.editForm.get(['enfant'])!.value,
      motifRefus: this.editForm.get(['motifRefus'])!.value,
      province: this.editForm.get(['province'])!.value,
    };
  }
}
