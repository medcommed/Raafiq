import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppRole } from '../app-role.model';

@Component({
  selector: 'jhi-app-role-detail',
  templateUrl: './app-role-detail.component.html',
})
export class AppRoleDetailComponent implements OnInit {
  appRole: IAppRole | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appRole }) => {
      this.appRole = appRole;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
