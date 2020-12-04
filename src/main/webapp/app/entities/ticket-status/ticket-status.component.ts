import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITicketStatus } from 'app/shared/model/ticket-status.model';
import { TicketStatusService } from './ticket-status.service';
import { TicketStatusDeleteDialogComponent } from './ticket-status-delete-dialog.component';

@Component({
  selector: 'jhi-ticket-status',
  templateUrl: './ticket-status.component.html',
})
export class TicketStatusComponent implements OnInit, OnDestroy {
  ticketStatuses?: ITicketStatus[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected ticketStatusService: TicketStatusService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.ticketStatusService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<ITicketStatus[]>) => (this.ticketStatuses = res.body || []));
      return;
    }

    this.ticketStatusService.query().subscribe((res: HttpResponse<ITicketStatus[]>) => (this.ticketStatuses = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTicketStatuses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITicketStatus): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTicketStatuses(): void {
    this.eventSubscriber = this.eventManager.subscribe('ticketStatusListModification', () => this.loadAll());
  }

  delete(ticketStatus: ITicketStatus): void {
    const modalRef = this.modalService.open(TicketStatusDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ticketStatus = ticketStatus;
  }
}
