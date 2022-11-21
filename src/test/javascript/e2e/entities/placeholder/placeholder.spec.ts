import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import PlaceholderComponentsPage from './placeholder.page-object';
import PlaceholderUpdatePage from './placeholder-update.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../util/utils';

const expect = chai.expect;

describe('Placeholder e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let placeholderComponentsPage: PlaceholderComponentsPage;
  let placeholderUpdatePage: PlaceholderUpdatePage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();
    await signInPage.username.sendKeys(username);
    await signInPage.password.sendKeys(password);
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
    await waitUntilDisplayed(navBarPage.adminMenu);
    await waitUntilDisplayed(navBarPage.accountMenu);
  });

  beforeEach(async () => {
    await browser.get('/');
    await waitUntilDisplayed(navBarPage.entityMenu);
    placeholderComponentsPage = new PlaceholderComponentsPage();
    placeholderComponentsPage = await placeholderComponentsPage.goToPage(navBarPage);
  });

  it('should load Placeholders', async () => {
    expect(await placeholderComponentsPage.title.getText()).to.match(/Placeholders/);
    expect(await placeholderComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete Placeholders', async () => {
    const beforeRecordsCount = (await isVisible(placeholderComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(placeholderComponentsPage.table);
    placeholderUpdatePage = await placeholderComponentsPage.goToCreatePlaceholder();
    await placeholderUpdatePage.enterData();
    expect(await isVisible(placeholderUpdatePage.saveButton)).to.be.false;

    expect(await placeholderComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(placeholderComponentsPage.table);
    await waitUntilCount(placeholderComponentsPage.records, beforeRecordsCount + 1);
    expect(await placeholderComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await placeholderComponentsPage.deletePlaceholder();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(placeholderComponentsPage.records, beforeRecordsCount);
      expect(await placeholderComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(placeholderComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
