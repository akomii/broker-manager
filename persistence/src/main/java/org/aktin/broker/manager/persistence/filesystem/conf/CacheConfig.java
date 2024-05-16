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

package org.aktin.broker.manager.persistence.filesystem.conf;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableCaching
@Profile("filesystem")
public class CacheConfig {

  @Bean
  public Caffeine<Object, Object> caffeineConfig() {
    return Caffeine.newBuilder()
        .maximumSize(100)
        .expireAfterWrite(60, TimeUnit.MINUTES);
  }

  @Bean
  public CacheManager cacheManager(Caffeine<Object, Object> caffeineConfig) {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    cacheManager.setCaches(List.of(
        new ConcurrentMapCache("managerNodes", caffeineConfig.build().asMap(), false),
        new ConcurrentMapCache("managerRequests", caffeineConfig.build().asMap(), false),
        new ConcurrentMapCache("requestsArchive", caffeineConfig.build().asMap(), false)
    ));
    return cacheManager;
  }
}
