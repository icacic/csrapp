import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPriority, Priority } from 'app/shared/model/priority.model';
import { PriorityService } from './priority.service';

@Component({
  selector: 'jhi-priority-update',
  templateUrl: './priority-update.component.html',
})
export class PriorityUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected priorityService: PriorityService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priority }) => {
      this.updateForm(priority);
    });
  }

  updateForm(priority: IPriority): void {
    this.editForm.patchValue({
      id: priority.id,
      name: priority.name,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const priority = this.createFromForm();
    if (priority.id !== undefined) {
      this.subscribeToSaveResponse(this.priorityService.update(priority));
    } else {
      this.subscribeToSaveResponse(this.priorityService.create(priority));
    }
  }

  private createFromForm(): IPriority {
    return {
      ...new Priority(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPriority>>): void {
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
