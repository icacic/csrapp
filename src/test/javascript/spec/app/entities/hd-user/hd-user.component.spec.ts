import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CsrappTestModule } from '../../../test.module';
import { HDUserComponent } from 'app/entities/hd-user/hd-user.component';
import { HDUserService } from 'app/entities/hd-user/hd-user.service';
import { HDUser } from 'app/shared/model/hd-user.model';

describe('Component Tests', () => {
  describe('HDUser Management Component', () => {
    let comp: HDUserComponent;
    let fixture: ComponentFixture<HDUserComponent>;
    let service: HDUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [HDUserComponent],
      })
        .overrideTemplate(HDUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HDUserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HDUserService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HDUser(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.hDUsers && comp.hDUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
