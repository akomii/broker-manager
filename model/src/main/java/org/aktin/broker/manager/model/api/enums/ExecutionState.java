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
import org.aktin.broker.manager.model.api.models.RequestExecution;

/**
 * Represents the possible states of a {@link RequestExecution}. The states typically transition in the following order:
 * <ul>
 *   <li><code>PENDING</code></li>
 *   <li><code>PUBLISHED</code></li>
 *   <li><code>CLOSED</code></li>
 *   <li><code>DELETED</code></li>
 * </ul>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public enum ExecutionState {
  /**
   * The execution has been created and is waiting for publishing. The content is fully editable.
   */
  PENDING,
  /**
   * The execution has been published on the AKTIN Broker and sent to corresponding {@link ManagerNode}s. Results may be available. Content is not
   * editable anymore.
   */
  PUBLISHED,
  /**
   * The execution has been closed on the AKTIN Broker and results can no longer be contributed. Content is not editable anymore.
   */
  CLOSED,
  /**
   * The execution and corresponding results have been deleted from the AKTIN Broker and are no longer available.
   */
  DELETED;
}
