import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CsrappSharedModule } from 'app/shared/shared.module';
import { HDUserComponent } from './hd-user.component';
import { HDUserDetailComponent } from './hd-user-detail.component';
import { HDUserUpdateComponent } from './hd-user-update.component';
import { HDUserDeleteDialogComponent } from './hd-user-delete-dialog.component';
import { hDUserRoute } from './hd-user.route';

@NgModule({
  imports: [CsrappSharedModule, RouterModule.forChild(hDUserRoute)],
  declarations: [HDUserComponent, HDUserDetailComponent, HDUserUpdateComponent, HDUserDeleteDialogComponent],
  entryComponents: [HDUserDeleteDialogComponent],
})
export class CsrappHDUserModule {}
