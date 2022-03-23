import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFamille, getFamilleIdentifier } from '../famille.model';

export type EntityResponseType = HttpResponse<IFamille>;
export type EntityArrayResponseType = HttpResponse<IFamille[]>;

@Injectable({ providedIn: 'root' })
export class FamilleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/familles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(famille: IFamille): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(famille);
    return this.http
      .post<IFamille>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(famille: IFamille): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(famille);
    return this.http
      .put<IFamille>(`${this.resourceUrl}/${getFamilleIdentifier(famille) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(famille: IFamille): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(famille);
    return this.http
      .patch<IFamille>(`${this.resourceUrl}/${getFamilleIdentifier(famille) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFamille>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFamille[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFamilleToCollectionIfMissing(familleCollection: IFamille[], ...famillesToCheck: (IFamille | null | undefined)[]): IFamille[] {
    const familles: IFamille[] = famillesToCheck.filter(isPresent);
    if (familles.length > 0) {
      const familleCollectionIdentifiers = familleCollection.map(familleItem => getFamilleIdentifier(familleItem)!);
      const famillesToAdd = familles.filter(familleItem => {
        const familleIdentifier = getFamilleIdentifier(familleItem);
        if (familleIdentifier == null || familleCollectionIdentifiers.includes(familleIdentifier)) {
          return false;
        }
        familleCollectionIdentifiers.push(familleIdentifier);
        return true;
      });
      return [...famillesToAdd, ...familleCollection];
    }
    return familleCollection;
  }

  protected convertDateFromClient(famille: IFamille): IFamille {
    return Object.assign({}, famille, {
      dateCreation: famille.dateCreation?.isValid() ? famille.dateCreation.toJSON() : undefined,
      dateModification: famille.dateModification?.isValid() ? famille.dateModification.toJSON() : undefined,
      dateNaissance: famille.dateNaissance?.isValid() ? famille.dateNaissance.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreation = res.body.dateCreation ? dayjs(res.body.dateCreation) : undefined;
      res.body.dateModification = res.body.dateModification ? dayjs(res.body.dateModification) : undefined;
      res.body.dateNaissance = res.body.dateNaissance ? dayjs(res.body.dateNaissance) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((famille: IFamille) => {
        famille.dateCreation = famille.dateCreation ? dayjs(famille.dateCreation) : undefined;
        famille.dateModification = famille.dateModification ? dayjs(famille.dateModification) : undefined;
        famille.dateNaissance = famille.dateNaissance ? dayjs(famille.dateNaissance) : undefined;
      });
    }
    return res;
  }
}
