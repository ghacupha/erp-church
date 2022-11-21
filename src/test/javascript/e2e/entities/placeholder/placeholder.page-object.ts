import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import PlaceholderUpdatePage from './placeholder-update.page-object';

const expect = chai.expect;
export class PlaceholderDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('erpChurchApp.placeholder.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-placeholder'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class PlaceholderComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('placeholder-heading'));
  noRecords: ElementFinder = element(by.css('#app-view-container .table-responsive div.alert.alert-warning'));
  table: ElementFinder = element(by.css('#app-view-container div.table-responsive > table'));

  records: ElementArrayFinder = this.table.all(by.css('tbody tr'));

  getDetailsButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-info.btn-sm'));
  }

  getEditButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-primary.btn-sm'));
  }

  getDeleteButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-danger.btn-sm'));
  }

  async goToPage(navBarPage: NavBarPage) {
    await navBarPage.getEntityPage('placeholder');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreatePlaceholder() {
    await this.createButton.click();
    return new PlaceholderUpdatePage();
  }

  async deletePlaceholder() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const placeholderDeleteDialog = new PlaceholderDeleteDialog();
    await waitUntilDisplayed(placeholderDeleteDialog.deleteModal);
    expect(await placeholderDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/erpChurchApp.placeholder.delete.question/);
    await placeholderDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(placeholderDeleteDialog.deleteModal);

    expect(await isVisible(placeholderDeleteDialog.deleteModal)).to.be.false;
  }
}
