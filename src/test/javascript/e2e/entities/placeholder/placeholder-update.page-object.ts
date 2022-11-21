import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class PlaceholderUpdatePage {
  pageTitle: ElementFinder = element(by.id('erpChurchApp.placeholder.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  placeholderIndexInput: ElementFinder = element(by.css('input#placeholder-placeholderIndex'));
  placeholderValueInput: ElementFinder = element(by.css('input#placeholder-placeholderValue'));
  archetypeSelect: ElementFinder = element(by.css('select#placeholder-archetype'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setPlaceholderIndexInput(placeholderIndex) {
    await this.placeholderIndexInput.sendKeys(placeholderIndex);
  }

  async getPlaceholderIndexInput() {
    return this.placeholderIndexInput.getAttribute('value');
  }

  async setPlaceholderValueInput(placeholderValue) {
    await this.placeholderValueInput.sendKeys(placeholderValue);
  }

  async getPlaceholderValueInput() {
    return this.placeholderValueInput.getAttribute('value');
  }

  async archetypeSelectLastOption() {
    await this.archetypeSelect.all(by.tagName('option')).last().click();
  }

  async archetypeSelectOption(option) {
    await this.archetypeSelect.sendKeys(option);
  }

  getArchetypeSelect() {
    return this.archetypeSelect;
  }

  async getArchetypeSelectedOption() {
    return this.archetypeSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }

  async enterData() {
    await waitUntilDisplayed(this.saveButton);
    await this.setPlaceholderIndexInput('placeholderIndex');
    await waitUntilDisplayed(this.saveButton);
    await this.setPlaceholderValueInput('placeholderValue');
    await this.archetypeSelectLastOption();
    await this.save();
    await waitUntilHidden(this.saveButton);
  }
}
