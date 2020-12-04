import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from './category.service';
import { CategoryDeleteDialogComponent } from './category-delete-dialog.component';

@Component({
  selector: 'jhi-category',
  templateUrl: './category.component.html',
})
export class CategoryComponent implements OnInit, OnDestroy {
  categories?: ICategory[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected categoryService: CategoryService,
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
      this.categoryService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
      return;
    }

    this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCategories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICategory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCategories(): void {
    this.eventSubscriber = this.eventManager.subscribe('categoryListModification', () => this.loadAll());
  }

  delete(category: ICategory): void {
    const modalRef = this.modalService.open(CategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.category = category;
  }
}
