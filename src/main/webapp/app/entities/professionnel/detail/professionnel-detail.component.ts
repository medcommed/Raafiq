import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProfessionnel } from '../professionnel.model';

@Component({
  selector: 'jhi-professionnel-detail',
  templateUrl: './professionnel-detail.component.html',
})
export class ProfessionnelDetailComponent implements OnInit {
  professionnel: IProfessionnel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ professionnel }) => {
      this.professionnel = professionnel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
