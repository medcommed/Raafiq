import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MotifRefusComponent } from './list/motif-refus.component';
import { MotifRefusDetailComponent } from './detail/motif-refus-detail.component';
import { MotifRefusUpdateComponent } from './update/motif-refus-update.component';
import { MotifRefusDeleteDialogComponent } from './delete/motif-refus-delete-dialog.component';
import { MotifRefusRoutingModule } from './route/motif-refus-routing.module';

@NgModule({
  imports: [SharedModule, MotifRefusRoutingModule],
  declarations: [MotifRefusComponent, MotifRefusDetailComponent, MotifRefusUpdateComponent, MotifRefusDeleteDialogComponent],
  entryComponents: [MotifRefusDeleteDialogComponent],
})
export class MotifRefusModule {}
