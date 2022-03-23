import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAppRole, AppRole } from '../app-role.model';
import { AppRoleService } from '../service/app-role.service';

@Component({
  selector: 'jhi-app-role-update',
  templateUrl: './app-role-update.component.html',
})
export class AppRoleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    libeleAr: [null, [Validators.maxLength(255)]],
    libeleFr: [null, [Validators.maxLength(255)]],
  });

  constructor(protected appRoleService: AppRoleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appRole }) => {
      this.updateForm(appRole);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appRole = this.createFromForm();
    if (appRole.id !== undefined) {
      this.subscribeToSaveResponse(this.appRoleService.update(appRole));
    } else {
      this.subscribeToSaveResponse(this.appRoleService.create(appRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppRole>>): void {
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

  protected updateForm(appRole: IAppRole): void {
    this.editForm.patchValue({
      id: appRole.id,
      libeleAr: appRole.libeleAr,
      libeleFr: appRole.libeleFr,
    });
  }

  protected createFromForm(): IAppRole {
    return {
      ...new AppRole(),
      id: this.editForm.get(['id'])!.value,
      libeleAr: this.editForm.get(['libeleAr'])!.value,
      libeleFr: this.editForm.get(['libeleFr'])!.value,
    };
  }
}
