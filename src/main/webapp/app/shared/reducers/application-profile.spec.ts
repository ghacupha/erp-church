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

import profile, { getProfile } from './application-profile';

describe('Profile reducer tests', () => {
  const initialState = {
    ribbonEnv: '',
    inProduction: true,
    isOpenAPIEnabled: false,
  };
  describe('Common tests', () => {
    it('should return the initial state', () => {
      const toTest = profile(undefined, { type: '' });
      expect(toTest).toEqual(initialState);
    });

    it('should return the right payload in prod', () => {
      const payload = {
        data: {
          'display-ribbon-on-profiles': 'awesome ribbon stuff',
          activeProfiles: ['prod'],
        },
      };

      expect(profile(undefined, { type: getProfile.fulfilled.type, payload })).toEqual({
        ribbonEnv: 'awesome ribbon stuff',
        inProduction: true,
        isOpenAPIEnabled: false,
      });
    });

    it('should return the right payload in dev with OpenAPI enabled', () => {
      const payload = {
        data: {
          'display-ribbon-on-profiles': 'awesome ribbon stuff',
          activeProfiles: ['api-docs', 'dev'],
        },
      };

      expect(profile(undefined, { type: getProfile.fulfilled.type, payload })).toEqual({
        ribbonEnv: 'awesome ribbon stuff',
        inProduction: false,
        isOpenAPIEnabled: true,
      });
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

    it('dispatches GET_SESSION_PENDING and GET_SESSION_FULFILLED actions', async () => {
      const expectedActions = [
        {
          type: getProfile.pending.type,
        },
        {
          type: getProfile.fulfilled.type,
          payload: resolvedObject,
        },
      ];
      await store.dispatch(getProfile());
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
    });
  });
});
