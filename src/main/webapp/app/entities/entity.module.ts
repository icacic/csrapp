import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'organization',
        loadChildren: () => import('./organization/organization.module').then(m => m.CsrappOrganizationModule),
      },
      {
        path: 'project',
        loadChildren: () => import('./project/project.module').then(m => m.CsrappProjectModule),
      },
      {
        path: 'ticket',
        loadChildren: () => import('./ticket/ticket.module').then(m => m.CsrappTicketModule),
      },
      {
        path: 'hd-user',
        loadChildren: () => import('./hd-user/hd-user.module').then(m => m.CsrappHDUserModule),
      },
      {
        path: 'attachment',
        loadChildren: () => import('./attachment/attachment.module').then(m => m.CsrappAttachmentModule),
      },
      {
        path: 'ticket-status',
        loadChildren: () => import('./ticket-status/ticket-status.module').then(m => m.CsrappTicketStatusModule),
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.CsrappCategoryModule),
      },
      {
        path: 'priority',
        loadChildren: () => import('./priority/priority.module').then(m => m.CsrappPriorityModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class CsrappEntityModule {}
