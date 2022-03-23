import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAppUser, AppUser } from '../app-user.model';
import { AppUserService } from '../service/app-user.service';
import { IAppRole } from 'app/entities/app-role/app-role.model';
import { AppRoleService } from 'app/entities/app-role/service/app-role.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

@Component({
  selector: 'jhi-app-user-update',
  templateUrl: './app-user-update.component.html',
})
export class AppUserUpdateComponent implements OnInit {
  isSaving = false;

  appRolesSharedCollection: IAppRole[] = [];
  provincesSharedCollection: IProvince[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    active: [null, [Validators.required]],
    dateCreation: [],
    dateModification: [],
    email: [null, [Validators.required, Validators.maxLength(255)]],
    entite: [null, [Validators.maxLength(255)]],
    nom: [null, [Validators.maxLength(255)]],
    password: [null, [Validators.required, Validators.maxLength(255)]],
    prenom: [null, [Validators.maxLength(255)]],
    telephone: [null, [Validators.maxLength(255)]],
    userName: [null, [Validators.required, Validators.maxLength(255)]],
    appRole: [],
    province: [],
  });

  constructor(
    protected appUserService: AppUserService,
    protected appRoleService: AppRoleService,
    protected provinceService: ProvinceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appUser }) => {
      if (appUser.id === undefined) {
        const today = dayjs().startOf('day');
        appUser.dateCreation = today;
        appUser.dateModification = today;
      }

      this.updateForm(appUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appUser = this.createFromForm();
    if (appUser.id !== undefined) {
      this.subscribeToSaveResponse(this.appUserService.update(appUser));
    } else {
      this.subscribeToSaveResponse(this.appUserService.create(appUser));
    }
  }

  trackAppRoleById(index: number, item: IAppRole): number {
    return item.id!;
  }

  trackProvinceById(index: number, item: IProvince): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppUser>>): void {
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

  protected updateForm(appUser: IAppUser): void {
    this.editForm.patchValue({
      id: appUser.id,
      active: appUser.active,
      dateCreation: appUser.dateCreation ? appUser.dateCreation.format(DATE_TIME_FORMAT) : null,
      dateModification: appUser.dateModification ? appUser.dateModification.format(DATE_TIME_FORMAT) : null,
      email: appUser.email,
      entite: appUser.entite,
      nom: appUser.nom,
      password: appUser.password,
      prenom: appUser.prenom,
      telephone: appUser.telephone,
      userName: appUser.userName,
      appRole: appUser.appRole,
      province: appUser.province,
    });

    this.appRolesSharedCollection = this.appRoleService.addAppRoleToCollectionIfMissing(this.appRolesSharedCollection, appUser.appRole);
    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing(
      this.provincesSharedCollection,
      appUser.province
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appRoleService
      .query()
      .pipe(map((res: HttpResponse<IAppRole[]>) => res.body ?? []))
      .pipe(
        map((appRoles: IAppRole[]) => this.appRoleService.addAppRoleToCollectionIfMissing(appRoles, this.editForm.get('appRole')!.value))
      )
      .subscribe((appRoles: IAppRole[]) => (this.appRolesSharedCollection = appRoles));

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

  protected createFromForm(): IAppUser {
    return {
      ...new AppUser(),
      id: this.editForm.get(['id'])!.value,
      active: this.editForm.get(['active'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value
        ? dayjs(this.editForm.get(['dateCreation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dateModification: this.editForm.get(['dateModification'])!.value
        ? dayjs(this.editForm.get(['dateModification'])!.value, DATE_TIME_FORMAT)
        : undefined,
      email: this.editForm.get(['email'])!.value,
      entite: this.editForm.get(['entite'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      password: this.editForm.get(['password'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      appRole: this.editForm.get(['appRole'])!.value,
      province: this.editForm.get(['province'])!.value,
    };
  }
}
