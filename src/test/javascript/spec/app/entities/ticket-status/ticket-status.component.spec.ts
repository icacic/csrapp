import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CsrappTestModule } from '../../../test.module';
import { TicketStatusComponent } from 'app/entities/ticket-status/ticket-status.component';
import { TicketStatusService } from 'app/entities/ticket-status/ticket-status.service';
import { TicketStatus } from 'app/shared/model/ticket-status.model';

describe('Component Tests', () => {
  describe('TicketStatus Management Component', () => {
    let comp: TicketStatusComponent;
    let fixture: ComponentFixture<TicketStatusComponent>;
    let service: TicketStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [TicketStatusComponent],
      })
        .overrideTemplate(TicketStatusComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TicketStatusComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TicketStatusService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TicketStatus(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.ticketStatuses && comp.ticketStatuses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
