import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppRole, AppRole } from '../app-role.model';
import { AppRoleService } from '../service/app-role.service';

@Injectable({ providedIn: 'root' })
export class AppRoleRoutingResolveService implements Resolve<IAppRole> {
  constructor(protected service: AppRoleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppRole> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((appRole: HttpResponse<AppRole>) => {
          if (appRole.body) {
            return of(appRole.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AppRole());
  }
}
