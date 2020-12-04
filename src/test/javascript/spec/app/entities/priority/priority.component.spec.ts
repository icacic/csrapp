import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CsrappTestModule } from '../../../test.module';
import { PriorityComponent } from 'app/entities/priority/priority.component';
import { PriorityService } from 'app/entities/priority/priority.service';
import { Priority } from 'app/shared/model/priority.model';

describe('Component Tests', () => {
  describe('Priority Management Component', () => {
    let comp: PriorityComponent;
    let fixture: ComponentFixture<PriorityComponent>;
    let service: PriorityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [PriorityComponent],
      })
        .overrideTemplate(PriorityComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PriorityComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PriorityService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Priority(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.priorities && comp.priorities[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
