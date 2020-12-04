export interface ITicketStatus {
  id?: number;
  name?: string;
}

export class TicketStatus implements ITicketStatus {
  constructor(public id?: number, public name?: string) {}
}
