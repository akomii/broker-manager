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

import org.aktin.broker.manager.model.api.models.ManagerRequest;
import org.aktin.broker.manager.model.api.models.RequestExecution;
import org.aktin.broker.manager.persistence.api.exceptions.DataReadException;
import org.aktin.broker.manager.persistence.api.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.service.api.exceptions.EntityNotFoundException;

public class RequestUtils {

  private RequestUtils() {
  }

  public static RequestExecution getExecutionFromRequest(ManagerRequest request, int sequenceId)
      throws EntityNotFoundException {
    return request.getRequestExecutions().stream()
        .filter(execution -> execution.getSequenceId() == sequenceId)
        .findFirst()
        .orElseThrow(() -> new EntityNotFoundException("Execution not found: " + sequenceId));
  }

  public static ManagerRequest getRequestFromRepository(ManagerRequestRepository requestRepository, int requestId)
      throws DataReadException, EntityNotFoundException {
    return requestRepository.get(requestId)
        .orElseThrow(() -> new EntityNotFoundException("Request not found: " + requestId));
  }
}
