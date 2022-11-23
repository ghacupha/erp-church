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

import { getSession } from 'app/shared/reducers/authentication';
import { AppThunk } from 'app/config/store';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  successMessage: null,
  updateSuccess: false,
  updateFailure: false,
};

export type SettingsState = Readonly<typeof initialState>;

// Actions
const apiUrl = 'api/account';

export const saveAccountSettings: (account: any) => AppThunk = account => async dispatch => {
  await dispatch(updateAccount(account));

  dispatch(getSession());
};

export const updateAccount = createAsyncThunk('settings/update_account', async (account: any) => axios.post<any>(apiUrl, account), {
  serializeError: serializeAxiosError,
});

export const SettingsSlice = createSlice({
  name: 'settings',
  initialState: initialState as SettingsState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(updateAccount.pending, state => {
        state.loading = true;
        state.errorMessage = null;
        state.updateSuccess = false;
      })
      .addCase(updateAccount.rejected, state => {
        state.loading = false;
        state.updateSuccess = false;
        state.updateFailure = true;
      })
      .addCase(updateAccount.fulfilled, state => {
        state.loading = false;
        state.updateSuccess = true;
        state.updateFailure = false;
        state.successMessage = 'Settings saved!';
      });
  },
});

export const { reset } = SettingsSlice.actions;

// Reducer
export default SettingsSlice.reducer;
