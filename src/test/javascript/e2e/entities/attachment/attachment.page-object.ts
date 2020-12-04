import { element, by, ElementFinder } from 'protractor';

export class AttachmentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-attachment div table .btn-danger'));
  title = element.all(by.css('jhi-attachment div h2#page-heading span')).first();
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

export class AttachmentUpdatePage {
  pageTitle = element(by.id('jhi-attachment-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nameInput = element(by.id('field_name'));
  extensionSelect = element(by.id('field_extension'));
  fileInput = element(by.id('file_file'));

  ticketSelect = element(by.id('field_ticket'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setExtensionSelect(extension: string): Promise<void> {
    await this.extensionSelect.sendKeys(extension);
  }

  async getExtensionSelect(): Promise<string> {
    return await this.extensionSelect.element(by.css('option:checked')).getText();
  }

  async extensionSelectLastOption(): Promise<void> {
    await this.extensionSelect.all(by.tagName('option')).last().click();
  }

  async setFileInput(file: string): Promise<void> {
    await this.fileInput.sendKeys(file);
  }

  async getFileInput(): Promise<string> {
    return await this.fileInput.getAttribute('value');
  }

  async ticketSelectLastOption(): Promise<void> {
    await this.ticketSelect.all(by.tagName('option')).last().click();
  }

  async ticketSelectOption(option: string): Promise<void> {
    await this.ticketSelect.sendKeys(option);
  }

  getTicketSelect(): ElementFinder {
    return this.ticketSelect;
  }

  async getTicketSelectedOption(): Promise<string> {
    return await this.ticketSelect.element(by.css('option:checked')).getText();
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

export class AttachmentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-attachment-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-attachment'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
