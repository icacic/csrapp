import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITicketStatus } from 'app/shared/model/ticket-status.model';
import { TicketStatusService } from './ticket-status.service';

@Component({
  templateUrl: './ticket-status-delete-dialog.component.html',
})
export class TicketStatusDeleteDialogComponent {
  ticketStatus?: ITicketStatus;

  constructor(
    protected ticketStatusService: TicketStatusService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ticketStatusService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ticketStatusListModification');
      this.activeModal.close();
    });
  }
}
