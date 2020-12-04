import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CsrappSharedModule } from 'app/shared/shared.module';
import { TicketStatusComponent } from './ticket-status.component';
import { TicketStatusDetailComponent } from './ticket-status-detail.component';
import { TicketStatusUpdateComponent } from './ticket-status-update.component';
import { TicketStatusDeleteDialogComponent } from './ticket-status-delete-dialog.component';
import { ticketStatusRoute } from './ticket-status.route';

@NgModule({
  imports: [CsrappSharedModule, RouterModule.forChild(ticketStatusRoute)],
  declarations: [TicketStatusComponent, TicketStatusDetailComponent, TicketStatusUpdateComponent, TicketStatusDeleteDialogComponent],
  entryComponents: [TicketStatusDeleteDialogComponent],
})
export class CsrappTicketStatusModule {}
