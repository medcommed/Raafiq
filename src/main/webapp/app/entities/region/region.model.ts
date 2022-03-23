import { IProvince } from 'app/entities/province/province.model';

export interface IRegion {
  id?: number;
  libeleAr?: string | null;
  libeleFr?: string | null;
  provinces?: IProvince[] | null;
}

export class Region implements IRegion {
  constructor(
    public id?: number,
    public libeleAr?: string | null,
    public libeleFr?: string | null,
    public provinces?: IProvince[] | null
  ) {}
}

export function getRegionIdentifier(region: IRegion): number | undefined {
  return region.id;
}
