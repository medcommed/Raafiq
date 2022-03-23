import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProfessionnelComponent } from '../list/professionnel.component';
import { ProfessionnelDetailComponent } from '../detail/professionnel-detail.component';
import { ProfessionnelUpdateComponent } from '../update/professionnel-update.component';
import { ProfessionnelRoutingResolveService } from './professionnel-routing-resolve.service';

const professionnelRoute: Routes = [
  {
    path: '',
    component: ProfessionnelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProfessionnelDetailComponent,
    resolve: {
      professionnel: ProfessionnelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProfessionnelUpdateComponent,
    resolve: {
      professionnel: ProfessionnelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProfessionnelUpdateComponent,
    resolve: {
      professionnel: ProfessionnelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(professionnelRoute)],
  exports: [RouterModule],
})
export class ProfessionnelRoutingModule {}
