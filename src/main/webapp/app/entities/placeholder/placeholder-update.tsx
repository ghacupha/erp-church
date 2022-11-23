import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPlaceholders } from 'app/entities/placeholder/placeholder.reducer';
import { IAppUser } from 'app/shared/model/app-user.model';
import { getEntities as getAppUsers } from 'app/entities/app-user/app-user.reducer';
import { IPlaceholder } from 'app/shared/model/placeholder.model';
import { getEntity, updateEntity, createEntity, reset } from './placeholder.reducer';

export const PlaceholderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const placeholders = useAppSelector(state => state.placeholder.entities);
  const appUsers = useAppSelector(state => state.appUser.entities);
  const placeholderEntity = useAppSelector(state => state.placeholder.entity);
  const loading = useAppSelector(state => state.placeholder.loading);
  const updating = useAppSelector(state => state.placeholder.updating);
  const updateSuccess = useAppSelector(state => state.placeholder.updateSuccess);

  const handleClose = () => {
    navigate('/placeholder' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPlaceholders({}));
    dispatch(getAppUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...placeholderEntity,
      ...values,
      archetype: placeholders.find(it => it.id.toString() === values.archetype.toString()),
      organization: appUsers.find(it => it.id.toString() === values.organization.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...placeholderEntity,
          archetype: placeholderEntity?.archetype?.id,
          organization: placeholderEntity?.organization?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="erpChurchApp.placeholder.home.createOrEditLabel" data-cy="PlaceholderCreateUpdateHeading">
            Create or edit a Placeholder
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="placeholder-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Placeholder Index"
                id="placeholder-placeholderIndex"
                name="placeholderIndex"
                data-cy="placeholderIndex"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Placeholder Value"
                id="placeholder-placeholderValue"
                name="placeholderValue"
                data-cy="placeholderValue"
                type="text"
              />
              <ValidatedField id="placeholder-archetype" name="archetype" data-cy="archetype" label="Archetype" type="select">
                <option value="" key="0" />
                {placeholders
                  ? placeholders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.placeholderValue}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="placeholder-organization"
                name="organization"
                data-cy="organization"
                label="Organization"
                type="select"
                required
              >
                <option value="" key="0" />
                {appUsers
                  ? appUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.designation}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/placeholder" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PlaceholderUpdate;
