import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { CsrappTestModule } from '../../../test.module';
import { TicketDetailComponent } from 'app/entities/ticket/ticket-detail.component';
import { Ticket } from 'app/shared/model/ticket.model';

describe('Component Tests', () => {
  describe('Ticket Management Detail Component', () => {
    let comp: TicketDetailComponent;
    let fixture: ComponentFixture<TicketDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ ticket: new Ticket(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [TicketDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TicketDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TicketDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load ticket on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ticket).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
