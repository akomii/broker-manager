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

package org.aktin.broker.manager.persistence.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.aktin.broker.manager.model.api.models.ManagerRequest;

/**
 * Defines an interface for data persistence operations related to {@link ManagerRequest} entities.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface ManagerRequestRepository {

  int save(ManagerRequest entity);

  Optional<ManagerRequest> get(int id);

  List<ManagerRequest> getAll();

  List<ManagerRequest> getFiltered(Predicate<ManagerRequest> filter);

  void delete(int id);
}
