import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProfessionnel, Professionnel } from '../professionnel.model';
import { ProfessionnelService } from '../service/professionnel.service';

@Injectable({ providedIn: 'root' })
export class ProfessionnelRoutingResolveService implements Resolve<IProfessionnel> {
  constructor(protected service: ProfessionnelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProfessionnel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((professionnel: HttpResponse<Professionnel>) => {
          if (professionnel.body) {
            return of(professionnel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Professionnel());
  }
}
