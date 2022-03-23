import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMotifRefus, MotifRefus } from '../motif-refus.model';

import { MotifRefusService } from './motif-refus.service';

describe('MotifRefus Service', () => {
  let service: MotifRefusService;
  let httpMock: HttpTestingController;
  let elemDefault: IMotifRefus;
  let expectedResult: IMotifRefus | IMotifRefus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MotifRefusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      libeleAr: 'AAAAAAA',
      libeleFr: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a MotifRefus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MotifRefus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MotifRefus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libeleAr: 'BBBBBB',
          libeleFr: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MotifRefus', () => {
      const patchObject = Object.assign(
        {
          libeleFr: 'BBBBBB',
        },
        new MotifRefus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MotifRefus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libeleAr: 'BBBBBB',
          libeleFr: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a MotifRefus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMotifRefusToCollectionIfMissing', () => {
      it('should add a MotifRefus to an empty array', () => {
        const motifRefus: IMotifRefus = { id: 123 };
        expectedResult = service.addMotifRefusToCollectionIfMissing([], motifRefus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(motifRefus);
      });

      it('should not add a MotifRefus to an array that contains it', () => {
        const motifRefus: IMotifRefus = { id: 123 };
        const motifRefusCollection: IMotifRefus[] = [
          {
            ...motifRefus,
          },
          { id: 456 },
        ];
        expectedResult = service.addMotifRefusToCollectionIfMissing(motifRefusCollection, motifRefus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MotifRefus to an array that doesn't contain it", () => {
        const motifRefus: IMotifRefus = { id: 123 };
        const motifRefusCollection: IMotifRefus[] = [{ id: 456 }];
        expectedResult = service.addMotifRefusToCollectionIfMissing(motifRefusCollection, motifRefus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(motifRefus);
      });

      it('should add only unique MotifRefus to an array', () => {
        const motifRefusArray: IMotifRefus[] = [{ id: 123 }, { id: 456 }, { id: 80028 }];
        const motifRefusCollection: IMotifRefus[] = [{ id: 123 }];
        expectedResult = service.addMotifRefusToCollectionIfMissing(motifRefusCollection, ...motifRefusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const motifRefus: IMotifRefus = { id: 123 };
        const motifRefus2: IMotifRefus = { id: 456 };
        expectedResult = service.addMotifRefusToCollectionIfMissing([], motifRefus, motifRefus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(motifRefus);
        expect(expectedResult).toContain(motifRefus2);
      });

      it('should accept null and undefined values', () => {
        const motifRefus: IMotifRefus = { id: 123 };
        expectedResult = service.addMotifRefusToCollectionIfMissing([], null, motifRefus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(motifRefus);
      });

      it('should return initial array if no MotifRefus is added', () => {
        const motifRefusCollection: IMotifRefus[] = [{ id: 123 }];
        expectedResult = service.addMotifRefusToCollectionIfMissing(motifRefusCollection, undefined, null);
        expect(expectedResult).toEqual(motifRefusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
