import dayjs from 'dayjs/esm';
import { IAppRole } from 'app/entities/app-role/app-role.model';
import { IProvince } from 'app/entities/province/province.model';
import { IBeneficiaire } from 'app/entities/beneficiaire/beneficiaire.model';
import { IFamille } from 'app/entities/famille/famille.model';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';

export interface IAppUser {
  id?: number;
  active?: boolean;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  email?: string;
  entite?: string | null;
  nom?: string | null;
  password?: string;
  prenom?: string | null;
  telephone?: string | null;
  userName?: string;
  appRole?: IAppRole | null;
  province?: IProvince | null;
  beneficiaires?: IBeneficiaire[] | null;
  familles?: IFamille[] | null;
  professionnels?: IProfessionnel[] | null;
}

export class AppUser implements IAppUser {
  constructor(
    public id?: number,
    public active?: boolean,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null,
    public email?: string,
    public entite?: string | null,
    public nom?: string | null,
    public password?: string,
    public prenom?: string | null,
    public telephone?: string | null,
    public userName?: string,
    public appRole?: IAppRole | null,
    public province?: IProvince | null,
    public beneficiaires?: IBeneficiaire[] | null,
    public familles?: IFamille[] | null,
    public professionnels?: IProfessionnel[] | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getAppUserIdentifier(appUser: IAppUser): number | undefined {
  return appUser.id;
}
