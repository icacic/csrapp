import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ITicket, Ticket } from 'app/shared/model/ticket.model';
import { TicketService } from './ticket.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ITicketStatus } from 'app/shared/model/ticket-status.model';
import { TicketStatusService } from 'app/entities/ticket-status/ticket-status.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { IPriority } from 'app/shared/model/priority.model';
import { PriorityService } from 'app/entities/priority/priority.service';
import { IHDUser } from 'app/shared/model/hd-user.model';
import { HDUserService } from 'app/entities/hd-user/hd-user.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project/project.service';

type SelectableEntity = ITicketStatus | ICategory | IPriority | IHDUser | IProject;

@Component({
  selector: 'jhi-ticket-update',
  templateUrl: './ticket-update.component.html',
})
export class TicketUpdateComponent implements OnInit {
  isSaving = false;
  ticketstatuses: ITicketStatus[] = [];
  categories: ICategory[] = [];
  priorities: IPriority[] = [];
  hdusers: IHDUser[] = [];
  projects: IProject[] = [];

  editForm = this.fb.group({
    id: [],
    rbr: [],
    description: [],
    statusId: [],
    categoryId: [],
    priorityId: [],
    users: [],
    projectId: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected ticketService: TicketService,
    protected ticketStatusService: TicketStatusService,
    protected categoryService: CategoryService,
    protected priorityService: PriorityService,
    protected hDUserService: HDUserService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ticket }) => {
      this.updateForm(ticket);

      this.ticketStatusService.query().subscribe((res: HttpResponse<ITicketStatus[]>) => (this.ticketstatuses = res.body || []));

      this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));

      this.priorityService.query().subscribe((res: HttpResponse<IPriority[]>) => (this.priorities = res.body || []));

      this.hDUserService.query().subscribe((res: HttpResponse<IHDUser[]>) => (this.hdusers = res.body || []));

      this.projectService.query().subscribe((res: HttpResponse<IProject[]>) => (this.projects = res.body || []));
    });
  }

  updateForm(ticket: ITicket): void {
    this.editForm.patchValue({
      id: ticket.id,
      rbr: ticket.rbr,
      description: ticket.description,
      statusId: ticket.statusId,
      categoryId: ticket.categoryId,
      priorityId: ticket.priorityId,
      users: ticket.users,
      projectId: ticket.projectId,
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
    const ticket = this.createFromForm();
    if (ticket.id !== undefined) {
      this.subscribeToSaveResponse(this.ticketService.update(ticket));
    } else {
      this.subscribeToSaveResponse(this.ticketService.create(ticket));
    }
  }

  private createFromForm(): ITicket {
    return {
      ...new Ticket(),
      id: this.editForm.get(['id'])!.value,
      rbr: this.editForm.get(['rbr'])!.value,
      description: this.editForm.get(['description'])!.value,
      statusId: this.editForm.get(['statusId'])!.value,
      categoryId: this.editForm.get(['categoryId'])!.value,
      priorityId: this.editForm.get(['priorityId'])!.value,
      users: this.editForm.get(['users'])!.value,
      projectId: this.editForm.get(['projectId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITicket>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IHDUser[], option: IHDUser): IHDUser {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
