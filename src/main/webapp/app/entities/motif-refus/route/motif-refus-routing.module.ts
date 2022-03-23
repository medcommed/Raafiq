import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MotifRefusComponent } from '../list/motif-refus.component';
import { MotifRefusDetailComponent } from '../detail/motif-refus-detail.component';
import { MotifRefusUpdateComponent } from '../update/motif-refus-update.component';
import { MotifRefusRoutingResolveService } from './motif-refus-routing-resolve.service';

const motifRefusRoute: Routes = [
  {
    path: '',
    component: MotifRefusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MotifRefusDetailComponent,
    resolve: {
      motifRefus: MotifRefusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MotifRefusUpdateComponent,
    resolve: {
      motifRefus: MotifRefusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MotifRefusUpdateComponent,
    resolve: {
      motifRefus: MotifRefusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(motifRefusRoute)],
  exports: [RouterModule],
})
export class MotifRefusRoutingModule {}
