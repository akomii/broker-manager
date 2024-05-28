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

package org.aktin.broker.manager.persistence.api.models;

import java.time.Instant;

/**
 * Represents a {@link ManagerRequest} that is executed repeatedly over time, generating a series of {@link RequestExecution}.
 *
 * @author akombeiz@ukaachen.de
 * @version 1.0
 */
public interface SeriesRequest extends ManagerRequest {

  int getAnchoredSequenceIdRef();

  void setAnchoredSequenceIdRef(int sequenceIdRef);

  boolean isAutoPublishing();

  void setAutoPublishing(boolean autoPublishing);

  Instant getSeriesClosingDate();

  void setSeriesClosingDate(Instant seriesClosingDate);

  Instant getSeriesArchiveDate();

  void setSeriesArchiveDate(Instant seriesArchiveDate);
}
