package org.aktin.broker.manager.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import org.aktin.broker.manager.KeycloakContainer;
import org.aktin.broker.manager.TestApplication;
import org.aktin.broker.manager.api.factories.NodeStatusInfoFactory;
import org.aktin.broker.manager.api.models.NodeStatusInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

//TODO containers wants to restart between tests
@Testcontainers
@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("security")
class TestNodeStatusInfoController {

  private final KeycloakContainer keycloakContainer = new KeycloakContainer();

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private NodeStatusInfoFactory nodeStatusInfoFactory;

  @Test
  void create() throws Exception {
    NodeStatusInfo statusInfo = createDefaultInfo();
    String token = keycloakContainer.fetchAccessTokenFor(keycloakContainer.getDefaultUsername());

    mockMvc.perform(post("/api/node-status-info")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(statusInfo)))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/api/node-status-info/1")
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.node").value(1))
        .andExpect(jsonPath("$.executionId").value(101));
  }

  @Test
  void createUnauthorized() throws Exception {
    NodeStatusInfo statusInfo = createDefaultInfo();
    mockMvc.perform(post("/api/node-status-info")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(statusInfo)))
        .andExpect(status().isUnauthorized());
  }

  private NodeStatusInfo createDefaultInfo() {
    NodeStatusInfo statusInfo = nodeStatusInfoFactory.create();
    statusInfo.setNode(1);
    statusInfo.setExecutionId(101);
    statusInfo.setStatusMessage("Processing");
    statusInfo.setDeleted(Instant.parse("2023-10-20T12:34:56Z"));
    statusInfo.setRetrieved(Instant.parse("2023-10-20T12:35:56Z"));
    statusInfo.setQueued(Instant.parse("2023-10-20T12:36:56Z"));
    statusInfo.setProcessing(Instant.parse("2023-10-20T12:37:56Z"));
    statusInfo.setCompleted(Instant.parse("2023-10-20T12:38:56Z"));
    statusInfo.setRejected(Instant.parse("2023-10-20T12:39:56Z"));
    statusInfo.setFailed(Instant.parse("2023-10-20T12:40:56Z"));
    statusInfo.setExpired(Instant.parse("2023-10-20T12:41:56Z"));
    return statusInfo;
  }
}
