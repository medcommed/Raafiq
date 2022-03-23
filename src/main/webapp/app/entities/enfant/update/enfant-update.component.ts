import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEnfant, Enfant } from '../enfant.model';
import { EnfantService } from '../service/enfant.service';

@Component({
  selector: 'jhi-enfant-update',
  templateUrl: './enfant-update.component.html',
})
export class EnfantUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    dateDiagnostic: [],
    dateNaissance: [],
    degreAutisme: [],
    mutualiste: [],
    nom: [null, [Validators.maxLength(255)]],
    nomFr: [null, [Validators.maxLength(255)]],
    nomMedecin: [null, [Validators.maxLength(255)]],
    prenom: [null, [Validators.maxLength(255)]],
    prenomfr: [null, [Validators.maxLength(255)]],
    scolariser: [],
    sexe: [],
    specialiteMedecin: [null, [Validators.maxLength(255)]],
  });

  constructor(protected enfantService: EnfantService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enfant }) => {
      this.updateForm(enfant);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enfant = this.createFromForm();
    if (enfant.id !== undefined) {
      this.subscribeToSaveResponse(this.enfantService.update(enfant));
    } else {
      this.subscribeToSaveResponse(this.enfantService.create(enfant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnfant>>): void {
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

  protected updateForm(enfant: IEnfant): void {
    this.editForm.patchValue({
      id: enfant.id,
      dateDiagnostic: enfant.dateDiagnostic,
      dateNaissance: enfant.dateNaissance,
      degreAutisme: enfant.degreAutisme,
      mutualiste: enfant.mutualiste,
      nom: enfant.nom,
      nomFr: enfant.nomFr,
      nomMedecin: enfant.nomMedecin,
      prenom: enfant.prenom,
      prenomfr: enfant.prenomfr,
      scolariser: enfant.scolariser,
      sexe: enfant.sexe,
      specialiteMedecin: enfant.specialiteMedecin,
    });
  }

  protected createFromForm(): IEnfant {
    return {
      ...new Enfant(),
      id: this.editForm.get(['id'])!.value,
      dateDiagnostic: this.editForm.get(['dateDiagnostic'])!.value,
      dateNaissance: this.editForm.get(['dateNaissance'])!.value,
      degreAutisme: this.editForm.get(['degreAutisme'])!.value,
      mutualiste: this.editForm.get(['mutualiste'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomMedecin: this.editForm.get(['nomMedecin'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      prenomfr: this.editForm.get(['prenomfr'])!.value,
      scolariser: this.editForm.get(['scolariser'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      specialiteMedecin: this.editForm.get(['specialiteMedecin'])!.value,
    };
  }
}
