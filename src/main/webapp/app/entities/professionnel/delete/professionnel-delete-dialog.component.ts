import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProfessionnel } from '../professionnel.model';
import { ProfessionnelService } from '../service/professionnel.service';

@Component({
  templateUrl: './professionnel-delete-dialog.component.html',
})
export class ProfessionnelDeleteDialogComponent {
  professionnel?: IProfessionnel;

  constructor(protected professionnelService: ProfessionnelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.professionnelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
