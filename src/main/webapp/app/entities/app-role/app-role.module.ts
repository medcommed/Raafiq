import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AppRoleComponent } from './list/app-role.component';
import { AppRoleDetailComponent } from './detail/app-role-detail.component';
import { AppRoleUpdateComponent } from './update/app-role-update.component';
import { AppRoleDeleteDialogComponent } from './delete/app-role-delete-dialog.component';
import { AppRoleRoutingModule } from './route/app-role-routing.module';

@NgModule({
  imports: [SharedModule, AppRoleRoutingModule],
  declarations: [AppRoleComponent, AppRoleDetailComponent, AppRoleUpdateComponent, AppRoleDeleteDialogComponent],
  entryComponents: [AppRoleDeleteDialogComponent],
})
export class AppRoleModule {}
