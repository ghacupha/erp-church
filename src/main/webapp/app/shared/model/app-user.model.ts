import { IUser } from 'app/shared/model/user.model';
import { IPlaceholder } from 'app/shared/model/placeholder.model';

export interface IAppUser {
  id?: number;
  designation?: string;
  systemUser?: IUser;
  placeholders?: IPlaceholder[] | null;
  organization?: IAppUser | null;
}

export const defaultValue: Readonly<IAppUser> = {};
