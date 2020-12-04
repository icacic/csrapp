import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CsrappTestModule } from '../../../test.module';
import { HDUserUpdateComponent } from 'app/entities/hd-user/hd-user-update.component';
import { HDUserService } from 'app/entities/hd-user/hd-user.service';
import { HDUser } from 'app/shared/model/hd-user.model';

describe('Component Tests', () => {
  describe('HDUser Management Update Component', () => {
    let comp: HDUserUpdateComponent;
    let fixture: ComponentFixture<HDUserUpdateComponent>;
    let service: HDUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [HDUserUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(HDUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HDUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HDUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new HDUser(123);
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
        const entity = new HDUser();
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
