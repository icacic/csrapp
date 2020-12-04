import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AttachmentComponentsPage, AttachmentDeleteDialog, AttachmentUpdatePage } from './attachment.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Attachment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let attachmentComponentsPage: AttachmentComponentsPage;
  let attachmentUpdatePage: AttachmentUpdatePage;
  let attachmentDeleteDialog: AttachmentDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Attachments', async () => {
    await navBarPage.goToEntity('attachment');
    attachmentComponentsPage = new AttachmentComponentsPage();
    await browser.wait(ec.visibilityOf(attachmentComponentsPage.title), 5000);
    expect(await attachmentComponentsPage.getTitle()).to.eq('csrappApp.attachment.home.title');
    await browser.wait(ec.or(ec.visibilityOf(attachmentComponentsPage.entities), ec.visibilityOf(attachmentComponentsPage.noResult)), 1000);
  });

  it('should load create Attachment page', async () => {
    await attachmentComponentsPage.clickOnCreateButton();
    attachmentUpdatePage = new AttachmentUpdatePage();
    expect(await attachmentUpdatePage.getPageTitle()).to.eq('csrappApp.attachment.home.createOrEditLabel');
    await attachmentUpdatePage.cancel();
  });

  it('should create and save Attachments', async () => {
    const nbButtonsBeforeCreate = await attachmentComponentsPage.countDeleteButtons();

    await attachmentComponentsPage.clickOnCreateButton();

    await promise.all([
      attachmentUpdatePage.setNameInput('name'),
      attachmentUpdatePage.extensionSelectLastOption(),
      attachmentUpdatePage.setFileInput(absolutePath),
      attachmentUpdatePage.ticketSelectLastOption(),
    ]);

    expect(await attachmentUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await attachmentUpdatePage.getFileInput()).to.endsWith(
      fileNameToUpload,
      'Expected File value to be end with ' + fileNameToUpload
    );

    await attachmentUpdatePage.save();
    expect(await attachmentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await attachmentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Attachment', async () => {
    const nbButtonsBeforeDelete = await attachmentComponentsPage.countDeleteButtons();
    await attachmentComponentsPage.clickOnLastDeleteButton();

    attachmentDeleteDialog = new AttachmentDeleteDialog();
    expect(await attachmentDeleteDialog.getDialogTitle()).to.eq('csrappApp.attachment.delete.question');
    await attachmentDeleteDialog.clickOnConfirmButton();

    expect(await attachmentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
