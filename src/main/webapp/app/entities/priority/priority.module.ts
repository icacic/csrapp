import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CsrappSharedModule } from 'app/shared/shared.module';
import { PriorityComponent } from './priority.component';
import { PriorityDetailComponent } from './priority-detail.component';
import { PriorityUpdateComponent } from './priority-update.component';
import { PriorityDeleteDialogComponent } from './priority-delete-dialog.component';
import { priorityRoute } from './priority.route';

@NgModule({
  imports: [CsrappSharedModule, RouterModule.forChild(priorityRoute)],
  declarations: [PriorityComponent, PriorityDetailComponent, PriorityUpdateComponent, PriorityDeleteDialogComponent],
  entryComponents: [PriorityDeleteDialogComponent],
})
export class CsrappPriorityModule {}
