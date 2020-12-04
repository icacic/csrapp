import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPriority, Priority } from 'app/shared/model/priority.model';
import { PriorityService } from './priority.service';
import { PriorityComponent } from './priority.component';
import { PriorityDetailComponent } from './priority-detail.component';
import { PriorityUpdateComponent } from './priority-update.component';

@Injectable({ providedIn: 'root' })
export class PriorityResolve implements Resolve<IPriority> {
  constructor(private service: PriorityService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPriority> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((priority: HttpResponse<Priority>) => {
          if (priority.body) {
            return of(priority.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Priority());
  }
}

export const priorityRoute: Routes = [
  {
    path: '',
    component: PriorityComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.priority.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PriorityDetailComponent,
    resolve: {
      priority: PriorityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.priority.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PriorityUpdateComponent,
    resolve: {
      priority: PriorityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.priority.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PriorityUpdateComponent,
    resolve: {
      priority: PriorityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.priority.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
