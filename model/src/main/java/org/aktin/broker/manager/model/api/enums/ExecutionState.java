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
 * Enumerates the potential states of a {@link RequestExecution}. States typically progress from
 * <code>PENDING</code> => <code>PUBLISHED</code> => <code>CLOSED</code> => <code>ARCHIVED</code>.
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
   * The execution has been sent to corresponding {@link ManagerNode}. Results may be available. Content is not editable anymore.
   */
  PUBLISHED,
  /**
   * The execution has been closed and results can no longer be contributed. Content is not editable anymore.
   */
  CLOSED,
  /**
   * The execution has been moved to long-term storage. The corresponding results have been deleted and are no longer available.
   */
  ARCHIVED;
}
