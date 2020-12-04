import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITicket, Ticket } from 'app/shared/model/ticket.model';
import { TicketService } from './ticket.service';
import { TicketComponent } from './ticket.component';
import { TicketDetailComponent } from './ticket-detail.component';
import { TicketUpdateComponent } from './ticket-update.component';

@Injectable({ providedIn: 'root' })
export class TicketResolve implements Resolve<ITicket> {
  constructor(private service: TicketService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITicket> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((ticket: HttpResponse<Ticket>) => {
          if (ticket.body) {
            return of(ticket.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ticket());
  }
}

export const ticketRoute: Routes = [
  {
    path: '',
    component: TicketComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.ticket.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TicketDetailComponent,
    resolve: {
      ticket: TicketResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.ticket.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TicketUpdateComponent,
    resolve: {
      ticket: TicketResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.ticket.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TicketUpdateComponent,
    resolve: {
      ticket: TicketResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'csrappApp.ticket.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
