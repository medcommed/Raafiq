import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProfessionnelComponent } from './list/professionnel.component';
import { ProfessionnelDetailComponent } from './detail/professionnel-detail.component';
import { ProfessionnelUpdateComponent } from './update/professionnel-update.component';
import { ProfessionnelDeleteDialogComponent } from './delete/professionnel-delete-dialog.component';
import { ProfessionnelRoutingModule } from './route/professionnel-routing.module';

@NgModule({
  imports: [SharedModule, ProfessionnelRoutingModule],
  declarations: [ProfessionnelComponent, ProfessionnelDetailComponent, ProfessionnelUpdateComponent, ProfessionnelDeleteDialogComponent],
  entryComponents: [ProfessionnelDeleteDialogComponent],
})
export class ProfessionnelModule {}
