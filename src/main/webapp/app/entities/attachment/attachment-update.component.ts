import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IAttachment, Attachment } from 'app/shared/model/attachment.model';
import { AttachmentService } from './attachment.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ITicket } from 'app/shared/model/ticket.model';
import { TicketService } from 'app/entities/ticket/ticket.service';

@Component({
  selector: 'jhi-attachment-update',
  templateUrl: './attachment-update.component.html',
})
export class AttachmentUpdateComponent implements OnInit {
  isSaving = false;
  tickets: ITicket[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    extension: [],
    file: [],
    fileContentType: [],
    ticketId: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected attachmentService: AttachmentService,
    protected ticketService: TicketService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attachment }) => {
      this.updateForm(attachment);

      this.ticketService.query().subscribe((res: HttpResponse<ITicket[]>) => (this.tickets = res.body || []));
    });
  }

  updateForm(attachment: IAttachment): void {
    this.editForm.patchValue({
      id: attachment.id,
      name: attachment.name,
      extension: attachment.extension,
      file: attachment.file,
      fileContentType: attachment.fileContentType,
      ticketId: attachment.ticketId,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('csrappApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attachment = this.createFromForm();
    if (attachment.id !== undefined) {
      this.subscribeToSaveResponse(this.attachmentService.update(attachment));
    } else {
      this.subscribeToSaveResponse(this.attachmentService.create(attachment));
    }
  }

  private createFromForm(): IAttachment {
    return {
      ...new Attachment(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      extension: this.editForm.get(['extension'])!.value,
      fileContentType: this.editForm.get(['fileContentType'])!.value,
      file: this.editForm.get(['file'])!.value,
      ticketId: this.editForm.get(['ticketId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttachment>>): void {
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

  trackById(index: number, item: ITicket): any {
    return item.id;
  }
}
