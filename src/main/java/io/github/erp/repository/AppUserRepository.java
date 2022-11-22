package io.github.erp.repository;

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

    @Query("SELECT * FROM app_user entity WHERE entity.system_user_id = :id")
    Flux<AppUser> findBySystemUser(Long id);

    @Query("SELECT * FROM app_user entity WHERE entity.system_user_id IS NULL")
    Flux<AppUser> findAllWhereSystemUserIsNull();

    @Query(
        "SELECT entity.* FROM app_user entity JOIN rel_app_user__placeholder joinTable ON entity.id = joinTable.placeholder_id WHERE joinTable.placeholder_id = :id"
    )
    Flux<AppUser> findByPlaceholder(Long id);

    @Query("SELECT * FROM app_user entity WHERE entity.organization_id = :id")
    Flux<AppUser> findByOrganization(Long id);

    @Query("SELECT * FROM app_user entity WHERE entity.organization_id IS NULL")
    Flux<AppUser> findAllWhereOrganizationIsNull();

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
