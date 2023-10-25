package org.aktin.broker.manager.endpoint.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.aktin.broker.manager.api.models.NodeStatusInfo;
import org.aktin.broker.manager.security.enums.UserRole.Code;
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
import org.springframework.web.bind.annotation.RestController;

//TODO NodeStatusInfoHandler
@RestController
@RequestMapping("/node-status-info")
public class NodeStatusInfoController {

  private final List<NodeStatusInfo> infos = new ArrayList<>();

  @Secured(Code.IT)
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody NodeStatusInfo nodeStatusInfo) {
    infos.add(nodeStatusInfo);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Secured(Code.IT)
  @GetMapping
  public ResponseEntity<List<NodeStatusInfo>> getAll() {
    return new ResponseEntity<>(infos, HttpStatus.OK);
  }

  @Secured(Code.IT)
  @GetMapping("/{id}")
  public ResponseEntity<NodeStatusInfo> get(@PathVariable Integer id) {
    Optional<NodeStatusInfo> statusInfo = findStatusInfoInList(id);
    return statusInfo.map(info -> new ResponseEntity<>(info, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Secured(Code.IT)
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Integer id,
      @RequestBody NodeStatusInfo updatedNode) {
    Optional<NodeStatusInfo> existingInfo = findStatusInfoInList(id);
    if (existingInfo.isPresent()) {
      infos.remove(existingInfo.get());
      infos.add(updatedNode);
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Secured(Code.IT)
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    Optional<NodeStatusInfo> infoToDelete = findStatusInfoInList(id);
    if (infoToDelete.isPresent()) {
      infos.remove(infoToDelete.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  private Optional<NodeStatusInfo> findStatusInfoInList(Integer id) {
    return infos.stream().filter(node -> node.getNode().equals(id)).findFirst();
  }
}
