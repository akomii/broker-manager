module org.aktin.broker.manager {
  // List of required modules
  requires broker.api;
  requires com.github.benmanes.caffeine;
  requires java.xml;
  requires java.xml.bind;
  requires static lombok;
  requires org.slf4j;
  requires query.model;
  requires spring.beans;
  requires spring.context;

  // Export only the repository interfaces
  exports org.aktin.broker.manager.exceptions;
  exports org.aktin.broker.manager.repositories;
}
