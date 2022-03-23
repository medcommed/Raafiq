import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBeneficiaire, Beneficiaire } from '../beneficiaire.model';

import { BeneficiaireService } from './beneficiaire.service';

describe('Beneficiaire Service', () => {
  let service: BeneficiaireService;
  let httpMock: HttpTestingController;
  let elemDefault: IBeneficiaire;
  let expectedResult: IBeneficiaire | IBeneficiaire[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BeneficiaireService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      typeBeneficiare: 'AAAAAAA',
      id: 0,
      adresse: 'AAAAAAA',
      benef2019: 0,
      benef2020: 0,
      cin: 'AAAAAAA',
      dateCreation: currentDate,
      dateModification: currentDate,
      dateNaissance: currentDate,
      email: 'AAAAAAA',
      etat: 0,
      explicationRefus: 'AAAAAAA',
      nbrEnfants: 0,
      niveauScolarite: 0,
      nom: 'AAAAAAA',
      nomFr: 'AAAAAAA',
      numeroDossier: 'AAAAAAA',
      prenom: 'AAAAAAA',
      prenomFr: 'AAAAAAA',
      profession: 'AAAAAAA',
      selectionner: 0,
      sexe: 0,
      telephone: 'AAAAAAA',
      autreBenef2019: 0,
      autreBenef2020: 0,
      relationFamiliale: 0,
      lieuTravailProfessionnel: 'AAAAAAA',
      specialiteProfessionnel: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateModification: currentDate.format(DATE_TIME_FORMAT),
          dateNaissance: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Beneficiaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateModification: currentDate.format(DATE_TIME_FORMAT),
          dateNaissance: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateModification: currentDate,
          dateNaissance: currentDate,
        },
        returnedFromService
      );

      service.create(new Beneficiaire()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Beneficiaire', () => {
      const returnedFromService = Object.assign(
        {
          typeBeneficiare: 'BBBBBB',
          id: 1,
          adresse: 'BBBBBB',
          benef2019: 1,
          benef2020: 1,
          cin: 'BBBBBB',
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateModification: currentDate.format(DATE_TIME_FORMAT),
          dateNaissance: currentDate.format(DATE_FORMAT),
          email: 'BBBBBB',
          etat: 1,
          explicationRefus: 'BBBBBB',
          nbrEnfants: 1,
          niveauScolarite: 1,
          nom: 'BBBBBB',
          nomFr: 'BBBBBB',
          numeroDossier: 'BBBBBB',
          prenom: 'BBBBBB',
          prenomFr: 'BBBBBB',
          profession: 'BBBBBB',
          selectionner: 1,
          sexe: 1,
          telephone: 'BBBBBB',
          autreBenef2019: 1,
          autreBenef2020: 1,
          relationFamiliale: 1,
          lieuTravailProfessionnel: 'BBBBBB',
          specialiteProfessionnel: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateModification: currentDate,
          dateNaissance: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Beneficiaire', () => {
      const patchObject = Object.assign(
        {
          cin: 'BBBBBB',
          dateModification: currentDate.format(DATE_TIME_FORMAT),
          email: 'BBBBBB',
          etat: 1,
          explicationRefus: 'BBBBBB',
          nbrEnfants: 1,
          nomFr: 'BBBBBB',
          numeroDossier: 'BBBBBB',
          prenom: 'BBBBBB',
          prenomFr: 'BBBBBB',
          telephone: 'BBBBBB',
          autreBenef2019: 1,
          relationFamiliale: 1,
          specialiteProfessionnel: 1,
        },
        new Beneficiaire()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateModification: currentDate,
          dateNaissance: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Beneficiaire', () => {
      const returnedFromService = Object.assign(
        {
          typeBeneficiare: 'BBBBBB',
          id: 1,
          adresse: 'BBBBBB',
          benef2019: 1,
          benef2020: 1,
          cin: 'BBBBBB',
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateModification: currentDate.format(DATE_TIME_FORMAT),
          dateNaissance: currentDate.format(DATE_FORMAT),
          email: 'BBBBBB',
          etat: 1,
          explicationRefus: 'BBBBBB',
          nbrEnfants: 1,
          niveauScolarite: 1,
          nom: 'BBBBBB',
          nomFr: 'BBBBBB',
          numeroDossier: 'BBBBBB',
          prenom: 'BBBBBB',
          prenomFr: 'BBBBBB',
          profession: 'BBBBBB',
          selectionner: 1,
          sexe: 1,
          telephone: 'BBBBBB',
          autreBenef2019: 1,
          autreBenef2020: 1,
          relationFamiliale: 1,
          lieuTravailProfessionnel: 'BBBBBB',
          specialiteProfessionnel: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateModification: currentDate,
          dateNaissance: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Beneficiaire', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBeneficiaireToCollectionIfMissing', () => {
      it('should add a Beneficiaire to an empty array', () => {
        const beneficiaire: IBeneficiaire = { id: 123 };
        expectedResult = service.addBeneficiaireToCollectionIfMissing([], beneficiaire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beneficiaire);
      });

      it('should not add a Beneficiaire to an array that contains it', () => {
        const beneficiaire: IBeneficiaire = { id: 123 };
        const beneficiaireCollection: IBeneficiaire[] = [
          {
            ...beneficiaire,
          },
          { id: 456 },
        ];
        expectedResult = service.addBeneficiaireToCollectionIfMissing(beneficiaireCollection, beneficiaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Beneficiaire to an array that doesn't contain it", () => {
        const beneficiaire: IBeneficiaire = { id: 123 };
        const beneficiaireCollection: IBeneficiaire[] = [{ id: 456 }];
        expectedResult = service.addBeneficiaireToCollectionIfMissing(beneficiaireCollection, beneficiaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beneficiaire);
      });

      it('should add only unique Beneficiaire to an array', () => {
        const beneficiaireArray: IBeneficiaire[] = [{ id: 123 }, { id: 456 }, { id: 85018 }];
        const beneficiaireCollection: IBeneficiaire[] = [{ id: 123 }];
        expectedResult = service.addBeneficiaireToCollectionIfMissing(beneficiaireCollection, ...beneficiaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const beneficiaire: IBeneficiaire = { id: 123 };
        const beneficiaire2: IBeneficiaire = { id: 456 };
        expectedResult = service.addBeneficiaireToCollectionIfMissing([], beneficiaire, beneficiaire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beneficiaire);
        expect(expectedResult).toContain(beneficiaire2);
      });

      it('should accept null and undefined values', () => {
        const beneficiaire: IBeneficiaire = { id: 123 };
        expectedResult = service.addBeneficiaireToCollectionIfMissing([], null, beneficiaire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beneficiaire);
      });

      it('should return initial array if no Beneficiaire is added', () => {
        const beneficiaireCollection: IBeneficiaire[] = [{ id: 123 }];
        expectedResult = service.addBeneficiaireToCollectionIfMissing(beneficiaireCollection, undefined, null);
        expect(expectedResult).toEqual(beneficiaireCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
