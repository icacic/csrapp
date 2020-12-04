import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPriority } from 'app/shared/model/priority.model';

@Component({
  selector: 'jhi-priority-detail',
  templateUrl: './priority-detail.component.html',
})
export class PriorityDetailComponent implements OnInit {
  priority: IPriority | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priority }) => (this.priority = priority));
  }

  previousState(): void {
    window.history.back();
  }
}
