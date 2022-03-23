import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMotifRefus } from '../motif-refus.model';
import { MotifRefusService } from '../service/motif-refus.service';

@Component({
  templateUrl: './motif-refus-delete-dialog.component.html',
})
export class MotifRefusDeleteDialogComponent {
  motifRefus?: IMotifRefus;

  constructor(protected motifRefusService: MotifRefusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.motifRefusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
