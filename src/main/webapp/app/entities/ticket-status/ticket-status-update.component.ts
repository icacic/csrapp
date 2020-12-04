import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITicketStatus, TicketStatus } from 'app/shared/model/ticket-status.model';
import { TicketStatusService } from './ticket-status.service';

@Component({
  selector: 'jhi-ticket-status-update',
  templateUrl: './ticket-status-update.component.html',
})
export class TicketStatusUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected ticketStatusService: TicketStatusService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ticketStatus }) => {
      this.updateForm(ticketStatus);
    });
  }

  updateForm(ticketStatus: ITicketStatus): void {
    this.editForm.patchValue({
      id: ticketStatus.id,
      name: ticketStatus.name,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ticketStatus = this.createFromForm();
    if (ticketStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.ticketStatusService.update(ticketStatus));
    } else {
      this.subscribeToSaveResponse(this.ticketStatusService.create(ticketStatus));
    }
  }

  private createFromForm(): ITicketStatus {
    return {
      ...new TicketStatus(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITicketStatus>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
