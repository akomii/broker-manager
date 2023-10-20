package org.aktin.broker.manager.persistence.factories;

import org.aktin.broker.manager.api.factories.NodeStatusInfoFactory;
import org.aktin.broker.manager.api.models.NodeStatusInfo;
import org.aktin.broker.manager.persistence.daos.NodeStatusInfoDao;
import org.springframework.stereotype.Component;

@Component
public class NodeStatusInfoFactoryImpl implements NodeStatusInfoFactory {

  @Override
  public NodeStatusInfo create() {
    return new NodeStatusInfoDao();
  }
}
