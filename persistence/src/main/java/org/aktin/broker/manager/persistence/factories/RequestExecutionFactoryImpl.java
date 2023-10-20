package org.aktin.broker.manager.persistence.factories;

import org.aktin.broker.manager.api.factories.RequestExecutionFactory;
import org.aktin.broker.manager.api.models.RequestExecution;
import org.aktin.broker.manager.persistence.daos.RequestExecutionDao;
import org.springframework.stereotype.Component;

@Component
public class RequestExecutionFactoryImpl implements RequestExecutionFactory {

  @Override
  public RequestExecution create() {
    return new RequestExecutionDao();
  }
}
