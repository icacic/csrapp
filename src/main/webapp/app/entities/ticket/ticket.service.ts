import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { ITicket } from 'app/shared/model/ticket.model';

type EntityResponseType = HttpResponse<ITicket>;
type EntityArrayResponseType = HttpResponse<ITicket[]>;

@Injectable({ providedIn: 'root' })
export class TicketService {
  public resourceUrl = SERVER_API_URL + 'api/tickets';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/tickets';

  constructor(protected http: HttpClient) {}

  create(ticket: ITicket): Observable<EntityResponseType> {
    return this.http.post<ITicket>(this.resourceUrl, ticket, { observe: 'response' });
  }

  update(ticket: ITicket): Observable<EntityResponseType> {
    return this.http.put<ITicket>(this.resourceUrl, ticket, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITicket>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITicket[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITicket[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
