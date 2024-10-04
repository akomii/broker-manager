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
 * Enumerates the possible states of a {@link ManagerRequest}. These states represent the lifecycle of a request within the system, typically
 * transitioning as follows:
 * <ol>
 *   <li><strong>DRAFT</strong> - Indicates that the request is a work in progress. All aspects of the request are editable. It can be used to create other drafts or be transitioned to the {@code ACTIVE} state for publication.</li>
 *   <li><strong>ACTIVE</strong> - The request is active and its executions are published to targeted {@link ManagerNode}s. Some minor content like tags are still be editable.</li>
 *   <li><strong>CLOSED</strong> - The request and all of its associated {@link RequestExecution}s are closed. New executions cannot be created, and the content is locked from further edits. All executions of this request are set to the state {@link ExecutionState#CLOSED}.</li>
 *   <li><strong>ARCHIVED</strong> - The request and all associated {@link RequestExecution}s have been archived for long-term storage.  All executions of this request are set to the state {@link ExecutionState#DELETED}.</li>
 * </ol>
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public enum RequestState {

  DRAFT,
  ACTIVE,
  CLOSED,
  ARCHIVED;
}

