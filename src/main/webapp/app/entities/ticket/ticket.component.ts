import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITicket } from 'app/shared/model/ticket.model';
import { TicketService } from './ticket.service';
import { TicketDeleteDialogComponent } from './ticket-delete-dialog.component';

@Component({
  selector: 'jhi-ticket',
  templateUrl: './ticket.component.html',
})
export class TicketComponent implements OnInit, OnDestroy {
  tickets?: ITicket[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected ticketService: TicketService,
    protected dataUtils: JhiDataUtils,
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
      this.ticketService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<ITicket[]>) => (this.tickets = res.body || []));
      return;
    }

    this.ticketService.query().subscribe((res: HttpResponse<ITicket[]>) => (this.tickets = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTickets();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITicket): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInTickets(): void {
    this.eventSubscriber = this.eventManager.subscribe('ticketListModification', () => this.loadAll());
  }

  delete(ticket: ITicket): void {
    const modalRef = this.modalService.open(TicketDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ticket = ticket;
  }
}
