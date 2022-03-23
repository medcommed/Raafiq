import { IRegion } from 'app/entities/region/region.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IBeneficiaire } from 'app/entities/beneficiaire/beneficiaire.model';
import { IFamille } from 'app/entities/famille/famille.model';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';

export interface IProvince {
  id?: number;
  libeleAr?: string | null;
  libeleFr?: string | null;
  region?: IRegion | null;
  appUsers?: IAppUser[] | null;
  beneficiaires?: IBeneficiaire[] | null;
  familles?: IFamille[] | null;
  professionnels?: IProfessionnel[] | null;
}

export class Province implements IProvince {
  constructor(
    public id?: number,
    public libeleAr?: string | null,
    public libeleFr?: string | null,
    public region?: IRegion | null,
    public appUsers?: IAppUser[] | null,
    public beneficiaires?: IBeneficiaire[] | null,
    public familles?: IFamille[] | null,
    public professionnels?: IProfessionnel[] | null
  ) {}
}

export function getProvinceIdentifier(province: IProvince): number | undefined {
  return province.id;
}
