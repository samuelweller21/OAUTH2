package com.samuelweller.Resource.Server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://127.0.0.1:3000")
@RestController
public class DocumentController {

	@Autowired
	private DocumentRepository docService;

	@GetMapping("/status")
	public String status(Authentication auth) {
		
		System.out.println(auth.getName());
		
		return "Hello";
	}

	@Secured("ROLE_TOP SECRET")
	@GetMapping("/secret")
	public Iterable<Document> hasSecret() {

		List<Document> allDocs = new ArrayList<Document>();
		docService.findAll().forEach(allDocs::add);

		return allDocs;
	}

}
