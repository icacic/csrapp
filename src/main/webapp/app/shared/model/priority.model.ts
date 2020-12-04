export interface IPriority {
  id?: number;
  name?: string;
}

export class Priority implements IPriority {
  constructor(public id?: number, public name?: string) {}
}
