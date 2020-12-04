import { ITicket } from 'app/shared/model/ticket.model';
import { IHDUser } from 'app/shared/model/hd-user.model';
import { ProjectStatus } from 'app/shared/model/enumerations/project-status.model';

export interface IProject {
  id?: number;
  name?: string;
  status?: ProjectStatus;
  tickets?: ITicket[];
  users?: IHDUser[];
  organizationName?: string;
  organizationId?: number;
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public name?: string,
    public status?: ProjectStatus,
    public tickets?: ITicket[],
    public users?: IHDUser[],
    public organizationName?: string,
    public organizationId?: number
  ) {}
}
