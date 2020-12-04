import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPriority } from 'app/shared/model/priority.model';
import { PriorityService } from './priority.service';
import { PriorityDeleteDialogComponent } from './priority-delete-dialog.component';

@Component({
  selector: 'jhi-priority',
  templateUrl: './priority.component.html',
})
export class PriorityComponent implements OnInit, OnDestroy {
  priorities?: IPriority[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected priorityService: PriorityService,
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
      this.priorityService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IPriority[]>) => (this.priorities = res.body || []));
      return;
    }

    this.priorityService.query().subscribe((res: HttpResponse<IPriority[]>) => (this.priorities = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPriorities();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPriority): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPriorities(): void {
    this.eventSubscriber = this.eventManager.subscribe('priorityListModification', () => this.loadAll());
  }

  delete(priority: IPriority): void {
    const modalRef = this.modalService.open(PriorityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.priority = priority;
  }
}
