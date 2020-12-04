import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttachment } from 'app/shared/model/attachment.model';
import { AttachmentService } from './attachment.service';
import { AttachmentDeleteDialogComponent } from './attachment-delete-dialog.component';

@Component({
  selector: 'jhi-attachment',
  templateUrl: './attachment.component.html',
})
export class AttachmentComponent implements OnInit, OnDestroy {
  attachments?: IAttachment[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected attachmentService: AttachmentService,
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
      this.attachmentService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IAttachment[]>) => (this.attachments = res.body || []));
      return;
    }

    this.attachmentService.query().subscribe((res: HttpResponse<IAttachment[]>) => (this.attachments = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAttachments();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAttachment): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInAttachments(): void {
    this.eventSubscriber = this.eventManager.subscribe('attachmentListModification', () => this.loadAll());
  }

  delete(attachment: IAttachment): void {
    const modalRef = this.modalService.open(AttachmentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.attachment = attachment;
  }
}
