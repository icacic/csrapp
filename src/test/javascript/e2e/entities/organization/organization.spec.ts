import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { OrganizationComponentsPage, OrganizationDeleteDialog, OrganizationUpdatePage } from './organization.page-object';

const expect = chai.expect;

describe('Organization e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let organizationComponentsPage: OrganizationComponentsPage;
  let organizationUpdatePage: OrganizationUpdatePage;
  let organizationDeleteDialog: OrganizationDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Organizations', async () => {
    await navBarPage.goToEntity('organization');
    organizationComponentsPage = new OrganizationComponentsPage();
    await browser.wait(ec.visibilityOf(organizationComponentsPage.title), 5000);
    expect(await organizationComponentsPage.getTitle()).to.eq('csrappApp.organization.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(organizationComponentsPage.entities), ec.visibilityOf(organizationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Organization page', async () => {
    await organizationComponentsPage.clickOnCreateButton();
    organizationUpdatePage = new OrganizationUpdatePage();
    expect(await organizationUpdatePage.getPageTitle()).to.eq('csrappApp.organization.home.createOrEditLabel');
    await organizationUpdatePage.cancel();
  });

  it('should create and save Organizations', async () => {
    const nbButtonsBeforeCreate = await organizationComponentsPage.countDeleteButtons();

    await organizationComponentsPage.clickOnCreateButton();

    await promise.all([
      organizationUpdatePage.setNameInput('name'),
      organizationUpdatePage.setAddressInput('address'),
      organizationUpdatePage.typeSelectLastOption(),
    ]);

    expect(await organizationUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await organizationUpdatePage.getAddressInput()).to.eq('address', 'Expected Address value to be equals to address');

    await organizationUpdatePage.save();
    expect(await organizationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await organizationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Organization', async () => {
    const nbButtonsBeforeDelete = await organizationComponentsPage.countDeleteButtons();
    await organizationComponentsPage.clickOnLastDeleteButton();

    organizationDeleteDialog = new OrganizationDeleteDialog();
    expect(await organizationDeleteDialog.getDialogTitle()).to.eq('csrappApp.organization.delete.question');
    await organizationDeleteDialog.clickOnConfirmButton();

    expect(await organizationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
