import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMotifRefus, MotifRefus } from '../motif-refus.model';
import { MotifRefusService } from '../service/motif-refus.service';

@Injectable({ providedIn: 'root' })
export class MotifRefusRoutingResolveService implements Resolve<IMotifRefus> {
  constructor(protected service: MotifRefusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMotifRefus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((motifRefus: HttpResponse<MotifRefus>) => {
          if (motifRefus.body) {
            return of(motifRefus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MotifRefus());
  }
}
