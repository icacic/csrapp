import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITicketStatus, TicketStatus } from 'app/shared/model/ticket-status.model';
import { TicketStatusService } from './ticket-status.service';
import { TicketStatusComponent } from './ticket-status.component';
import { TicketStatusDetailComponent } from './ticket-status-detail.component';
import { TicketStatusUpdateComponent } from './ticket-status-update.component';

@Injectable({ providedIn: 'root' })
export class TicketStatusResolve implements Resolve<ITicketStatus> {
  constructor(private service: TicketStatusService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITicketStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((ticketStatus: HttpResponse<TicketStatus>) => {
          if (ticketStatus.body) {
            return of(ticketStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TicketStatus());
  }
}

export const ticketStatusRoute: Routes = [
  {
    path: '',
    component: TicketStatusComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.ticketStatus.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TicketStatusDetailComponent,
    resolve: {
      ticketStatus: TicketStatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.ticketStatus.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TicketStatusUpdateComponent,
    resolve: {
      ticketStatus: TicketStatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.ticketStatus.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TicketStatusUpdateComponent,
    resolve: {
      ticketStatus: TicketStatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.ticketStatus.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
