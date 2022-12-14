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

import static org.springframework.data.relational.core.query.Criteria.where;

import io.github.erp.domain.Placeholder;
import io.github.erp.repository.rowmapper.AppUserRowMapper;
import io.github.erp.repository.rowmapper.PlaceholderRowMapper;
import io.github.erp.repository.rowmapper.PlaceholderRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Placeholder entity.
 */
@SuppressWarnings("unused")
class PlaceholderRepositoryInternalImpl extends SimpleR2dbcRepository<Placeholder, Long> implements PlaceholderRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PlaceholderRowMapper placeholderMapper;
    private final AppUserRowMapper appuserMapper;

    private static final Table entityTable = Table.aliased("placeholder", EntityManager.ENTITY_ALIAS);
    private static final Table archetypeTable = Table.aliased("placeholder", "archetype");
    private static final Table organizationTable = Table.aliased("app_user", "e_organization");

    public PlaceholderRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PlaceholderRowMapper placeholderMapper,
        AppUserRowMapper appuserMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Placeholder.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.placeholderMapper = placeholderMapper;
        this.appuserMapper = appuserMapper;
    }

    @Override
    public Flux<Placeholder> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Placeholder> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PlaceholderSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PlaceholderSqlHelper.getColumns(archetypeTable, "archetype"));
        columns.addAll(AppUserSqlHelper.getColumns(organizationTable, "organization"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(archetypeTable)
            .on(Column.create("archetype_id", entityTable))
            .equals(Column.create("id", archetypeTable))
            .leftOuterJoin(organizationTable)
            .on(Column.create("organization_id", entityTable))
            .equals(Column.create("id", organizationTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Placeholder.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Placeholder> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Placeholder> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Placeholder> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Placeholder> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Placeholder> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Placeholder process(Row row, RowMetadata metadata) {
        Placeholder entity = placeholderMapper.apply(row, "e");
        entity.setArchetype(placeholderMapper.apply(row, "archetype"));
        entity.setOrganization(appuserMapper.apply(row, "organization"));
        return entity;
    }

    @Override
    public <S extends Placeholder> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
