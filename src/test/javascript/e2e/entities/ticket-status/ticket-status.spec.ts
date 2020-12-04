import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { TicketStatusComponentsPage, TicketStatusDeleteDialog, TicketStatusUpdatePage } from './ticket-status.page-object';

const expect = chai.expect;

describe('TicketStatus e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ticketStatusComponentsPage: TicketStatusComponentsPage;
  let ticketStatusUpdatePage: TicketStatusUpdatePage;
  let ticketStatusDeleteDialog: TicketStatusDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TicketStatuses', async () => {
    await navBarPage.goToEntity('ticket-status');
    ticketStatusComponentsPage = new TicketStatusComponentsPage();
    await browser.wait(ec.visibilityOf(ticketStatusComponentsPage.title), 5000);
    expect(await ticketStatusComponentsPage.getTitle()).to.eq('csrappApp.ticketStatus.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(ticketStatusComponentsPage.entities), ec.visibilityOf(ticketStatusComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TicketStatus page', async () => {
    await ticketStatusComponentsPage.clickOnCreateButton();
    ticketStatusUpdatePage = new TicketStatusUpdatePage();
    expect(await ticketStatusUpdatePage.getPageTitle()).to.eq('csrappApp.ticketStatus.home.createOrEditLabel');
    await ticketStatusUpdatePage.cancel();
  });

  it('should create and save TicketStatuses', async () => {
    const nbButtonsBeforeCreate = await ticketStatusComponentsPage.countDeleteButtons();

    await ticketStatusComponentsPage.clickOnCreateButton();

    await promise.all([ticketStatusUpdatePage.setNameInput('name')]);

    expect(await ticketStatusUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');

    await ticketStatusUpdatePage.save();
    expect(await ticketStatusUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ticketStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last TicketStatus', async () => {
    const nbButtonsBeforeDelete = await ticketStatusComponentsPage.countDeleteButtons();
    await ticketStatusComponentsPage.clickOnLastDeleteButton();

    ticketStatusDeleteDialog = new TicketStatusDeleteDialog();
    expect(await ticketStatusDeleteDialog.getDialogTitle()).to.eq('csrappApp.ticketStatus.delete.question');
    await ticketStatusDeleteDialog.clickOnConfirmButton();

    expect(await ticketStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
