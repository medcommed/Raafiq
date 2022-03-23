import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProfessionnel, getProfessionnelIdentifier } from '../professionnel.model';

export type EntityResponseType = HttpResponse<IProfessionnel>;
export type EntityArrayResponseType = HttpResponse<IProfessionnel[]>;

@Injectable({ providedIn: 'root' })
export class ProfessionnelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/professionnels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(professionnel: IProfessionnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(professionnel);
    return this.http
      .post<IProfessionnel>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(professionnel: IProfessionnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(professionnel);
    return this.http
      .put<IProfessionnel>(`${this.resourceUrl}/${getProfessionnelIdentifier(professionnel) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(professionnel: IProfessionnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(professionnel);
    return this.http
      .patch<IProfessionnel>(`${this.resourceUrl}/${getProfessionnelIdentifier(professionnel) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProfessionnel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProfessionnel[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProfessionnelToCollectionIfMissing(
    professionnelCollection: IProfessionnel[],
    ...professionnelsToCheck: (IProfessionnel | null | undefined)[]
  ): IProfessionnel[] {
    const professionnels: IProfessionnel[] = professionnelsToCheck.filter(isPresent);
    if (professionnels.length > 0) {
      const professionnelCollectionIdentifiers = professionnelCollection.map(
        professionnelItem => getProfessionnelIdentifier(professionnelItem)!
      );
      const professionnelsToAdd = professionnels.filter(professionnelItem => {
        const professionnelIdentifier = getProfessionnelIdentifier(professionnelItem);
        if (professionnelIdentifier == null || professionnelCollectionIdentifiers.includes(professionnelIdentifier)) {
          return false;
        }
        professionnelCollectionIdentifiers.push(professionnelIdentifier);
        return true;
      });
      return [...professionnelsToAdd, ...professionnelCollection];
    }
    return professionnelCollection;
  }

  protected convertDateFromClient(professionnel: IProfessionnel): IProfessionnel {
    return Object.assign({}, professionnel, {
      dateCreation: professionnel.dateCreation?.isValid() ? professionnel.dateCreation.toJSON() : undefined,
      dateModification: professionnel.dateModification?.isValid() ? professionnel.dateModification.toJSON() : undefined,
      dateNaissance: professionnel.dateNaissance?.isValid() ? professionnel.dateNaissance.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((professionnel: IProfessionnel) => {
        professionnel.dateCreation = professionnel.dateCreation ? dayjs(professionnel.dateCreation) : undefined;
        professionnel.dateModification = professionnel.dateModification ? dayjs(professionnel.dateModification) : undefined;
        professionnel.dateNaissance = professionnel.dateNaissance ? dayjs(professionnel.dateNaissance) : undefined;
      });
    }
    return res;
  }
}
