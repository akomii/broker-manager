package org.aktin.broker.manager.endpoint.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.aktin.broker.manager.api.models.ManagerNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO managerNodeHandler
@RestController
@RequestMapping("/node")
public class ManagerNodeController {

  private final List<ManagerNode> nodes = new ArrayList<>();

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody ManagerNode managerNode) {
    nodes.add(managerNode);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<ManagerNode>> getAll() {
    return new ResponseEntity<>(nodes, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ManagerNode> get(@PathVariable Integer id) {
    Optional<ManagerNode> managerNode = findNodeInList(id);
    return managerNode.map(node -> new ResponseEntity<>(node, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Integer id,
      @RequestBody ManagerNode updatedNode) {
    Optional<ManagerNode> existingNode = findNodeInList(id);
    if (existingNode.isPresent()) {
      nodes.remove(existingNode.get());
      nodes.add(updatedNode);
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    Optional<ManagerNode> nodeToDelete = findNodeInList(id);
    if (nodeToDelete.isPresent()) {
      nodes.remove(nodeToDelete.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  private Optional<ManagerNode> findNodeInList(Integer id) {
    return nodes.stream().filter(node -> node.getId().equals(id))
        .findFirst();
  }
}
