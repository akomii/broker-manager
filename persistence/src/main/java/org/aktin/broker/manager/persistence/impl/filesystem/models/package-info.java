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

@XmlJavaTypeAdapters(value = {
    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class),
    @XmlJavaTypeAdapter(type = Period.class, value = PeriodAdapter.class),
    @XmlJavaTypeAdapter(type = Instant.class, value = InstantAdapter.class),
    @XmlJavaTypeAdapter(type = TextEntry.class, value = TextEntryAdapter.class),
    @XmlJavaTypeAdapter(type = RequestExecution.class, value = RequestExecutionAdapter.class),
    @XmlJavaTypeAdapter(type = NodeStatusInfo.class, value = NodeStatusInfoAdapter.class),
    @XmlJavaTypeAdapter(type = DownloadEvent.class, value = DownloadEventAdapter.class)
})

package org.aktin.broker.manager.persistence.impl.filesystem.models;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import org.aktin.broker.manager.model.models.DownloadEvent;
import org.aktin.broker.manager.model.models.NodeStatusInfo;
import org.aktin.broker.manager.model.models.RequestExecution;
import org.aktin.broker.manager.model.models.TextEntry;
import org.aktin.broker.manager.persistence.impl.filesystem.adapters.DownloadEventAdapter;
import org.aktin.broker.manager.persistence.impl.filesystem.adapters.NodeStatusInfoAdapter;
import org.aktin.broker.manager.persistence.impl.filesystem.adapters.RequestExecutionAdapter;
import org.aktin.broker.manager.persistence.impl.filesystem.adapters.TextEntryAdapter;
import org.aktin.broker.xml.util.DurationAdapter;
import org.aktin.broker.xml.util.InstantAdapter;
import org.aktin.broker.xml.util.PeriodAdapter;
