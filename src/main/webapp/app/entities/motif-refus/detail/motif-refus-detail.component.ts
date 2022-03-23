import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMotifRefus } from '../motif-refus.model';

@Component({
  selector: 'jhi-motif-refus-detail',
  templateUrl: './motif-refus-detail.component.html',
})
export class MotifRefusDetailComponent implements OnInit {
  motifRefus: IMotifRefus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ motifRefus }) => {
      this.motifRefus = motifRefus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
