import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMotifRefus, getMotifRefusIdentifier } from '../motif-refus.model';

export type EntityResponseType = HttpResponse<IMotifRefus>;
export type EntityArrayResponseType = HttpResponse<IMotifRefus[]>;

@Injectable({ providedIn: 'root' })
export class MotifRefusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/motif-refuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(motifRefus: IMotifRefus): Observable<EntityResponseType> {
    return this.http.post<IMotifRefus>(this.resourceUrl, motifRefus, { observe: 'response' });
  }

  update(motifRefus: IMotifRefus): Observable<EntityResponseType> {
    return this.http.put<IMotifRefus>(`${this.resourceUrl}/${getMotifRefusIdentifier(motifRefus) as number}`, motifRefus, {
      observe: 'response',
    });
  }

  partialUpdate(motifRefus: IMotifRefus): Observable<EntityResponseType> {
    return this.http.patch<IMotifRefus>(`${this.resourceUrl}/${getMotifRefusIdentifier(motifRefus) as number}`, motifRefus, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMotifRefus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMotifRefus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMotifRefusToCollectionIfMissing(
    motifRefusCollection: IMotifRefus[],
    ...motifRefusesToCheck: (IMotifRefus | null | undefined)[]
  ): IMotifRefus[] {
    const motifRefuses: IMotifRefus[] = motifRefusesToCheck.filter(isPresent);
    if (motifRefuses.length > 0) {
      const motifRefusCollectionIdentifiers = motifRefusCollection.map(motifRefusItem => getMotifRefusIdentifier(motifRefusItem)!);
      const motifRefusesToAdd = motifRefuses.filter(motifRefusItem => {
        const motifRefusIdentifier = getMotifRefusIdentifier(motifRefusItem);
        if (motifRefusIdentifier == null || motifRefusCollectionIdentifiers.includes(motifRefusIdentifier)) {
          return false;
        }
        motifRefusCollectionIdentifiers.push(motifRefusIdentifier);
        return true;
      });
      return [...motifRefusesToAdd, ...motifRefusCollection];
    }
    return motifRefusCollection;
  }
}
