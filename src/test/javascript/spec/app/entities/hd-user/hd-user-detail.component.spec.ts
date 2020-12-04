import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CsrappTestModule } from '../../../test.module';
import { HDUserDetailComponent } from 'app/entities/hd-user/hd-user-detail.component';
import { HDUser } from 'app/shared/model/hd-user.model';

describe('Component Tests', () => {
  describe('HDUser Management Detail Component', () => {
    let comp: HDUserDetailComponent;
    let fixture: ComponentFixture<HDUserDetailComponent>;
    const route = ({ data: of({ hDUser: new HDUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [HDUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(HDUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HDUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load hDUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.hDUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
