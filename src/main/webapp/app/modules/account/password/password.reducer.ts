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
  loading: false,
  errorMessage: null,
  successMessage: null,
  updateSuccess: false,
  updateFailure: false,
};

export type PasswordState = Readonly<typeof initialState>;

const apiUrl = 'api/account';

interface IPassword {
  currentPassword: string;
  newPassword: string;
}

// Actions

export const savePassword = createAsyncThunk(
  'password/update_password',
  async (password: IPassword) => axios.post(`${apiUrl}/change-password`, password),
  { serializeError: serializeAxiosError }
);

export const PasswordSlice = createSlice({
  name: 'password',
  initialState: initialState as PasswordState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(savePassword.pending, state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addCase(savePassword.rejected, state => {
        state.loading = false;
        state.updateSuccess = false;
        state.updateFailure = true;
        state.errorMessage = 'An error has occurred! The password could not be changed.';
      })
      .addCase(savePassword.fulfilled, state => {
        state.loading = false;
        state.updateSuccess = true;
        state.updateFailure = false;
        state.successMessage = 'Password changed!';
      });
  },
});

export const { reset } = PasswordSlice.actions;

// Reducer
export default PasswordSlice.reducer;
