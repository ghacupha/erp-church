///
/// Erp Church - Data management for religious institutions
/// Copyright © 2022 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import thunk from 'redux-thunk';
import axios from 'axios';
import sinon from 'sinon';
import configureStore from 'redux-mock-store';

import password, { savePassword, reset } from './password.reducer';

describe('Password reducer tests', () => {
  describe('Common tests', () => {
    it('should return the initial state', () => {
      const toTest = password(undefined, { type: '' });
      expect(toTest).toMatchObject({
        loading: false,
        errorMessage: null,
        updateSuccess: false,
        updateFailure: false,
      });
    });
  });

  describe('Password update', () => {
    it('should detect a request', () => {
      const toTest = password(undefined, { type: savePassword.pending.type });
      expect(toTest).toMatchObject({
        updateSuccess: false,
        updateFailure: false,
        loading: true,
      });
    });
    it('should detect a success', () => {
      const toTest = password(undefined, { type: savePassword.fulfilled.type });
      expect(toTest).toMatchObject({
        updateSuccess: true,
        updateFailure: false,
        loading: false,
      });
    });
    it('should detect a failure', () => {
      const toTest = password(undefined, { type: savePassword.rejected.type });
      expect(toTest).toMatchObject({
        updateSuccess: false,
        updateFailure: true,
        loading: false,
      });
    });

    it('should reset the state', () => {
      const initialState = {
        loading: false,
        errorMessage: null,
        successMessage: null,
        updateSuccess: false,
        updateFailure: false,
      };
      expect(password({ ...initialState, loading: true }, reset)).toEqual({
        ...initialState,
      });
    });
  });

  describe('Actions', () => {
    let store;

    const resolvedObject = { value: 'whatever' };
    beforeEach(() => {
      const mockStore = configureStore([thunk]);
      store = mockStore({});
      axios.post = sinon.stub().returns(Promise.resolve(resolvedObject));
    });

    it('dispatches UPDATE_PASSWORD_PENDING and UPDATE_PASSWORD_FULFILLED actions', async () => {
      const expectedActions = [
        {
          type: savePassword.pending.type,
        },
        {
          type: savePassword.fulfilled.type,
          payload: resolvedObject,
        },
      ];
      await store.dispatch(savePassword({ currentPassword: '', newPassword: '' }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
    });
    it('dispatches RESET actions', async () => {
      await store.dispatch(reset());
      expect(store.getActions()[0]).toMatchObject(reset());
    });
  });
});
