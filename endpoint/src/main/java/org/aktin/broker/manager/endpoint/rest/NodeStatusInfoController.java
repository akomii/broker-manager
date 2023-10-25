package org.aktin.broker.manager.endpoint.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.aktin.broker.manager.api.models.NodeStatusInfo;
import org.aktin.broker.manager.security.enums.UserRole.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//TODO NodeStatusInfoHandler
@RestController
@RequestMapping("/node-status-info")
public class NodeStatusInfoController {

  private static final Logger log = LoggerFactory.getLogger(NodeStatusInfoController.class);

  private final List<NodeStatusInfo> infos = new ArrayList<>();

  @Secured(Code.IT)
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody NodeStatusInfo nodeStatusInfo, Principal principal) {
    log.info("User {} created NodeStatusInfo: {}", principal.getName(), nodeStatusInfo);
    infos.add(nodeStatusInfo);
  }

  @Secured(Code.IT)
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<NodeStatusInfo> getAll(Principal principal) {
    return infos;
  }

  @Secured(Code.IT)
  @GetMapping("/{id}")
  public ResponseEntity<NodeStatusInfo> get(@PathVariable Integer id, Principal principal) {
    return findStatusInfoInList(id)
        .map(info -> new ResponseEntity<>(info, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Secured(Code.IT)
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Integer id,
      @RequestBody NodeStatusInfo updatedNode, Principal principal) {
    Optional<NodeStatusInfo> existingInfo = findStatusInfoInList(id);
    if (existingInfo.isPresent()) {
      log.info("User {} updated NodeStatusInfo with id: {}", principal.getName(), id);
      infos.remove(existingInfo.get());
      infos.add(updatedNode);
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Secured(Code.IT)
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id, Principal principal) {
    Optional<NodeStatusInfo> infoToDelete = findStatusInfoInList(id);
    if (infoToDelete.isPresent()) {
      log.info("User {} deleted NodeStatusInfo with id: {}", principal.getName(), id);
      infos.remove(infoToDelete.get());
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  private Optional<NodeStatusInfo> findStatusInfoInList(Integer id) {
    return infos.stream().filter(node -> node.getNode().equals(id)).findFirst();
  }
}
