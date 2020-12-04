import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHDUser } from 'app/shared/model/hd-user.model';
import { HDUserService } from './hd-user.service';

@Component({
  templateUrl: './hd-user-delete-dialog.component.html',
})
export class HDUserDeleteDialogComponent {
  hDUser?: IHDUser;

  constructor(protected hDUserService: HDUserService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hDUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('hDUserListModification');
      this.activeModal.close();
    });
  }
}
