import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CsrappTestModule } from '../../../test.module';
import { TicketStatusUpdateComponent } from 'app/entities/ticket-status/ticket-status-update.component';
import { TicketStatusService } from 'app/entities/ticket-status/ticket-status.service';
import { TicketStatus } from 'app/shared/model/ticket-status.model';

describe('Component Tests', () => {
  describe('TicketStatus Management Update Component', () => {
    let comp: TicketStatusUpdateComponent;
    let fixture: ComponentFixture<TicketStatusUpdateComponent>;
    let service: TicketStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [TicketStatusUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TicketStatusUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TicketStatusUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TicketStatusService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TicketStatus(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new TicketStatus();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
