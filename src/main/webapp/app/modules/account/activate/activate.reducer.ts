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
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';

import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  activationSuccess: false,
  activationFailure: false,
};

export type ActivateState = Readonly<typeof initialState>;

// Actions

export const activateAction = createAsyncThunk('activate/activate_account', async (key: string) => axios.get(`api/activate?key=${key}`), {
  serializeError: serializeAxiosError,
});

export const ActivateSlice = createSlice({
  name: 'activate',
  initialState: initialState as ActivateState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(activateAction.pending, () => initialState)
      .addCase(activateAction.rejected, state => {
        state.activationFailure = true;
      })
      .addCase(activateAction.fulfilled, state => {
        state.activationSuccess = true;
      });
  },
});

export const { reset } = ActivateSlice.actions;

// Reducer
export default ActivateSlice.reducer;
