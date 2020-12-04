import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHDUser } from 'app/shared/model/hd-user.model';
import { HDUserService } from './hd-user.service';
import { HDUserDeleteDialogComponent } from './hd-user-delete-dialog.component';

@Component({
  selector: 'jhi-hd-user',
  templateUrl: './hd-user.component.html',
})
export class HDUserComponent implements OnInit, OnDestroy {
  hDUsers?: IHDUser[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected hDUserService: HDUserService,
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
      this.hDUserService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IHDUser[]>) => (this.hDUsers = res.body || []));
      return;
    }

    this.hDUserService.query().subscribe((res: HttpResponse<IHDUser[]>) => (this.hDUsers = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInHDUsers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHDUser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHDUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('hDUserListModification', () => this.loadAll());
  }

  delete(hDUser: IHDUser): void {
    const modalRef = this.modalService.open(HDUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.hDUser = hDUser;
  }
}
