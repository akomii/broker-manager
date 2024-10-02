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

package org.aktin.broker.manager.persistence.impl.filesystem.repositories;

import java.io.IOException;
import java.util.List;
import org.aktin.broker.manager.model.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.api.repositories.ManagerNodeRepository;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlMarshaller;
import org.aktin.broker.manager.persistence.impl.filesystem.util.XmlUnmarshaller;

// ManagerNodes are registered on the broker-server, broker-manager only mirrors them, so no id generation is necessary
public class FsManagerNodeRepository extends AbstractXMLRepository<ManagerNode> implements ManagerNodeRepository {

  public FsManagerNodeRepository(XmlMarshaller xmlMarshaller, XmlUnmarshaller<ManagerNode> xmlUnmarshaller, String nodesDirectory)
      throws IOException {
    super(xmlMarshaller, xmlUnmarshaller, nodesDirectory);
  }

  @Override
  protected String entityType() {
    return "ManagerNode";
  }

  @Override
  public ManagerNode save(ManagerNode entity) {
    return saveEntity(entity);
  }

  @Override
  public List<ManagerNode> getAll() {
    return getAllEntities();
  }

  @Override
  public void delete(int id) {
    deleteEntity(id);
  }
}
