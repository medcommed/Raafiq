import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'app-role',
        data: { pageTitle: 'raafiqApp.appRole.home.title' },
        loadChildren: () => import('./app-role/app-role.module').then(m => m.AppRoleModule),
      },
      {
        path: 'app-user',
        data: { pageTitle: 'raafiqApp.appUser.home.title' },
        loadChildren: () => import('./app-user/app-user.module').then(m => m.AppUserModule),
      },
      {
        path: 'beneficiaire',
        data: { pageTitle: 'raafiqApp.beneficiaire.home.title' },
        loadChildren: () => import('./beneficiaire/beneficiaire.module').then(m => m.BeneficiaireModule),
      },
      {
        path: 'enfant',
        data: { pageTitle: 'raafiqApp.enfant.home.title' },
        loadChildren: () => import('./enfant/enfant.module').then(m => m.EnfantModule),
      },
      {
        path: 'famille',
        data: { pageTitle: 'raafiqApp.famille.home.title' },
        loadChildren: () => import('./famille/famille.module').then(m => m.FamilleModule),
      },
      {
        path: 'motif-refus',
        data: { pageTitle: 'raafiqApp.motifRefus.home.title' },
        loadChildren: () => import('./motif-refus/motif-refus.module').then(m => m.MotifRefusModule),
      },
      {
        path: 'professionnel',
        data: { pageTitle: 'raafiqApp.professionnel.home.title' },
        loadChildren: () => import('./professionnel/professionnel.module').then(m => m.ProfessionnelModule),
      },
      {
        path: 'province',
        data: { pageTitle: 'raafiqApp.province.home.title' },
        loadChildren: () => import('./province/province.module').then(m => m.ProvinceModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'raafiqApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
