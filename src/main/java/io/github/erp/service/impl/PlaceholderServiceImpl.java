package io.github.erp.service.impl;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.Placeholder;
import io.github.erp.repository.PlaceholderRepository;
import io.github.erp.repository.search.PlaceholderSearchRepository;
import io.github.erp.service.PlaceholderService;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.mapper.PlaceholderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Placeholder}.
 */
@Service
@Transactional
public class PlaceholderServiceImpl implements PlaceholderService {

    private final Logger log = LoggerFactory.getLogger(PlaceholderServiceImpl.class);

    private final PlaceholderRepository placeholderRepository;

    private final PlaceholderMapper placeholderMapper;

    private final PlaceholderSearchRepository placeholderSearchRepository;

    public PlaceholderServiceImpl(
        PlaceholderRepository placeholderRepository,
        PlaceholderMapper placeholderMapper,
        PlaceholderSearchRepository placeholderSearchRepository
    ) {
        this.placeholderRepository = placeholderRepository;
        this.placeholderMapper = placeholderMapper;
        this.placeholderSearchRepository = placeholderSearchRepository;
    }

    @Override
    public Mono<PlaceholderDTO> save(PlaceholderDTO placeholderDTO) {
        log.debug("Request to save Placeholder : {}", placeholderDTO);
        return placeholderRepository
            .save(placeholderMapper.toEntity(placeholderDTO))
            .flatMap(placeholderSearchRepository::save)
            .map(placeholderMapper::toDto);
    }

    @Override
    public Mono<PlaceholderDTO> update(PlaceholderDTO placeholderDTO) {
        log.debug("Request to update Placeholder : {}", placeholderDTO);
        return placeholderRepository
            .save(placeholderMapper.toEntity(placeholderDTO))
            .flatMap(placeholderSearchRepository::save)
            .map(placeholderMapper::toDto);
    }

    @Override
    public Mono<PlaceholderDTO> partialUpdate(PlaceholderDTO placeholderDTO) {
        log.debug("Request to partially update Placeholder : {}", placeholderDTO);

        return placeholderRepository
            .findById(placeholderDTO.getId())
            .map(existingPlaceholder -> {
                placeholderMapper.partialUpdate(existingPlaceholder, placeholderDTO);

                return existingPlaceholder;
            })
            .flatMap(placeholderRepository::save)
            .flatMap(savedPlaceholder -> {
                placeholderSearchRepository.save(savedPlaceholder);

                return Mono.just(savedPlaceholder);
            })
            .map(placeholderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PlaceholderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Placeholders");
        return placeholderRepository.findAllBy(pageable).map(placeholderMapper::toDto);
    }

    public Flux<PlaceholderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return placeholderRepository.findAllWithEagerRelationships(pageable).map(placeholderMapper::toDto);
    }

    public Mono<Long> countAll() {
        return placeholderRepository.count();
    }

    public Mono<Long> searchCount() {
        return placeholderSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PlaceholderDTO> findOne(Long id) {
        log.debug("Request to get Placeholder : {}", id);
        return placeholderRepository.findOneWithEagerRelationships(id).map(placeholderMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Placeholder : {}", id);
        return placeholderRepository.deleteById(id).then(placeholderSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PlaceholderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Placeholders for query {}", query);
        return placeholderSearchRepository.search(query, pageable).map(placeholderMapper::toDto);
    }
}
