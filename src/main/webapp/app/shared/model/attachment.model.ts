import { Extension } from 'app/shared/model/enumerations/extension.model';

export interface IAttachment {
  id?: number;
  name?: string;
  extension?: Extension;
  fileContentType?: string;
  file?: any;
  ticketDescription?: string;
  ticketId?: number;
}

export class Attachment implements IAttachment {
  constructor(
    public id?: number,
    public name?: string,
    public extension?: Extension,
    public fileContentType?: string,
    public file?: any,
    public ticketDescription?: string,
    public ticketId?: number
  ) {}
}
