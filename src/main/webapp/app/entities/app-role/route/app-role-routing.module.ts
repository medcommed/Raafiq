import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppRoleComponent } from '../list/app-role.component';
import { AppRoleDetailComponent } from '../detail/app-role-detail.component';
import { AppRoleUpdateComponent } from '../update/app-role-update.component';
import { AppRoleRoutingResolveService } from './app-role-routing-resolve.service';

const appRoleRoute: Routes = [
  {
    path: '',
    component: AppRoleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppRoleDetailComponent,
    resolve: {
      appRole: AppRoleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppRoleUpdateComponent,
    resolve: {
      appRole: AppRoleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppRoleUpdateComponent,
    resolve: {
      appRole: AppRoleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(appRoleRoute)],
  exports: [RouterModule],
})
export class AppRoleRoutingModule {}
