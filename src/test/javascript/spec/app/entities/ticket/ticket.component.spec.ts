import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CsrappTestModule } from '../../../test.module';
import { TicketComponent } from 'app/entities/ticket/ticket.component';
import { TicketService } from 'app/entities/ticket/ticket.service';
import { Ticket } from 'app/shared/model/ticket.model';

describe('Component Tests', () => {
  describe('Ticket Management Component', () => {
    let comp: TicketComponent;
    let fixture: ComponentFixture<TicketComponent>;
    let service: TicketService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [TicketComponent],
      })
        .overrideTemplate(TicketComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TicketComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TicketService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Ticket(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.tickets && comp.tickets[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
