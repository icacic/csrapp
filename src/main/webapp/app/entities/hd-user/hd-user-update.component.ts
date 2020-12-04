import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IHDUser, HDUser } from 'app/shared/model/hd-user.model';
import { HDUserService } from './hd-user.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from 'app/entities/organization/organization.service';

type SelectableEntity = IUser | IOrganization;

@Component({
  selector: 'jhi-hd-user-update',
  templateUrl: './hd-user-update.component.html',
})
export class HDUserUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  organizations: IOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    email: [],
    address: [],
    userId: [],
    organizationId: [],
  });

  constructor(
    protected hDUserService: HDUserService,
    protected userService: UserService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hDUser }) => {
      this.updateForm(hDUser);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.organizationService.query().subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));
    });
  }

  updateForm(hDUser: IHDUser): void {
    this.editForm.patchValue({
      id: hDUser.id,
      firstName: hDUser.firstName,
      lastName: hDUser.lastName,
      email: hDUser.email,
      address: hDUser.address,
      userId: hDUser.userId,
      organizationId: hDUser.organizationId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hDUser = this.createFromForm();
    if (hDUser.id !== undefined) {
      this.subscribeToSaveResponse(this.hDUserService.update(hDUser));
    } else {
      this.subscribeToSaveResponse(this.hDUserService.create(hDUser));
    }
  }

  private createFromForm(): IHDUser {
    return {
      ...new HDUser(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      address: this.editForm.get(['address'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      organizationId: this.editForm.get(['organizationId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHDUser>>): void {
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
}
