import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CsrappTestModule } from '../../../test.module';
import { TicketStatusDetailComponent } from 'app/entities/ticket-status/ticket-status-detail.component';
import { TicketStatus } from 'app/shared/model/ticket-status.model';

describe('Component Tests', () => {
  describe('TicketStatus Management Detail Component', () => {
    let comp: TicketStatusDetailComponent;
    let fixture: ComponentFixture<TicketStatusDetailComponent>;
    const route = ({ data: of({ ticketStatus: new TicketStatus(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [TicketStatusDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TicketStatusDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TicketStatusDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ticketStatus on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ticketStatus).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
