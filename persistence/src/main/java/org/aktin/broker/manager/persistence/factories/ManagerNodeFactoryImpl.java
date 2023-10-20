package org.aktin.broker.manager.persistence.factories;

import org.aktin.broker.manager.api.factories.ManagerNodeFactory;
import org.aktin.broker.manager.api.models.ManagerNode;
import org.aktin.broker.manager.persistence.daos.ManagerNodeDao;
import org.springframework.stereotype.Component;

@Component
public class ManagerNodeFactoryImpl implements ManagerNodeFactory {

  @Override
  public ManagerNode create() {
    return new ManagerNodeDao();
  }
}
