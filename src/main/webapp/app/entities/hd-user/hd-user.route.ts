import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHDUser, HDUser } from 'app/shared/model/hd-user.model';
import { HDUserService } from './hd-user.service';
import { HDUserComponent } from './hd-user.component';
import { HDUserDetailComponent } from './hd-user-detail.component';
import { HDUserUpdateComponent } from './hd-user-update.component';

@Injectable({ providedIn: 'root' })
export class HDUserResolve implements Resolve<IHDUser> {
  constructor(private service: HDUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHDUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((hDUser: HttpResponse<HDUser>) => {
          if (hDUser.body) {
            return of(hDUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HDUser());
  }
}

export const hDUserRoute: Routes = [
  {
    path: '',
    component: HDUserComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.hDUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HDUserDetailComponent,
    resolve: {
      hDUser: HDUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.hDUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HDUserUpdateComponent,
    resolve: {
      hDUser: HDUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.hDUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HDUserUpdateComponent,
    resolve: {
      hDUser: HDUserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.hDUser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
