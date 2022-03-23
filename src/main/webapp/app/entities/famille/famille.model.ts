import dayjs from 'dayjs/esm';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IEnfant } from 'app/entities/enfant/enfant.model';
import { IMotifRefus } from 'app/entities/motif-refus/motif-refus.model';
import { IProvince } from 'app/entities/province/province.model';

export interface IFamille {
  id?: number;
  adresse?: string | null;
  benef2019?: number | null;
  benef2020?: number | null;
  cin?: string;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  dateNaissance?: dayjs.Dayjs | null;
  email?: string;
  etat?: number | null;
  explicationRefus?: string | null;
  nbrEnfants?: number;
  niveauScolarite?: number | null;
  nom?: string | null;
  nomFr?: string | null;
  numeroDossier?: string;
  prenom?: string | null;
  prenomFr?: string | null;
  profession?: string | null;
  selectionner?: number | null;
  sexe?: number | null;
  telephone?: string | null;
  autreBenef2019?: number | null;
  autreBenef2020?: number | null;
  relationFamiliale?: number | null;
  appUser?: IAppUser | null;
  enfant?: IEnfant | null;
  motifRefus?: IMotifRefus | null;
  province?: IProvince | null;
}

export class Famille implements IFamille {
  constructor(
    public id?: number,
    public adresse?: string | null,
    public benef2019?: number | null,
    public benef2020?: number | null,
    public cin?: string,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null,
    public dateNaissance?: dayjs.Dayjs | null,
    public email?: string,
    public etat?: number | null,
    public explicationRefus?: string | null,
    public nbrEnfants?: number,
    public niveauScolarite?: number | null,
    public nom?: string | null,
    public nomFr?: string | null,
    public numeroDossier?: string,
    public prenom?: string | null,
    public prenomFr?: string | null,
    public profession?: string | null,
    public selectionner?: number | null,
    public sexe?: number | null,
    public telephone?: string | null,
    public autreBenef2019?: number | null,
    public autreBenef2020?: number | null,
    public relationFamiliale?: number | null,
    public appUser?: IAppUser | null,
    public enfant?: IEnfant | null,
    public motifRefus?: IMotifRefus | null,
    public province?: IProvince | null
  ) {}
}

export function getFamilleIdentifier(famille: IFamille): number | undefined {
  return famille.id;
}
