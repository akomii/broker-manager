/*
 * Copyright (c) 2024 AKTIN
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.aktin.broker.manager.enums;

import org.aktin.broker.manager.models.ManagerRequest;

/**
 * Enumerates the potential states of a {@link ManagerRequest}. States typically progress from
 * <code>DRAFT</code> => <code>ONLINE</code> => <code>CLOSED</code> => <code>ARCHIVED</code>.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public enum RequestState {
  /**
   * The request is a work in progress. All contents of the request can be changed. It can be used to create other drafts or be set in the
   * <code>ONLINE</code> state.
   */
  DRAFT,
  /**
   * The request is active and its content is not editable anymore.
   */
  ONLINE,
  /**
   * The request and all of its executions are closed. Executions cannot receive results anymore.
   */
  CLOSED,
  /**
   * The request and all of its executions have been moved to a long-term archive for historical or auditing purposes.
   */
  ARCHIVED
}
