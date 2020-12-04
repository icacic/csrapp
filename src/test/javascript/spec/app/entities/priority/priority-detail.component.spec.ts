import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CsrappTestModule } from '../../../test.module';
import { PriorityDetailComponent } from 'app/entities/priority/priority-detail.component';
import { Priority } from 'app/shared/model/priority.model';

describe('Component Tests', () => {
  describe('Priority Management Detail Component', () => {
    let comp: PriorityDetailComponent;
    let fixture: ComponentFixture<PriorityDetailComponent>;
    const route = ({ data: of({ priority: new Priority(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [PriorityDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PriorityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PriorityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load priority on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.priority).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
