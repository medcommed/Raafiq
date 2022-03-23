import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBeneficiaire, getBeneficiaireIdentifier } from '../beneficiaire.model';

export type EntityResponseType = HttpResponse<IBeneficiaire>;
export type EntityArrayResponseType = HttpResponse<IBeneficiaire[]>;

@Injectable({ providedIn: 'root' })
export class BeneficiaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/beneficiaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(beneficiaire: IBeneficiaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(beneficiaire);
    return this.http
      .post<IBeneficiaire>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(beneficiaire: IBeneficiaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(beneficiaire);
    return this.http
      .put<IBeneficiaire>(`${this.resourceUrl}/${getBeneficiaireIdentifier(beneficiaire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(beneficiaire: IBeneficiaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(beneficiaire);
    return this.http
      .patch<IBeneficiaire>(`${this.resourceUrl}/${getBeneficiaireIdentifier(beneficiaire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBeneficiaire>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBeneficiaire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBeneficiaireToCollectionIfMissing(
    beneficiaireCollection: IBeneficiaire[],
    ...beneficiairesToCheck: (IBeneficiaire | null | undefined)[]
  ): IBeneficiaire[] {
    const beneficiaires: IBeneficiaire[] = beneficiairesToCheck.filter(isPresent);
    if (beneficiaires.length > 0) {
      const beneficiaireCollectionIdentifiers = beneficiaireCollection.map(
        beneficiaireItem => getBeneficiaireIdentifier(beneficiaireItem)!
      );
      const beneficiairesToAdd = beneficiaires.filter(beneficiaireItem => {
        const beneficiaireIdentifier = getBeneficiaireIdentifier(beneficiaireItem);
        if (beneficiaireIdentifier == null || beneficiaireCollectionIdentifiers.includes(beneficiaireIdentifier)) {
          return false;
        }
        beneficiaireCollectionIdentifiers.push(beneficiaireIdentifier);
        return true;
      });
      return [...beneficiairesToAdd, ...beneficiaireCollection];
    }
    return beneficiaireCollection;
  }

  protected convertDateFromClient(beneficiaire: IBeneficiaire): IBeneficiaire {
    return Object.assign({}, beneficiaire, {
      dateCreation: beneficiaire.dateCreation?.isValid() ? beneficiaire.dateCreation.toJSON() : undefined,
      dateModification: beneficiaire.dateModification?.isValid() ? beneficiaire.dateModification.toJSON() : undefined,
      dateNaissance: beneficiaire.dateNaissance?.isValid() ? beneficiaire.dateNaissance.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((beneficiaire: IBeneficiaire) => {
        beneficiaire.dateCreation = beneficiaire.dateCreation ? dayjs(beneficiaire.dateCreation) : undefined;
        beneficiaire.dateModification = beneficiaire.dateModification ? dayjs(beneficiaire.dateModification) : undefined;
        beneficiaire.dateNaissance = beneficiaire.dateNaissance ? dayjs(beneficiaire.dateNaissance) : undefined;
      });
    }
    return res;
  }
}
