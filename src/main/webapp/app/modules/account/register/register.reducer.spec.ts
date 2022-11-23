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

import register, { handleRegister, reset } from './register.reducer';

describe('Creating account tests', () => {
  const initialState = {
    loading: false,
    registrationSuccess: false,
    registrationFailure: false,
    errorMessage: null,
    successMessage: null,
  };

  it('should return the initial state', () => {
    expect(register(undefined, { type: '' })).toEqual({
      ...initialState,
    });
  });

  it('should detect a request', () => {
    expect(register(undefined, { type: handleRegister.pending.type })).toEqual({
      ...initialState,
      loading: true,
    });
  });

  it('should handle RESET', () => {
    expect(
      register({ loading: true, registrationSuccess: true, registrationFailure: true, errorMessage: '', successMessage: '' }, reset())
    ).toEqual({
      ...initialState,
    });
  });

  it('should handle CREATE_ACCOUNT success', () => {
    expect(
      register(undefined, {
        type: handleRegister.fulfilled.type,
        payload: 'fake payload',
      })
    ).toEqual({
      ...initialState,
      registrationSuccess: true,
      successMessage: 'Registration saved! Please check your email for confirmation.',
    });
  });

  it('should handle CREATE_ACCOUNT failure', () => {
    const error = { message: 'fake error' };
    expect(
      register(undefined, {
        type: handleRegister.rejected.type,
        error,
      })
    ).toEqual({
      ...initialState,
      registrationFailure: true,
      errorMessage: error.message,
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

    it('dispatches CREATE_ACCOUNT_PENDING and CREATE_ACCOUNT_FULFILLED actions', async () => {
      const expectedActions = [
        {
          type: handleRegister.pending.type,
        },
        {
          type: handleRegister.fulfilled.type,
          payload: resolvedObject,
        },
      ];
      await store.dispatch(handleRegister({ login: '', email: '', password: '' }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
    });
    it('dispatches RESET actions', async () => {
      await store.dispatch(reset());
      expect(store.getActions()[0]).toMatchObject(reset());
    });
  });
});
