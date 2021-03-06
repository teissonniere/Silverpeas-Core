/*
 * Copyright (C) 2000 - 2018 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.silverpeas.security.web;

import org.silverpeas.util.FileUtil;
import org.silverpeas.core.util.LocalizationBundle;
import org.silverpeas.core.util.StringUtil;
import com.silverpeas.web.TestResources;
import org.silverpeas.util.FileRepositoryManager;
import org.silverpeas.core.util.ResourceLocator;
import java.io.File;
import java.io.StringReader;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import javax.inject.Named;
import org.silverpeas.util.crypto.Cipher;
import org.silverpeas.util.crypto.CipherFactory;
import org.silverpeas.util.crypto.CipherKey;
import org.silverpeas.util.crypto.CryptographicAlgorithmName;


/**
 * It wraps all the resources required by the unit tests on the REST-based web services related on
 * the security.
 */
@Named(TestResources.TEST_RESOURCES_NAME)
public class CryptoTestResources extends TestResources {

  /**
   * The name of the Java package in which are defined the REST-based web service for the
   * cryptographic functions.
   */
  public static final String CRYPTO_WEB_PACKAGE = CipherKeyResource.class.getPackage().getName();

  /**
   * The configuration file from which the Spring context must be bootstrapped.
   */
  public static final String SPRING_CONTEXT = "spring-security-webservice.xml";

  /**
   * The path of the file storing the actual cipher key.
   */
  public static final String ACTUAL_KEY_FILE_PATH = FileRepositoryManager.getSecurityDirPath() +
      ".aid_key";

  /**
   * The path of the file storing the old cipher key.
   */
  public static final String OLD_KEY_FILE_PATH = FileRepositoryManager.getSecurityDirPath() +
      ".did_key";

  /**
   * The symmetric key used to encrypt the cipher key that is used to encrypt and decrypt the
   * content in Silverpeas. This value is hard-coded and comes from the actual implementation of
   * ContentEncryptionService.
   */
  private static final String CAST5_KEY = "06277d1ce530c94bd9a13a72a58342be";

  public static final LocalizationBundle messages =
      ResourceLocator.getLocalizationBundle("org.silverpeas.crypto.multilang.cryptoBundle", "fr");

  public static void generateCipherKeyFile() throws Exception {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(256);
    SecretKey skey = keyGenerator.generateKey();
    String key = StringUtil.asHex(skey.getEncoded());
    CipherFactory cipherFactory = CipherFactory.getFactory();
    Cipher cast5 = cipherFactory.getCipher(CryptographicAlgorithmName.CAST5);
    CipherKey cast5Key = CipherKey.aKeyFromHexText(CAST5_KEY);
    String encryptedContent = StringUtil.asBase64(cast5Key.getRawKey()) + " " +
        StringUtil.asBase64(cast5.encrypt(key, cast5Key));

    File keyFile = new File(ACTUAL_KEY_FILE_PATH);
    if (keyFile.exists()) {
      keyFile.setWritable(true);
    }
    FileUtil.writeFile(keyFile, new StringReader(encryptedContent));
    keyFile.setReadOnly();
  }
}
