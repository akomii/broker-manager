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

import java.util.EnumMap;
import java.util.Set;
import org.aktin.broker.manager.model.api.enums.RequestState;
import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestStateManager {

  private static final Logger log = LoggerFactory.getLogger(RequestStateManager.class);
  private static final EnumMap<RequestState, Set<RequestState>> VALID_TRANSITIONS = new EnumMap<>(RequestState.class);

  static {
    VALID_TRANSITIONS.put(RequestState.DRAFT, Set.of(RequestState.ONLINE));
    VALID_TRANSITIONS.put(RequestState.ONLINE, Set.of(RequestState.CLOSED));
    VALID_TRANSITIONS.put(RequestState.CLOSED, Set.of(RequestState.ARCHIVED));
    VALID_TRANSITIONS.put(RequestState.ARCHIVED, Set.of());
  }

  public static void setRequestStateToOnline(ManagerRequest request) {
    setRequestState(request, RequestState.ONLINE);
  }

  public static void setRequestStateToClosed(ManagerRequest request) {
    setRequestState(request, RequestState.CLOSED);
  }

  public static void setRequestStateToArchived(ManagerRequest request) {
    setRequestState(request, RequestState.ARCHIVED);
  }

  private static void setRequestState(ManagerRequest request, RequestState newState) {
    RequestState currentState = request.getRequestState();
    if (isValidTransition(currentState, newState)) {
      log.info("Setting state of request {} from {} to {}", request.getId(), currentState, newState);
      request.setRequestState(newState);
    } else {
      log.warn("Invalid state transition for request {} from {} to {}", request.getId(), currentState, newState);
      throw new IllegalStateException("Invalid state transition from " + currentState + " to " + newState);
    }
  }

  private static boolean isValidTransition(RequestState currentState, RequestState newState) {
    Set<RequestState> validNextStates = VALID_TRANSITIONS.get(currentState);
    return validNextStates != null && validNextStates.contains(newState);
  }
}