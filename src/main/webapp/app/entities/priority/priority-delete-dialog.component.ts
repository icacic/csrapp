import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPriority } from 'app/shared/model/priority.model';
import { PriorityService } from './priority.service';

@Component({
  templateUrl: './priority-delete-dialog.component.html',
})
export class PriorityDeleteDialogComponent {
  priority?: IPriority;

  constructor(protected priorityService: PriorityService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.priorityService.delete(id).subscribe(() => {
      this.eventManager.broadcast('priorityListModification');
      this.activeModal.close();
    });
  }
}
