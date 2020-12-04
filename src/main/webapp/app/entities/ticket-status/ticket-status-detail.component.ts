import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITicketStatus } from 'app/shared/model/ticket-status.model';

@Component({
  selector: 'jhi-ticket-status-detail',
  templateUrl: './ticket-status-detail.component.html',
})
export class TicketStatusDetailComponent implements OnInit {
  ticketStatus: ITicketStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ticketStatus }) => (this.ticketStatus = ticketStatus));
  }

  previousState(): void {
    window.history.back();
  }
}
