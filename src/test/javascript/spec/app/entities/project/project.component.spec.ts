import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CsrappTestModule } from '../../../test.module';
import { ProjectComponent } from 'app/entities/project/project.component';
import { ProjectService } from 'app/entities/project/project.service';
import { Project } from 'app/shared/model/project.model';

describe('Component Tests', () => {
  describe('Project Management Component', () => {
    let comp: ProjectComponent;
    let fixture: ComponentFixture<ProjectComponent>;
    let service: ProjectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [ProjectComponent],
      })
        .overrideTemplate(ProjectComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProjectComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProjectService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Project(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.projects && comp.projects[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
