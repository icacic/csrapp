import { IProject } from 'app/shared/model/project.model';
import { IHDUser } from 'app/shared/model/hd-user.model';
import { OrganizationType } from 'app/shared/model/enumerations/organization-type.model';

export interface IOrganization {
  id?: number;
  name?: string;
  address?: string;
  type?: OrganizationType;
  projects?: IProject[];
  users?: IHDUser[];
}

export class Organization implements IOrganization {
  constructor(
    public id?: number,
    public name?: string,
    public address?: string,
    public type?: OrganizationType,
    public projects?: IProject[],
    public users?: IHDUser[]
  ) {}
}
