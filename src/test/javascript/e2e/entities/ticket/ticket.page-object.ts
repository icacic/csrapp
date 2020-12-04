import { element, by, ElementFinder } from 'protractor';

export class TicketComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-ticket div table .btn-danger'));
  title = element.all(by.css('jhi-ticket div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class TicketUpdatePage {
  pageTitle = element(by.id('jhi-ticket-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  rbrInput = element(by.id('field_rbr'));
  descriptionInput = element(by.id('field_description'));

  statusSelect = element(by.id('field_status'));
  categorySelect = element(by.id('field_category'));
  prioritySelect = element(by.id('field_priority'));
  usersSelect = element(by.id('field_users'));
  projectSelect = element(by.id('field_project'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setRbrInput(rbr: string): Promise<void> {
    await this.rbrInput.sendKeys(rbr);
  }

  async getRbrInput(): Promise<string> {
    return await this.rbrInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async statusSelectLastOption(): Promise<void> {
    await this.statusSelect.all(by.tagName('option')).last().click();
  }

  async statusSelectOption(option: string): Promise<void> {
    await this.statusSelect.sendKeys(option);
  }

  getStatusSelect(): ElementFinder {
    return this.statusSelect;
  }

  async getStatusSelectedOption(): Promise<string> {
    return await this.statusSelect.element(by.css('option:checked')).getText();
  }

  async categorySelectLastOption(): Promise<void> {
    await this.categorySelect.all(by.tagName('option')).last().click();
  }

  async categorySelectOption(option: string): Promise<void> {
    await this.categorySelect.sendKeys(option);
  }

  getCategorySelect(): ElementFinder {
    return this.categorySelect;
  }

  async getCategorySelectedOption(): Promise<string> {
    return await this.categorySelect.element(by.css('option:checked')).getText();
  }

  async prioritySelectLastOption(): Promise<void> {
    await this.prioritySelect.all(by.tagName('option')).last().click();
  }

  async prioritySelectOption(option: string): Promise<void> {
    await this.prioritySelect.sendKeys(option);
  }

  getPrioritySelect(): ElementFinder {
    return this.prioritySelect;
  }

  async getPrioritySelectedOption(): Promise<string> {
    return await this.prioritySelect.element(by.css('option:checked')).getText();
  }

  async usersSelectLastOption(): Promise<void> {
    await this.usersSelect.all(by.tagName('option')).last().click();
  }

  async usersSelectOption(option: string): Promise<void> {
    await this.usersSelect.sendKeys(option);
  }

  getUsersSelect(): ElementFinder {
    return this.usersSelect;
  }

  async getUsersSelectedOption(): Promise<string> {
    return await this.usersSelect.element(by.css('option:checked')).getText();
  }

  async projectSelectLastOption(): Promise<void> {
    await this.projectSelect.all(by.tagName('option')).last().click();
  }

  async projectSelectOption(option: string): Promise<void> {
    await this.projectSelect.sendKeys(option);
  }

  getProjectSelect(): ElementFinder {
    return this.projectSelect;
  }

  async getProjectSelectedOption(): Promise<string> {
    return await this.projectSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class TicketDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-ticket-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-ticket'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
