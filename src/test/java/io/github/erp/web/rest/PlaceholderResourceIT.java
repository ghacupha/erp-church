package io.github.erp.web.rest;

/*-
 * Erp Church - Data management for religious institutions
 * Copyright © 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.AppUser;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.EntityManager;
import io.github.erp.repository.PlaceholderRepository;
import io.github.erp.repository.search.PlaceholderSearchRepository;
import io.github.erp.service.PlaceholderService;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.mapper.PlaceholderMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link PlaceholderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PlaceholderResourceIT {

    private static final String DEFAULT_PLACEHOLDER_INDEX = "AAAAAAAAAA";
    private static final String UPDATED_PLACEHOLDER_INDEX = "BBBBBBBBBB";

    private static final String DEFAULT_PLACEHOLDER_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_PLACEHOLDER_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/placeholders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/placeholders";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlaceholderRepository placeholderRepository;

    @Mock
    private PlaceholderRepository placeholderRepositoryMock;

    @Autowired
    private PlaceholderMapper placeholderMapper;

    @Mock
    private PlaceholderService placeholderServiceMock;

    @Autowired
    private PlaceholderSearchRepository placeholderSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Placeholder placeholder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Placeholder createEntity(EntityManager em) {
        Placeholder placeholder = new Placeholder().placeholderIndex(DEFAULT_PLACEHOLDER_INDEX).placeholderValue(DEFAULT_PLACEHOLDER_VALUE);
        // Add required entity
        AppUser appUser;
        appUser = em.insert(AppUserResourceIT.createEntity(em)).block();
        placeholder.setOrganization(appUser);
        return placeholder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Placeholder createUpdatedEntity(EntityManager em) {
        Placeholder placeholder = new Placeholder().placeholderIndex(UPDATED_PLACEHOLDER_INDEX).placeholderValue(UPDATED_PLACEHOLDER_VALUE);
        // Add required entity
        AppUser appUser;
        appUser = em.insert(AppUserResourceIT.createUpdatedEntity(em)).block();
        placeholder.setOrganization(appUser);
        return placeholder;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Placeholder.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        AppUserResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        placeholderSearchRepository.deleteAll().block();
        assertThat(placeholderSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        placeholder = createEntity(em);
    }

    @Test
    void createPlaceholder() throws Exception {
        int databaseSizeBeforeCreate = placeholderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Placeholder testPlaceholder = placeholderList.get(placeholderList.size() - 1);
        assertThat(testPlaceholder.getPlaceholderIndex()).isEqualTo(DEFAULT_PLACEHOLDER_INDEX);
        assertThat(testPlaceholder.getPlaceholderValue()).isEqualTo(DEFAULT_PLACEHOLDER_VALUE);
    }

    @Test
    void createPlaceholderWithExistingId() throws Exception {
        // Create the Placeholder with an existing ID
        placeholder.setId(1L);
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        int databaseSizeBeforeCreate = placeholderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkPlaceholderIndexIsRequired() throws Exception {
        int databaseSizeBeforeTest = placeholderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        // set the field null
        placeholder.setPlaceholderIndex(null);

        // Create the Placeholder, which fails.
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPlaceholders() {
        // Initialize the database
        placeholderRepository.save(placeholder).block();

        // Get all the placeholderList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(placeholder.getId().intValue()))
            .jsonPath("$.[*].placeholderIndex")
            .value(hasItem(DEFAULT_PLACEHOLDER_INDEX))
            .jsonPath("$.[*].placeholderValue")
            .value(hasItem(DEFAULT_PLACEHOLDER_VALUE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlaceholdersWithEagerRelationshipsIsEnabled() {
        when(placeholderServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(placeholderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlaceholdersWithEagerRelationshipsIsNotEnabled() {
        when(placeholderServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(placeholderRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPlaceholder() {
        // Initialize the database
        placeholderRepository.save(placeholder).block();

        // Get the placeholder
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, placeholder.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(placeholder.getId().intValue()))
            .jsonPath("$.placeholderIndex")
            .value(is(DEFAULT_PLACEHOLDER_INDEX))
            .jsonPath("$.placeholderValue")
            .value(is(DEFAULT_PLACEHOLDER_VALUE));
    }

    @Test
    void getNonExistingPlaceholder() {
        // Get the placeholder
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPlaceholder() throws Exception {
        // Initialize the database
        placeholderRepository.save(placeholder).block();

        int databaseSizeBeforeUpdate = placeholderRepository.findAll().collectList().block().size();
        placeholderSearchRepository.save(placeholder).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());

        // Update the placeholder
        Placeholder updatedPlaceholder = placeholderRepository.findById(placeholder.getId()).block();
        updatedPlaceholder.placeholderIndex(UPDATED_PLACEHOLDER_INDEX).placeholderValue(UPDATED_PLACEHOLDER_VALUE);
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(updatedPlaceholder);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, placeholderDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        Placeholder testPlaceholder = placeholderList.get(placeholderList.size() - 1);
        assertThat(testPlaceholder.getPlaceholderIndex()).isEqualTo(UPDATED_PLACEHOLDER_INDEX);
        assertThat(testPlaceholder.getPlaceholderValue()).isEqualTo(UPDATED_PLACEHOLDER_VALUE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Placeholder> placeholderSearchList = IterableUtils.toList(placeholderSearchRepository.findAll().collectList().block());
                Placeholder testPlaceholderSearch = placeholderSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPlaceholderSearch.getPlaceholderIndex()).isEqualTo(UPDATED_PLACEHOLDER_INDEX);
                assertThat(testPlaceholderSearch.getPlaceholderValue()).isEqualTo(UPDATED_PLACEHOLDER_VALUE);
            });
    }

    @Test
    void putNonExistingPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, placeholderDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePlaceholderWithPatch() throws Exception {
        // Initialize the database
        placeholderRepository.save(placeholder).block();

        int databaseSizeBeforeUpdate = placeholderRepository.findAll().collectList().block().size();

        // Update the placeholder using partial update
        Placeholder partialUpdatedPlaceholder = new Placeholder();
        partialUpdatedPlaceholder.setId(placeholder.getId());

        partialUpdatedPlaceholder.placeholderIndex(UPDATED_PLACEHOLDER_INDEX);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlaceholder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlaceholder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        Placeholder testPlaceholder = placeholderList.get(placeholderList.size() - 1);
        assertThat(testPlaceholder.getPlaceholderIndex()).isEqualTo(UPDATED_PLACEHOLDER_INDEX);
        assertThat(testPlaceholder.getPlaceholderValue()).isEqualTo(DEFAULT_PLACEHOLDER_VALUE);
    }

    @Test
    void fullUpdatePlaceholderWithPatch() throws Exception {
        // Initialize the database
        placeholderRepository.save(placeholder).block();

        int databaseSizeBeforeUpdate = placeholderRepository.findAll().collectList().block().size();

        // Update the placeholder using partial update
        Placeholder partialUpdatedPlaceholder = new Placeholder();
        partialUpdatedPlaceholder.setId(placeholder.getId());

        partialUpdatedPlaceholder.placeholderIndex(UPDATED_PLACEHOLDER_INDEX).placeholderValue(UPDATED_PLACEHOLDER_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlaceholder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlaceholder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        Placeholder testPlaceholder = placeholderList.get(placeholderList.size() - 1);
        assertThat(testPlaceholder.getPlaceholderIndex()).isEqualTo(UPDATED_PLACEHOLDER_INDEX);
        assertThat(testPlaceholder.getPlaceholderValue()).isEqualTo(UPDATED_PLACEHOLDER_VALUE);
    }

    @Test
    void patchNonExistingPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, placeholderDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePlaceholder() {
        // Initialize the database
        placeholderRepository.save(placeholder).block();
        placeholderRepository.save(placeholder).block();
        placeholderSearchRepository.save(placeholder).block();

        int databaseSizeBeforeDelete = placeholderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the placeholder
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, placeholder.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Placeholder> placeholderList = placeholderRepository.findAll().collectList().block();
        assertThat(placeholderList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(placeholderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPlaceholder() {
        // Initialize the database
        placeholder = placeholderRepository.save(placeholder).block();
        placeholderSearchRepository.save(placeholder).block();

        // Search the placeholder
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + placeholder.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(placeholder.getId().intValue()))
            .jsonPath("$.[*].placeholderIndex")
            .value(hasItem(DEFAULT_PLACEHOLDER_INDEX))
            .jsonPath("$.[*].placeholderValue")
            .value(hasItem(DEFAULT_PLACEHOLDER_VALUE));
    }
}
