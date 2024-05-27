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
    @XmlJavaTypeAdapter(type = java.time.Duration.class, value = org.aktin.broker.xml.util.DurationAdapter.class),
    @XmlJavaTypeAdapter(type = java.time.Period.class, value = org.aktin.broker.xml.util.PeriodAdapter.class),
    @XmlJavaTypeAdapter(type = java.time.Instant.class, value = org.aktin.broker.xml.util.InstantAdapter.class),
    @XmlJavaTypeAdapter(type = org.aktin.broker.manager.persistence.api.models.UserNote.class, value = org.aktin.broker.manager.persistence.filesystem.adapters.UserNoteAdapter.class)

})

package org.aktin.broker.manager.persistence.filesystem.models;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
