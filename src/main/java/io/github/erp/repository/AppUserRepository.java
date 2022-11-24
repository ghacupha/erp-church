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

import io.github.erp.domain.AppUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AppUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppUserRepository extends ReactiveCrudRepository<AppUser, Long>, AppUserRepositoryInternal {
    Flux<AppUser> findAllBy(Pageable pageable);

    @Override
    Mono<AppUser> findOneWithEagerRelationships(Long id);

    @Override
    Flux<AppUser> findAllWithEagerRelationships();

    @Override
    Flux<AppUser> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM app_user entity WHERE entity.organization_id = :id")
    Flux<AppUser> findByOrganization(Long id);

    @Query("SELECT * FROM app_user entity WHERE entity.organization_id IS NULL")
    Flux<AppUser> findAllWhereOrganizationIsNull();

    @Query("SELECT * FROM app_user entity WHERE entity.system_user_id = :id")
    Flux<AppUser> findBySystemUser(Long id);

    @Query("SELECT * FROM app_user entity WHERE entity.system_user_id IS NULL")
    Flux<AppUser> findAllWhereSystemUserIsNull();

    @Override
    <S extends AppUser> Mono<S> save(S entity);

    @Override
    Flux<AppUser> findAll();

    @Override
    Mono<AppUser> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AppUserRepositoryInternal {
    <S extends AppUser> Mono<S> save(S entity);

    Flux<AppUser> findAllBy(Pageable pageable);

    Flux<AppUser> findAll();

    Mono<AppUser> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AppUser> findAllBy(Pageable pageable, Criteria criteria);

    Mono<AppUser> findOneWithEagerRelationships(Long id);

    Flux<AppUser> findAllWithEagerRelationships();

    Flux<AppUser> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
