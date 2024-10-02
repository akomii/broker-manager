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

package org.aktin.broker.manager.model.api.enums;

import org.aktin.broker.manager.model.api.models.ManagerNode;
import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.aktin.broker.manager.model.api.models.RequestExecution;

/**
 * Represents the possible states of a {@link ManagerRequest}. The states typically transition in the following order:
 * <ul>
 *   <li><code>DRAFT</code></li>
 *   <li><code>ACTIVE</code></li>
 *   <li><code>CLOSED</code></li>
 *   <li><code>ARCHIVED</code></li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public enum RequestState {
  /**
   * The request is a work in progress. All contents of the request can be changed. It can be used to create other drafts or be published in the
   * <code>ACTIVE</code> state.
   */
  DRAFT,
  /**
   * The request is active and its executions are published to {@link ManagerNode}s. Some minor content is still editable.
   */
  ACTIVE,
  /**
   * The request and all of its {@link RequestExecution}s are closed. New executions cannot be created. The content cannot be edited. All executions of this request are set to the state {@link ExecutionState#CLOSED}.
   */
  CLOSED,
  /**
   * The request and all of its {@link RequestExecution}s have been moved to a long-term archive for historical or auditing purposes. All executions of this request are set to the state {@link ExecutionState#DELETED}.
   */
  ARCHIVED
}
