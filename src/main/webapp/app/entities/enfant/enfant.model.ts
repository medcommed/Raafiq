import dayjs from 'dayjs/esm';
import { IBeneficiaire } from 'app/entities/beneficiaire/beneficiaire.model';
import { IFamille } from 'app/entities/famille/famille.model';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';

export interface IEnfant {
  id?: number;
  dateDiagnostic?: dayjs.Dayjs | null;
  dateNaissance?: dayjs.Dayjs | null;
  degreAutisme?: number | null;
  mutualiste?: number | null;
  nom?: string | null;
  nomFr?: string | null;
  nomMedecin?: string | null;
  prenom?: string | null;
  prenomfr?: string | null;
  scolariser?: number | null;
  sexe?: number | null;
  specialiteMedecin?: string | null;
  beneficiaires?: IBeneficiaire[] | null;
  familles?: IFamille[] | null;
  professionnels?: IProfessionnel[] | null;
}

export class Enfant implements IEnfant {
  constructor(
    public id?: number,
    public dateDiagnostic?: dayjs.Dayjs | null,
    public dateNaissance?: dayjs.Dayjs | null,
    public degreAutisme?: number | null,
    public mutualiste?: number | null,
    public nom?: string | null,
    public nomFr?: string | null,
    public nomMedecin?: string | null,
    public prenom?: string | null,
    public prenomfr?: string | null,
    public scolariser?: number | null,
    public sexe?: number | null,
    public specialiteMedecin?: string | null,
    public beneficiaires?: IBeneficiaire[] | null,
    public familles?: IFamille[] | null,
    public professionnels?: IProfessionnel[] | null
  ) {}
}

export function getEnfantIdentifier(enfant: IEnfant): number | undefined {
  return enfant.id;
}
