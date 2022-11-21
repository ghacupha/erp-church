import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Placeholder e2e test', () => {
  const placeholderPageUrl = '/placeholder';
  const placeholderPageUrlPattern = new RegExp('/placeholder(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const placeholderSample = { placeholderIndex: 'Frozen Keyboard' };

  let placeholder;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/placeholders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/placeholders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/placeholders/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (placeholder) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/placeholders/${placeholder.id}`,
      }).then(() => {
        placeholder = undefined;
      });
    }
  });

  it('Placeholders menu should load Placeholders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('placeholder');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Placeholder').should('exist');
    cy.url().should('match', placeholderPageUrlPattern);
  });

  describe('Placeholder page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(placeholderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Placeholder page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/placeholder/new$'));
        cy.getEntityCreateUpdateHeading('Placeholder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', placeholderPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/placeholders',
          body: placeholderSample,
        }).then(({ body }) => {
          placeholder = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/placeholders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/placeholders?page=0&size=20>; rel="last",<http://localhost/api/placeholders?page=0&size=20>; rel="first"',
              },
              body: [placeholder],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(placeholderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Placeholder page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('placeholder');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', placeholderPageUrlPattern);
      });

      it('edit button click should load edit Placeholder page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Placeholder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', placeholderPageUrlPattern);
      });

      it('edit button click should load edit Placeholder page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Placeholder');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', placeholderPageUrlPattern);
      });

      it('last delete button click should delete instance of Placeholder', () => {
        cy.intercept('GET', '/api/placeholders/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('placeholder').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', placeholderPageUrlPattern);

        placeholder = undefined;
      });
    });
  });

  describe('new Placeholder page', () => {
    beforeEach(() => {
      cy.visit(`${placeholderPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Placeholder');
    });

    it('should create an instance of Placeholder', () => {
      cy.get(`[data-cy="placeholderIndex"]`).type('methodologies').should('have.value', 'methodologies');

      cy.get(`[data-cy="placeholderValue"]`).type('Shoes').should('have.value', 'Shoes');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        placeholder = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', placeholderPageUrlPattern);
    });
  });
});
