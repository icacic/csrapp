import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from './organization.service';
import { OrganizationDeleteDialogComponent } from './organization-delete-dialog.component';

@Component({
  selector: 'jhi-organization',
  templateUrl: './organization.component.html',
})
export class OrganizationComponent implements OnInit, OnDestroy {
  organizations?: IOrganization[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected organizationService: OrganizationService,
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
      this.organizationService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));
      return;
    }

    this.organizationService.query().subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInOrganizations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOrganization): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInOrganizations(): void {
    this.eventSubscriber = this.eventManager.subscribe('organizationListModification', () => this.loadAll());
  }

  delete(organization: IOrganization): void {
    const modalRef = this.modalService.open(OrganizationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.organization = organization;
  }
}
