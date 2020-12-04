import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITicket } from 'app/shared/model/ticket.model';
import { TicketService } from './ticket.service';

@Component({
  templateUrl: './ticket-delete-dialog.component.html',
})
export class TicketDeleteDialogComponent {
  ticket?: ITicket;

  constructor(protected ticketService: TicketService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ticketService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ticketListModification');
      this.activeModal.close();
    });
  }
}
