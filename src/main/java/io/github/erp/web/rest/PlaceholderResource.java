package io.github.erp.web.rest;

import io.github.erp.repository.PlaceholderRepository;
import io.github.erp.service.PlaceholderService;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link io.github.erp.domain.Placeholder}.
 */
@RestController
@RequestMapping("/api")
public class PlaceholderResource {

    private final Logger log = LoggerFactory.getLogger(PlaceholderResource.class);

    private static final String ENTITY_NAME = "placeholder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaceholderService placeholderService;

    private final PlaceholderRepository placeholderRepository;

    public PlaceholderResource(PlaceholderService placeholderService, PlaceholderRepository placeholderRepository) {
        this.placeholderService = placeholderService;
        this.placeholderRepository = placeholderRepository;
    }

    /**
     * {@code POST  /placeholders} : Create a new placeholder.
     *
     * @param placeholderDTO the placeholderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new placeholderDTO, or with status {@code 400 (Bad Request)} if the placeholder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/placeholders")
    public Mono<ResponseEntity<PlaceholderDTO>> createPlaceholder(@Valid @RequestBody PlaceholderDTO placeholderDTO)
        throws URISyntaxException {
        log.debug("REST request to save Placeholder : {}", placeholderDTO);
        if (placeholderDTO.getId() != null) {
            throw new BadRequestAlertException("A new placeholder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return placeholderService
            .save(placeholderDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/placeholders/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /placeholders/:id} : Updates an existing placeholder.
     *
     * @param id the id of the placeholderDTO to save.
     * @param placeholderDTO the placeholderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated placeholderDTO,
     * or with status {@code 400 (Bad Request)} if the placeholderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the placeholderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/placeholders/{id}")
    public Mono<ResponseEntity<PlaceholderDTO>> updatePlaceholder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlaceholderDTO placeholderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Placeholder : {}, {}", id, placeholderDTO);
        if (placeholderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, placeholderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return placeholderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return placeholderService
                    .update(placeholderDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /placeholders/:id} : Partial updates given fields of an existing placeholder, field will ignore if it is null
     *
     * @param id the id of the placeholderDTO to save.
     * @param placeholderDTO the placeholderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated placeholderDTO,
     * or with status {@code 400 (Bad Request)} if the placeholderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the placeholderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the placeholderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/placeholders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PlaceholderDTO>> partialUpdatePlaceholder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlaceholderDTO placeholderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Placeholder partially : {}, {}", id, placeholderDTO);
        if (placeholderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, placeholderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return placeholderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PlaceholderDTO> result = placeholderService.partialUpdate(placeholderDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /placeholders} : get all the placeholders.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of placeholders in body.
     */
    @GetMapping("/placeholders")
    public Mono<ResponseEntity<List<PlaceholderDTO>>> getAllPlaceholders(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Placeholders");
        return placeholderService
            .countAll()
            .zipWith(placeholderService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /placeholders/:id} : get the "id" placeholder.
     *
     * @param id the id of the placeholderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the placeholderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/placeholders/{id}")
    public Mono<ResponseEntity<PlaceholderDTO>> getPlaceholder(@PathVariable Long id) {
        log.debug("REST request to get Placeholder : {}", id);
        Mono<PlaceholderDTO> placeholderDTO = placeholderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(placeholderDTO);
    }

    /**
     * {@code DELETE  /placeholders/:id} : delete the "id" placeholder.
     *
     * @param id the id of the placeholderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/placeholders/{id}")
    public Mono<ResponseEntity<Void>> deletePlaceholder(@PathVariable Long id) {
        log.debug("REST request to delete Placeholder : {}", id);
        return placeholderService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    /**
     * {@code SEARCH  /_search/placeholders?query=:query} : search for the placeholder corresponding
     * to the query.
     *
     * @param query the query of the placeholder search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search/placeholders")
    public Mono<ResponseEntity<Flux<PlaceholderDTO>>> searchPlaceholders(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of Placeholders for query {}", query);
        return placeholderService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(UriComponentsBuilder.fromHttpRequest(request), page))
            .map(headers -> ResponseEntity.ok().headers(headers).body(placeholderService.search(query, pageable)));
    }
}
