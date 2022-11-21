import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Placeholder from './placeholder';
import PlaceholderDetail from './placeholder-detail';
import PlaceholderUpdate from './placeholder-update';
import PlaceholderDeleteDialog from './placeholder-delete-dialog';

const PlaceholderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Placeholder />} />
    <Route path="new" element={<PlaceholderUpdate />} />
    <Route path=":id">
      <Route index element={<PlaceholderDetail />} />
      <Route path="edit" element={<PlaceholderUpdate />} />
      <Route path="delete" element={<PlaceholderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PlaceholderRoutes;
