package org.aktin.broker.manager.api.persistence;

import org.aktin.broker.manager.api.models.ManagerRequest;
import org.aktin.broker.query.xml.QuerySchedule;

public interface ManagerRequestHandler extends
    GenericHandler<ManagerRequest<QuerySchedule>, Integer> {

}
