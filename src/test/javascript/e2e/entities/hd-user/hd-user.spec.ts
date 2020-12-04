import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { HDUserComponentsPage, HDUserDeleteDialog, HDUserUpdatePage } from './hd-user.page-object';

const expect = chai.expect;

describe('HDUser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let hDUserComponentsPage: HDUserComponentsPage;
  let hDUserUpdatePage: HDUserUpdatePage;
  let hDUserDeleteDialog: HDUserDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load HDUsers', async () => {
    await navBarPage.goToEntity('hd-user');
    hDUserComponentsPage = new HDUserComponentsPage();
    await browser.wait(ec.visibilityOf(hDUserComponentsPage.title), 5000);
    expect(await hDUserComponentsPage.getTitle()).to.eq('csrappApp.hDUser.home.title');
    await browser.wait(ec.or(ec.visibilityOf(hDUserComponentsPage.entities), ec.visibilityOf(hDUserComponentsPage.noResult)), 1000);
  });

  it('should load create HDUser page', async () => {
    await hDUserComponentsPage.clickOnCreateButton();
    hDUserUpdatePage = new HDUserUpdatePage();
    expect(await hDUserUpdatePage.getPageTitle()).to.eq('csrappApp.hDUser.home.createOrEditLabel');
    await hDUserUpdatePage.cancel();
  });

  it('should create and save HDUsers', async () => {
    const nbButtonsBeforeCreate = await hDUserComponentsPage.countDeleteButtons();

    await hDUserComponentsPage.clickOnCreateButton();

    await promise.all([
      hDUserUpdatePage.setFirstNameInput('firstName'),
      hDUserUpdatePage.setLastNameInput('lastName'),
      hDUserUpdatePage.setEmailInput('email'),
      hDUserUpdatePage.setAddressInput('address'),
      hDUserUpdatePage.userSelectLastOption(),
      hDUserUpdatePage.organizationSelectLastOption(),
    ]);

    expect(await hDUserUpdatePage.getFirstNameInput()).to.eq('firstName', 'Expected FirstName value to be equals to firstName');
    expect(await hDUserUpdatePage.getLastNameInput()).to.eq('lastName', 'Expected LastName value to be equals to lastName');
    expect(await hDUserUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await hDUserUpdatePage.getAddressInput()).to.eq('address', 'Expected Address value to be equals to address');

    await hDUserUpdatePage.save();
    expect(await hDUserUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await hDUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last HDUser', async () => {
    const nbButtonsBeforeDelete = await hDUserComponentsPage.countDeleteButtons();
    await hDUserComponentsPage.clickOnLastDeleteButton();

    hDUserDeleteDialog = new HDUserDeleteDialog();
    expect(await hDUserDeleteDialog.getDialogTitle()).to.eq('csrappApp.hDUser.delete.question');
    await hDUserDeleteDialog.clickOnConfirmButton();

    expect(await hDUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
