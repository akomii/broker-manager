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

package org.aktin.broker.manager.service.impl.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.aktin.broker.manager.service.api.exceptions.HashGenerationException;

public class SHA256Generator implements HashGenerator {

  private static final String HASH_ALGORITHM = "SHA-256";

  @Override
  public String getAlgorithm() {
    return HASH_ALGORITHM;
  }

  @Override
  public String generateHash(InputStream dataStream) throws HashGenerationException {
    try {
      MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
      byte[] byteArray = new byte[1024];
      int bytesCount;
      while ((bytesCount = dataStream.read(byteArray)) != -1) {
        digest.update(byteArray, 0, bytesCount);
      }
      byte[] bytes = digest.digest();
      StringBuilder sb = new StringBuilder();
      for (byte b : bytes) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException | IOException e) {
      throw new HashGenerationException("Error occurred during generation of file hash", e);
    }
  }
}
