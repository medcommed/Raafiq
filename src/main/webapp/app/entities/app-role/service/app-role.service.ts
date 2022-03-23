import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppRole, getAppRoleIdentifier } from '../app-role.model';

export type EntityResponseType = HttpResponse<IAppRole>;
export type EntityArrayResponseType = HttpResponse<IAppRole[]>;

@Injectable({ providedIn: 'root' })
export class AppRoleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app-roles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(appRole: IAppRole): Observable<EntityResponseType> {
    return this.http.post<IAppRole>(this.resourceUrl, appRole, { observe: 'response' });
  }

  update(appRole: IAppRole): Observable<EntityResponseType> {
    return this.http.put<IAppRole>(`${this.resourceUrl}/${getAppRoleIdentifier(appRole) as number}`, appRole, { observe: 'response' });
  }

  partialUpdate(appRole: IAppRole): Observable<EntityResponseType> {
    return this.http.patch<IAppRole>(`${this.resourceUrl}/${getAppRoleIdentifier(appRole) as number}`, appRole, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAppRole>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAppRoleToCollectionIfMissing(appRoleCollection: IAppRole[], ...appRolesToCheck: (IAppRole | null | undefined)[]): IAppRole[] {
    const appRoles: IAppRole[] = appRolesToCheck.filter(isPresent);
    if (appRoles.length > 0) {
      const appRoleCollectionIdentifiers = appRoleCollection.map(appRoleItem => getAppRoleIdentifier(appRoleItem)!);
      const appRolesToAdd = appRoles.filter(appRoleItem => {
        const appRoleIdentifier = getAppRoleIdentifier(appRoleItem);
        if (appRoleIdentifier == null || appRoleCollectionIdentifiers.includes(appRoleIdentifier)) {
          return false;
        }
        appRoleCollectionIdentifiers.push(appRoleIdentifier);
        return true;
      });
      return [...appRolesToAdd, ...appRoleCollection];
    }
    return appRoleCollection;
  }
}
