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

package org.aktin.broker.manager.service.impl.util;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Set;
import org.aktin.broker.manager.model.api.enums.ExecutionState;
import org.aktin.broker.manager.model.api.models.RequestExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionStateManager {

  private static final Logger log = LoggerFactory.getLogger(ExecutionStateManager.class);
  private static final EnumMap<ExecutionState, Set<ExecutionState>> VALID_TRANSITIONS = new EnumMap<>(ExecutionState.class);

  static {
    VALID_TRANSITIONS.put(ExecutionState.PENDING, Set.of(ExecutionState.PUBLISHED));
    VALID_TRANSITIONS.put(ExecutionState.PUBLISHED, Set.of(ExecutionState.CLOSED, ExecutionState.ARCHIVED));
    VALID_TRANSITIONS.put(ExecutionState.CLOSED, Set.of(ExecutionState.ARCHIVED));
    VALID_TRANSITIONS.put(ExecutionState.ARCHIVED, Set.of());
  }

  private ExecutionStateManager() {
  }

  public static void setToPublished(int requestId, RequestExecution execution) {
    setExecutionState(requestId, execution, ExecutionState.PUBLISHED);
    execution.setPublishedDate(Instant.now());
  }

  public static void setToClosed(int requestId, RequestExecution execution) {
    setExecutionState(requestId, execution, ExecutionState.CLOSED);
    execution.setClosedDate(Instant.now());
  }

  public static void setToArchived(int requestId, RequestExecution execution) {
    setExecutionState(requestId, execution, ExecutionState.ARCHIVED);
    execution.setArchivedDate(Instant.now());
  }

  private static void setExecutionState(int requestId, RequestExecution execution, ExecutionState newState) {
    ExecutionState currentState = execution.getExecutionState();
    if (isValidTransition(currentState, newState)) {
      log.info("Setting state of request {} execution {} from {} to {}", requestId, execution.getSequenceId(), currentState, newState);
      execution.setExecutionState(newState);
    } else {
      log.warn("Invalid state transition for request {} execution {} from {} to {}", requestId, execution.getSequenceId(), currentState, newState);
      throw new IllegalStateException("Invalid state transition from " + currentState + " to " + newState);
    }
  }

  private static boolean isValidTransition(ExecutionState currentState, ExecutionState newState) {
    Set<ExecutionState> validNextStates = VALID_TRANSITIONS.get(currentState);
    return validNextStates != null && validNextStates.contains(newState);
  }
}