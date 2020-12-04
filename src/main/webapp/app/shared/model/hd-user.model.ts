import { IProject } from 'app/shared/model/project.model';
import { ITicket } from 'app/shared/model/ticket.model';

export interface IHDUser {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  address?: string;
  userEmail?: string;
  userId?: number;
  organizationName?: string;
  organizationId?: number;
  projects?: IProject[];
  tickets?: ITicket[];
}

export class HDUser implements IHDUser {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public address?: string,
    public userEmail?: string,
    public userId?: number,
    public organizationName?: string,
    public organizationId?: number,
    public projects?: IProject[],
    public tickets?: ITicket[]
  ) {}
}
