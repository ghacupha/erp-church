package io.github.erp.repository;

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

import io.github.erp.domain.Placeholder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Placeholder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaceholderRepository extends ReactiveCrudRepository<Placeholder, Long>, PlaceholderRepositoryInternal {
    Flux<Placeholder> findAllBy(Pageable pageable);

    @Override
    Mono<Placeholder> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Placeholder> findAllWithEagerRelationships();

    @Override
    Flux<Placeholder> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM placeholder entity WHERE entity.archetype_id = :id")
    Flux<Placeholder> findByArchetype(Long id);

    @Query("SELECT * FROM placeholder entity WHERE entity.archetype_id IS NULL")
    Flux<Placeholder> findAllWhereArchetypeIsNull();

    @Query("SELECT * FROM placeholder entity WHERE entity.organization_id = :id")
    Flux<Placeholder> findByOrganization(Long id);

    @Query("SELECT * FROM placeholder entity WHERE entity.organization_id IS NULL")
    Flux<Placeholder> findAllWhereOrganizationIsNull();

    @Override
    <S extends Placeholder> Mono<S> save(S entity);

    @Override
    Flux<Placeholder> findAll();

    @Override
    Mono<Placeholder> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PlaceholderRepositoryInternal {
    <S extends Placeholder> Mono<S> save(S entity);

    Flux<Placeholder> findAllBy(Pageable pageable);

    Flux<Placeholder> findAll();

    Mono<Placeholder> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Placeholder> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Placeholder> findOneWithEagerRelationships(Long id);

    Flux<Placeholder> findAllWithEagerRelationships();

    Flux<Placeholder> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
