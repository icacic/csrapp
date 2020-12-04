import { IAttachment } from 'app/shared/model/attachment.model';
import { IHDUser } from 'app/shared/model/hd-user.model';

export interface ITicket {
  id?: number;
  rbr?: string;
  description?: any;
  attachments?: IAttachment[];
  statusName?: string;
  statusId?: number;
  categoryName?: string;
  categoryId?: number;
  priorityName?: string;
  priorityId?: number;
  users?: IHDUser[];
  projectName?: string;
  projectId?: number;
}

export class Ticket implements ITicket {
  constructor(
    public id?: number,
    public rbr?: string,
    public description?: any,
    public attachments?: IAttachment[],
    public statusName?: string,
    public statusId?: number,
    public categoryName?: string,
    public categoryId?: number,
    public priorityName?: string,
    public priorityId?: number,
    public users?: IHDUser[],
    public projectName?: string,
    public projectId?: number
  ) {}
}
