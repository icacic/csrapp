import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CsrappTestModule } from '../../../test.module';
import { PriorityUpdateComponent } from 'app/entities/priority/priority-update.component';
import { PriorityService } from 'app/entities/priority/priority.service';
import { Priority } from 'app/shared/model/priority.model';

describe('Component Tests', () => {
  describe('Priority Management Update Component', () => {
    let comp: PriorityUpdateComponent;
    let fixture: ComponentFixture<PriorityUpdateComponent>;
    let service: PriorityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [PriorityUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PriorityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PriorityUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PriorityService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Priority(123);
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
        const entity = new Priority();
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
