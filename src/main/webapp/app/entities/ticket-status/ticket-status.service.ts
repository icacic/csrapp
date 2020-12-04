import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { ITicketStatus } from 'app/shared/model/ticket-status.model';

type EntityResponseType = HttpResponse<ITicketStatus>;
type EntityArrayResponseType = HttpResponse<ITicketStatus[]>;

@Injectable({ providedIn: 'root' })
export class TicketStatusService {
  public resourceUrl = SERVER_API_URL + 'api/ticket-statuses';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/ticket-statuses';

  constructor(protected http: HttpClient) {}

  create(ticketStatus: ITicketStatus): Observable<EntityResponseType> {
    return this.http.post<ITicketStatus>(this.resourceUrl, ticketStatus, { observe: 'response' });
  }

  update(ticketStatus: ITicketStatus): Observable<EntityResponseType> {
    return this.http.put<ITicketStatus>(this.resourceUrl, ticketStatus, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITicketStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITicketStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITicketStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
