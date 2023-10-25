package org.aktin.broker.manager;

import java.nio.file.Paths;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class KeycloakContainer {

  private final FixedHostPortGenericContainer KEYCLOAK;
  private final String KEYCLOAK_URL;

  // keycloak admin, not realm admin
  private static final String DEFAULT_ADMIN_NAME = "admin";
  private static final String DEFAULT_ADMIN_PASSWORD = "password";

  // analog to /resources/realm.json
  private static final String DEFAULT_REALM_NAME = "broker-manager-realm";
  private static final String DEFAULT_USER = "john";

  private static final String KEYCLOAK_VERISON = "22.0.1";
  private static final int HOST_PORT = 8180;
  private static final int KEYCLOAK_PORT = 8080;
  private static final String REALM_IMPORT_PATH = Paths.get("src", "test", "resources",
      "realm.json").toAbsolutePath().toString();

  public KeycloakContainer() {
    KEYCLOAK = initKeycloakContainer();
    KEYCLOAK.start();
    KEYCLOAK_URL = buildKeycloakUrl();
    Runtime.getRuntime().addShutdownHook(new Thread(KEYCLOAK::stop));
  }

  private FixedHostPortGenericContainer initKeycloakContainer() {
    return new FixedHostPortGenericContainer<>(
        "quay.io/keycloak/keycloak:" + KEYCLOAK_VERISON).withFixedExposedPort(HOST_PORT,
            KEYCLOAK_PORT).withEnv("KEYCLOAK_ADMIN", DEFAULT_ADMIN_NAME)
        .withEnv("KEYCLOAK_ADMIN_PASSWORD", DEFAULT_ADMIN_PASSWORD)
        .withFileSystemBind(REALM_IMPORT_PATH, "/opt/keycloak/data/import/realm.json")
        .withCommand("start-dev", "--import-realm")
        .waitingFor(Wait.forHttp("/admin").forStatusCode(200));
  }

  private String buildKeycloakUrl() {
    return "http://" + KEYCLOAK.getHost() + ":" + HOST_PORT;
  }

  public String fetchAccessTokenFor(String username) {
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<MultiValueMap<String, String>> requestEntity = buildRequestEntity(username,
        username);
    ResponseEntity<Map> responseEntity = makePostRequest(restTemplate, requestEntity);
    return extractAccessToken(responseEntity);
  }

  private HttpEntity<MultiValueMap<String, String>> buildRequestEntity(String username,
      String password) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("client_id", "broker-manager-client");
    requestBody.add("username", username);
    requestBody.add("password", password);
    requestBody.add("grant_type", "password");
    return new HttpEntity<>(requestBody, headers);
  }

  private ResponseEntity<Map> makePostRequest(RestTemplate restTemplate,
      HttpEntity<MultiValueMap<String, String>> requestEntity) {
    String url = KEYCLOAK_URL + "/realms/" + DEFAULT_REALM_NAME + "/protocol/openid-connect/token";
    return restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
  }

  private String extractAccessToken(ResponseEntity<Map> responseEntity) {
    Map<String, Object> responseBody = responseEntity.getBody();
    if (responseBody != null && responseBody.containsKey("access_token")) {
      return (String) responseBody.get("access_token");
    }
    return null;
  }

  public String getDefaultUsername() {
    return DEFAULT_USER;
  }
}
