package com.samuelweller.Resource.Server;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends CrudRepository<Document, Long> {

	  List<Document> findByName(String name);
	
}
