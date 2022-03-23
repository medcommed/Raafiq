import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppRole } from '../app-role.model';
import { AppRoleService } from '../service/app-role.service';

@Component({
  templateUrl: './app-role-delete-dialog.component.html',
})
export class AppRoleDeleteDialogComponent {
  appRole?: IAppRole;

  constructor(protected appRoleService: AppRoleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appRoleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
