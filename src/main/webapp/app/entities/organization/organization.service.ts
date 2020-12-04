import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IOrganization } from 'app/shared/model/organization.model';

type EntityResponseType = HttpResponse<IOrganization>;
type EntityArrayResponseType = HttpResponse<IOrganization[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationService {
  public resourceUrl = SERVER_API_URL + 'api/organizations';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/organizations';

  constructor(protected http: HttpClient) {}

  create(organization: IOrganization): Observable<EntityResponseType> {
    return this.http.post<IOrganization>(this.resourceUrl, organization, { observe: 'response' });
  }

  update(organization: IOrganization): Observable<EntityResponseType> {
    return this.http.put<IOrganization>(this.resourceUrl, organization, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganization>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganization[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganization[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
