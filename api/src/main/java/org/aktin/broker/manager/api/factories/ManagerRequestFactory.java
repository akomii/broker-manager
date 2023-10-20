package org.aktin.broker.manager.api.factories;

import org.aktin.broker.manager.api.models.SeriesRequest;
import org.aktin.broker.manager.api.models.SingleRequest;

public interface ManagerRequestFactory {

  SingleRequest createSingleRequest();

  SeriesRequest createSeriesRequest();
}