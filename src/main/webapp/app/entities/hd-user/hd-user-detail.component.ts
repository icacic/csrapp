import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHDUser } from 'app/shared/model/hd-user.model';

@Component({
  selector: 'jhi-hd-user-detail',
  templateUrl: './hd-user-detail.component.html',
})
export class HDUserDetailComponent implements OnInit {
  hDUser: IHDUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hDUser }) => (this.hDUser = hDUser));
  }

  previousState(): void {
    window.history.back();
  }
}
