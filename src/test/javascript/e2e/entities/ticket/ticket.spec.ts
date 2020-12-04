import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { TicketComponentsPage, TicketDeleteDialog, TicketUpdatePage } from './ticket.page-object';

const expect = chai.expect;

describe('Ticket e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ticketComponentsPage: TicketComponentsPage;
  let ticketUpdatePage: TicketUpdatePage;
  let ticketDeleteDialog: TicketDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Tickets', async () => {
    await navBarPage.goToEntity('ticket');
    ticketComponentsPage = new TicketComponentsPage();
    await browser.wait(ec.visibilityOf(ticketComponentsPage.title), 5000);
    expect(await ticketComponentsPage.getTitle()).to.eq('csrappApp.ticket.home.title');
    await browser.wait(ec.or(ec.visibilityOf(ticketComponentsPage.entities), ec.visibilityOf(ticketComponentsPage.noResult)), 1000);
  });

  it('should load create Ticket page', async () => {
    await ticketComponentsPage.clickOnCreateButton();
    ticketUpdatePage = new TicketUpdatePage();
    expect(await ticketUpdatePage.getPageTitle()).to.eq('csrappApp.ticket.home.createOrEditLabel');
    await ticketUpdatePage.cancel();
  });

  it('should create and save Tickets', async () => {
    const nbButtonsBeforeCreate = await ticketComponentsPage.countDeleteButtons();

    await ticketComponentsPage.clickOnCreateButton();

    await promise.all([
      ticketUpdatePage.setRbrInput('rbr'),
      ticketUpdatePage.setDescriptionInput('description'),
      ticketUpdatePage.statusSelectLastOption(),
      ticketUpdatePage.categorySelectLastOption(),
      ticketUpdatePage.prioritySelectLastOption(),
      // ticketUpdatePage.usersSelectLastOption(),
      ticketUpdatePage.projectSelectLastOption(),
    ]);

    expect(await ticketUpdatePage.getRbrInput()).to.eq('rbr', 'Expected Rbr value to be equals to rbr');
    expect(await ticketUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');

    await ticketUpdatePage.save();
    expect(await ticketUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ticketComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Ticket', async () => {
    const nbButtonsBeforeDelete = await ticketComponentsPage.countDeleteButtons();
    await ticketComponentsPage.clickOnLastDeleteButton();

    ticketDeleteDialog = new TicketDeleteDialog();
    expect(await ticketDeleteDialog.getDialogTitle()).to.eq('csrappApp.ticket.delete.question');
    await ticketDeleteDialog.clickOnConfirmButton();

    expect(await ticketComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
