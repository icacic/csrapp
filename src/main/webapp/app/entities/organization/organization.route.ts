import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IOrganization, Organization } from 'app/shared/model/organization.model';
import { OrganizationService } from './organization.service';
import { OrganizationComponent } from './organization.component';
import { OrganizationDetailComponent } from './organization-detail.component';
import { OrganizationUpdateComponent } from './organization-update.component';

@Injectable({ providedIn: 'root' })
export class OrganizationResolve implements Resolve<IOrganization> {
  constructor(private service: OrganizationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrganization> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((organization: HttpResponse<Organization>) => {
          if (organization.body) {
            return of(organization.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Organization());
  }
}

export const organizationRoute: Routes = [
  {
    path: '',
    component: OrganizationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.organization.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationDetailComponent,
    resolve: {
      organization: OrganizationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.organization.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationUpdateComponent,
    resolve: {
      organization: OrganizationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.organization.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationUpdateComponent,
    resolve: {
      organization: OrganizationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.organization.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
