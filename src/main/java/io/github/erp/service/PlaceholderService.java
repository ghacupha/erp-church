package io.github.erp.service;

import io.github.erp.service.dto.PlaceholderDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link io.github.erp.domain.Placeholder}.
 */
public interface PlaceholderService {
    /**
     * Save a placeholder.
     *
     * @param placeholderDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PlaceholderDTO> save(PlaceholderDTO placeholderDTO);

    /**
     * Updates a placeholder.
     *
     * @param placeholderDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PlaceholderDTO> update(PlaceholderDTO placeholderDTO);

    /**
     * Partially updates a placeholder.
     *
     * @param placeholderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PlaceholderDTO> partialUpdate(PlaceholderDTO placeholderDTO);

    /**
     * Get all the placeholders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PlaceholderDTO> findAll(Pageable pageable);

    /**
     * Get all the placeholders with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PlaceholderDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of placeholders available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of placeholders available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" placeholder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PlaceholderDTO> findOne(Long id);

    /**
     * Delete the "id" placeholder.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the placeholder corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PlaceholderDTO> search(String query, Pageable pageable);
}
