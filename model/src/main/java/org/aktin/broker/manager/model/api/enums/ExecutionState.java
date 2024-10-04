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
 * Enumerates the possible states of a {@link RequestExecution}. These states represent the lifecycle of an execution, typically transitioning as
 * follows:
 * <ol>
 *   <li><strong>PENDING</strong> - The execution has been created and is awaiting publication. The content is fully editable.</li>
 *   <li><strong>PUBLISHED</strong> - The execution has been published via the AKTIN Broker and distributed to the corresponding {@link ManagerNode}s. Results may be available. The content is no longer editable.</li>
 *   <li><strong>CLOSED</strong> - The execution has been closed on the AKTIN Broker. Results can no longer be contributed. The content remains non-editable.</li>
 *   <li><strong>DELETED</strong> - The execution and its corresponding results have been deleted from the AKTIN Broker. They are no longer available within the system..</li>
 * </ol>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public enum ExecutionState {

  PENDING,
  PUBLISHED,
  CLOSED,
  DELETED;
}