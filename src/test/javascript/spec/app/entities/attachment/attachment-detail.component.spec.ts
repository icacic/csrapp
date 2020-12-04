import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { CsrappTestModule } from '../../../test.module';
import { AttachmentDetailComponent } from 'app/entities/attachment/attachment-detail.component';
import { Attachment } from 'app/shared/model/attachment.model';

describe('Component Tests', () => {
  describe('Attachment Management Detail Component', () => {
    let comp: AttachmentDetailComponent;
    let fixture: ComponentFixture<AttachmentDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ attachment: new Attachment(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsrappTestModule],
        declarations: [AttachmentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AttachmentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttachmentDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load attachment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.attachment).toEqual(jasmine.objectContaining({ id: 123 }));
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
