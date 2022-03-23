import { IBeneficiaire } from 'app/entities/beneficiaire/beneficiaire.model';
import { IFamille } from 'app/entities/famille/famille.model';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';

export interface IMotifRefus {
  id?: number;
  libeleAr?: string | null;
  libeleFr?: string | null;
  beneficiaires?: IBeneficiaire[] | null;
  familles?: IFamille[] | null;
  professionnels?: IProfessionnel[] | null;
}

export class MotifRefus implements IMotifRefus {
  constructor(
    public id?: number,
    public libeleAr?: string | null,
    public libeleFr?: string | null,
    public beneficiaires?: IBeneficiaire[] | null,
    public familles?: IFamille[] | null,
    public professionnels?: IProfessionnel[] | null
  ) {}
}

export function getMotifRefusIdentifier(motifRefus: IMotifRefus): number | undefined {
  return motifRefus.id;
}
