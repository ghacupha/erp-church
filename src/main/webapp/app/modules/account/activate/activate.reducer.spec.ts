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

import activate, { activateAction, reset } from './activate.reducer';

describe('Activate reducer tests', () => {
  it('should return the initial state', () => {
    expect(activate(undefined, { type: '' })).toMatchObject({
      activationSuccess: false,
      activationFailure: false,
    });
  });

  it('should reset', () => {
    expect(activate({ activationSuccess: true, activationFailure: false }, reset)).toMatchObject({
      activationSuccess: false,
      activationFailure: false,
    });
  });

  it('should detect a success', () => {
    expect(activate(undefined, { type: activateAction.fulfilled.type })).toMatchObject({
      activationSuccess: true,
      activationFailure: false,
    });
  });

  it('should return the same state on request', () => {
    expect(activate(undefined, { type: activateAction.pending.type })).toMatchObject({
      activationSuccess: false,
      activationFailure: false,
    });
  });

  it('should detect a failure', () => {
    expect(activate(undefined, { type: activateAction.rejected.type })).toMatchObject({
      activationSuccess: false,
      activationFailure: true,
    });
  });

  it('should reset the state', () => {
    const initialState = {
      activationSuccess: false,
      activationFailure: false,
    };
    expect(activate({ activationSuccess: true, activationFailure: true }, reset)).toEqual({
      ...initialState,
    });
  });

  describe('Actions', () => {
    let store;

    const resolvedObject = { value: 'whatever' };
    beforeEach(() => {
      const mockStore = configureStore([thunk]);
      store = mockStore({});
      axios.get = sinon.stub().returns(Promise.resolve(resolvedObject));
    });

    it('dispatches ACTIVATE_ACCOUNT_PENDING and ACTIVATE_ACCOUNT_FULFILLED actions', async () => {
      const expectedActions = [
        {
          type: activateAction.pending.type,
        },
        {
          type: activateAction.fulfilled.type,
          payload: resolvedObject,
        },
      ];
      await store.dispatch(activateAction(''));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
    });
    it('dispatches RESET actions', async () => {
      await store.dispatch(reset());
      expect(store.getActions()[0]).toMatchObject(reset());
    });
  });
});
