/*
 * Erp Church - Data management for religious institutions
 * Copyright © 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
const path = require('path');

const tsconfig = require('../tsconfig.json');

module.exports = {
  root,
  mapTypescriptAliasToWebpackAlias,
};

const _root = path.resolve(__dirname, '..');

function root(args) {
  args = Array.prototype.slice.call(arguments, 0);
  return path.join.apply(path, [_root].concat(args));
}

function mapTypescriptAliasToWebpackAlias(alias = {}) {
  const webpackAliases = { ...alias };
  if (!tsconfig.compilerOptions.paths) {
    return webpackAliases;
  }
  Object.entries(tsconfig.compilerOptions.paths)
    .filter(([key, value]) => {
      // use Typescript alias in Webpack only if this has value
      return !!value.length;
    })
    .map(([key, value]) => {
      // if Typescript alias ends with /* then remove this for Webpack
      const regexToReplace = /\/\*$/;
      const aliasKey = key.replace(regexToReplace, '');
      const aliasValue = value[0].replace(regexToReplace, '');
      return [aliasKey, root(aliasValue)];
    })
    .reduce((aliases, [key, value]) => {
      aliases[key] = value;
      return aliases;
    }, webpackAliases);
  return webpackAliases;
}
