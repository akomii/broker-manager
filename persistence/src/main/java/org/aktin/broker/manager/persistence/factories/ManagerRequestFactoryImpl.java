package org.aktin.broker.manager.persistence.factories;

import org.aktin.broker.manager.api.factories.ManagerRequestFactory;
import org.aktin.broker.manager.api.models.SeriesRequest;
import org.aktin.broker.manager.api.models.SingleRequest;
import org.aktin.broker.manager.persistence.daos.SeriesRequestDao;
import org.aktin.broker.manager.persistence.daos.SingleRequestDao;

public class ManagerRequestFactoryImpl implements ManagerRequestFactory {

  @Override
  public SingleRequest createSingleRequest() {
    return new SingleRequestDao();
  }

  @Override
  public SeriesRequest createSeriesRequest() {
    return new SeriesRequestDao();
  }
}
