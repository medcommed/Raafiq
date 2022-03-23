import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMotifRefus, MotifRefus } from '../motif-refus.model';
import { MotifRefusService } from '../service/motif-refus.service';

@Component({
  selector: 'jhi-motif-refus-update',
  templateUrl: './motif-refus-update.component.html',
})
export class MotifRefusUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    libeleAr: [null, [Validators.maxLength(255)]],
    libeleFr: [null, [Validators.maxLength(255)]],
  });

  constructor(protected motifRefusService: MotifRefusService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ motifRefus }) => {
      this.updateForm(motifRefus);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const motifRefus = this.createFromForm();
    if (motifRefus.id !== undefined) {
      this.subscribeToSaveResponse(this.motifRefusService.update(motifRefus));
    } else {
      this.subscribeToSaveResponse(this.motifRefusService.create(motifRefus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMotifRefus>>): void {
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

  protected updateForm(motifRefus: IMotifRefus): void {
    this.editForm.patchValue({
      id: motifRefus.id,
      libeleAr: motifRefus.libeleAr,
      libeleFr: motifRefus.libeleFr,
    });
  }

  protected createFromForm(): IMotifRefus {
    return {
      ...new MotifRefus(),
      id: this.editForm.get(['id'])!.value,
      libeleAr: this.editForm.get(['libeleAr'])!.value,
      libeleFr: this.editForm.get(['libeleFr'])!.value,
    };
  }
}
