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

package org.aktin.broker.manager.persistence.filesystem.repositories;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.aktin.broker.manager.models.ManagerRequest;
import org.aktin.broker.manager.exceptions.DataDeleteException;
import org.aktin.broker.manager.exceptions.DataPersistException;
import org.aktin.broker.manager.exceptions.DataReadException;
import org.aktin.broker.manager.repositories.ManagerRequestRepository;
import org.aktin.broker.manager.persistence.filesystem.util.FsIdGenerator;
import org.aktin.broker.manager.persistence.filesystem.util.XmlMarshaller;
import org.aktin.broker.manager.persistence.filesystem.util.XmlUnmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

//TODO refactor and simplify
//TODO test Cache
//TODO add SqlLite for indexing requests with Tags, Name, ID, external IDs and Orgs?
public class FsManagerRequestRepository implements ManagerRequestRepository {

  private static final Logger log = LoggerFactory.getLogger(FsManagerRequestRepository.class);
  private static final String XML_EXTENSION = ".xml";

  private final XmlMarshaller xmlMarshaller;
  private final XmlUnmarshaller<ManagerRequest> xmlUnmarshaller;
  private final String requestsDirectory;

  private final Map<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();
  private final FsIdGenerator fsIdGenerator;

  public FsManagerRequestRepository(XmlMarshaller xmlMarshaller, XmlUnmarshaller<ManagerRequest> xmlUnmarshaller, String requestsDirectory)
      throws IOException {
    this.xmlMarshaller = xmlMarshaller;
    this.xmlUnmarshaller = xmlUnmarshaller;
    this.requestsDirectory = requestsDirectory;
    Path storagePath = Paths.get(requestsDirectory);
    Files.createDirectories(storagePath);
    this.fsIdGenerator = new FsIdGenerator(storagePath);
  }

  private ReentrantReadWriteLock getLock(String filePath) {
    return fileLocks.computeIfAbsent(filePath, f -> new ReentrantReadWriteLock());
  }

  @CacheEvict(cacheNames = "managerRequests", key = "#entity.id")
  @Override
  public int save(ManagerRequest entity) throws DataPersistException {
    if (entity.getId() == 0) {
      entity.setId(fsIdGenerator.generateId());
    }
    String filePath = Paths.get(requestsDirectory, entity.getId() + XML_EXTENSION).toString();
    ReentrantReadWriteLock lock = getLock(filePath);
    lock.writeLock().lock();
    try {
      File file = new File(filePath);
      if (!file.exists()) {
        log.info("Creating new ManagerRequest: {}", filePath);
      }
      xmlMarshaller.marshal(entity, file);
      return entity.getId();
    } catch (Exception e) {
      throw new DataPersistException("Failed to save ManagerRequest: " + entity.getId(), e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @CacheEvict(cacheNames = "managerRequests", key = "#id")
  @Override
  public void delete(int id) throws DataDeleteException {
    String filePath = Paths.get(requestsDirectory, id + XML_EXTENSION).toString();
    ReentrantReadWriteLock lock = getLock(filePath);
    lock.writeLock().lock();
    try {
      boolean deleted = Files.deleteIfExists(Paths.get(filePath));
      if (!deleted) {
        log.warn("ManagerRequest file not found for deletion: {}", filePath);
      } else {
        log.info("Deleted ManagerRequest file: {}", filePath);
      }
    } catch (Exception e) {
      throw new DataDeleteException("Error deleting ManagerRequest: " + filePath, e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Cacheable(cacheNames = "managerRequests", key = "#id")
  @Override
  public Optional<ManagerRequest> get(int id) throws DataReadException {
    String filePath = Paths.get(requestsDirectory, id + XML_EXTENSION).toString();
    File file = new File(filePath);
    if (!file.exists()) {
      return Optional.empty();
    }
    ReentrantReadWriteLock lock = getLock(filePath);
    lock.readLock().lock();
    try {
      return Optional.of(xmlUnmarshaller.unmarshal(file));
    } catch (Exception e) {
      throw new DataReadException("Error retrieving ManagerRequest: " + filePath, e);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Cacheable(cacheNames = "managerRequests")
  @Override
  public List<ManagerRequest> getAll() {
    List<ManagerRequest> requests = new ArrayList<>();
    File storageDir = new File(requestsDirectory);
    File[] files = storageDir.listFiles((dir, name) -> name.endsWith(XML_EXTENSION));
    if (files == null) {
      return requests;
    }
    for (File file : files) {
      String filePath = file.getAbsolutePath();
      ReentrantReadWriteLock lock = getLock(filePath);
      lock.readLock().lock();
      try {
        ManagerRequest request = xmlUnmarshaller.unmarshal(file);
        if (request != null) {
          requests.add(request);
        }
      } catch (Exception e) {
        log.warn("Error retrieving ManagerRequest: {}, skipping...", filePath, e);
      } finally {
        lock.readLock().unlock();
      }
    }
    return requests;
  }

  @Cacheable(cacheNames = "managerRequests")
  @Override
  public List<ManagerRequest> getAllForOrganizations(List<Integer> authorizedOrgIds) {
    List<ManagerRequest> filteredRequests = new ArrayList<>();
    File storageDir = new File(requestsDirectory);
    File[] files = storageDir.listFiles((dir, name) -> name.endsWith(XML_EXTENSION));
    if (files == null) {
      return filteredRequests;
    }
    for (File file : files) {
      String filePath = file.getAbsolutePath();
      ReentrantReadWriteLock lock = getLock(filePath);
      lock.readLock().lock();
      try {
        ManagerRequest request = xmlUnmarshaller.unmarshal(file);
        if (request != null && hasAuthorizedOrganization(request, authorizedOrgIds)) {
          filteredRequests.add(request);
        }
      } catch (Exception e) {
        log.warn("Error retrieving ManagerRequest: {}, skipping...", filePath, e);
      } finally {
        lock.readLock().unlock();
      }
    }
    return filteredRequests;
  }

  private boolean hasAuthorizedOrganization(ManagerRequest request, List<Integer> authorizedOrgIds) {
    Set<Integer> requestOrgIds = request.getAuthorizedOrganizations();
    return !Collections.disjoint(requestOrgIds, authorizedOrgIds);
  }
}
