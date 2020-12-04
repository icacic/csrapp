import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IHDUser } from 'app/shared/model/hd-user.model';

type EntityResponseType = HttpResponse<IHDUser>;
type EntityArrayResponseType = HttpResponse<IHDUser[]>;

@Injectable({ providedIn: 'root' })
export class HDUserService {
  public resourceUrl = SERVER_API_URL + 'api/hd-users';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/hd-users';

  constructor(protected http: HttpClient) {}

  create(hDUser: IHDUser): Observable<EntityResponseType> {
    return this.http.post<IHDUser>(this.resourceUrl, hDUser, { observe: 'response' });
  }

  update(hDUser: IHDUser): Observable<EntityResponseType> {
    return this.http.put<IHDUser>(this.resourceUrl, hDUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHDUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHDUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHDUser[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
