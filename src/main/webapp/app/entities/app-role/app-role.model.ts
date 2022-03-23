import { IAppUser } from 'app/entities/app-user/app-user.model';

export interface IAppRole {
  id?: number;
  libeleAr?: string | null;
  libeleFr?: string | null;
  appUsers?: IAppUser[] | null;
}

export class AppRole implements IAppRole {
  constructor(public id?: number, public libeleAr?: string | null, public libeleFr?: string | null, public appUsers?: IAppUser[] | null) {}
}

export function getAppRoleIdentifier(appRole: IAppRole): number | undefined {
  return appRole.id;
}
