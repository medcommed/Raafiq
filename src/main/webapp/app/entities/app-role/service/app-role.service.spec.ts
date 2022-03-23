import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAppRole, AppRole } from '../app-role.model';

import { AppRoleService } from './app-role.service';

describe('AppRole Service', () => {
  let service: AppRoleService;
  let httpMock: HttpTestingController;
  let elemDefault: IAppRole;
  let expectedResult: IAppRole | IAppRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AppRoleService);
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

    it('should create a AppRole', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AppRole()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AppRole', () => {
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

    it('should partial update a AppRole', () => {
      const patchObject = Object.assign(
        {
          libeleFr: 'BBBBBB',
        },
        new AppRole()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AppRole', () => {
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

    it('should delete a AppRole', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAppRoleToCollectionIfMissing', () => {
      it('should add a AppRole to an empty array', () => {
        const appRole: IAppRole = { id: 123 };
        expectedResult = service.addAppRoleToCollectionIfMissing([], appRole);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appRole);
      });

      it('should not add a AppRole to an array that contains it', () => {
        const appRole: IAppRole = { id: 123 };
        const appRoleCollection: IAppRole[] = [
          {
            ...appRole,
          },
          { id: 456 },
        ];
        expectedResult = service.addAppRoleToCollectionIfMissing(appRoleCollection, appRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AppRole to an array that doesn't contain it", () => {
        const appRole: IAppRole = { id: 123 };
        const appRoleCollection: IAppRole[] = [{ id: 456 }];
        expectedResult = service.addAppRoleToCollectionIfMissing(appRoleCollection, appRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appRole);
      });

      it('should add only unique AppRole to an array', () => {
        const appRoleArray: IAppRole[] = [{ id: 123 }, { id: 456 }, { id: 27557 }];
        const appRoleCollection: IAppRole[] = [{ id: 123 }];
        expectedResult = service.addAppRoleToCollectionIfMissing(appRoleCollection, ...appRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const appRole: IAppRole = { id: 123 };
        const appRole2: IAppRole = { id: 456 };
        expectedResult = service.addAppRoleToCollectionIfMissing([], appRole, appRole2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appRole);
        expect(expectedResult).toContain(appRole2);
      });

      it('should accept null and undefined values', () => {
        const appRole: IAppRole = { id: 123 };
        expectedResult = service.addAppRoleToCollectionIfMissing([], null, appRole, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appRole);
      });

      it('should return initial array if no AppRole is added', () => {
        const appRoleCollection: IAppRole[] = [{ id: 123 }];
        expectedResult = service.addAppRoleToCollectionIfMissing(appRoleCollection, undefined, null);
        expect(expectedResult).toEqual(appRoleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
