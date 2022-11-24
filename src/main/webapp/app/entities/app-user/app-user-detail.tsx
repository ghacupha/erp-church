import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './app-user.reducer';

export const AppUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const appUserEntity = useAppSelector(state => state.appUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="appUserDetailsHeading">App User</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{appUserEntity.id}</dd>
          <dt>
            <span id="designation">Designation</span>
          </dt>
          <dd>{appUserEntity.designation}</dd>
          <dt>
            <span id="identifier">Identifier</span>
          </dt>
          <dd>{appUserEntity.identifier}</dd>
          <dt>
            <span id="isCorporateAccount">Is Corporate Account</span>
          </dt>
          <dd>{appUserEntity.isCorporateAccount ? 'true' : 'false'}</dd>
          <dt>Organization</dt>
          <dd>{appUserEntity.organization ? appUserEntity.organization.designation : ''}</dd>
          <dt>System User</dt>
          <dd>{appUserEntity.systemUser ? appUserEntity.systemUser.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/app-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/app-user/${appUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AppUserDetail;
