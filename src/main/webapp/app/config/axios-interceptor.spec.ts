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

import axios from 'axios';
import sinon from 'sinon';

import setupAxiosInterceptors from './axios-interceptor';

describe('Axios Interceptor', () => {
  describe('setupAxiosInterceptors', () => {
    const client = axios;
    const onUnauthenticated = sinon.spy();
    setupAxiosInterceptors(onUnauthenticated);

    it('onRequestSuccess is called on fulfilled request', () => {
      expect((client.interceptors.request as any).handlers[0].fulfilled({ data: 'foo', url: '/test' })).toMatchObject({
        data: 'foo',
      });
    });
    it('onResponseSuccess is called on fulfilled response', () => {
      expect((client.interceptors.response as any).handlers[0].fulfilled({ data: 'foo' })).toEqual({ data: 'foo' });
    });
    it('onResponseError is called on rejected response', () => {
      const rejectError = {
        response: {
          statusText: 'NotFound',
          status: 403,
          data: { message: 'Page not found' },
        },
      };
      expect((client.interceptors.response as any).handlers[0].rejected(rejectError)).rejects.toEqual(rejectError);
      expect(onUnauthenticated.calledOnce).toBe(true);
    });
  });
});
