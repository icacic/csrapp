import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IPriority } from 'app/shared/model/priority.model';

type EntityResponseType = HttpResponse<IPriority>;
type EntityArrayResponseType = HttpResponse<IPriority[]>;

@Injectable({ providedIn: 'root' })
export class PriorityService {
  public resourceUrl = SERVER_API_URL + 'api/priorities';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/priorities';

  constructor(protected http: HttpClient) {}

  create(priority: IPriority): Observable<EntityResponseType> {
    return this.http.post<IPriority>(this.resourceUrl, priority, { observe: 'response' });
  }

  update(priority: IPriority): Observable<EntityResponseType> {
    return this.http.put<IPriority>(this.resourceUrl, priority, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPriority>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPriority[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPriority[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
