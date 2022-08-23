package com.samuelweller.Resource.Server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public List<String> hasSecret() {
		List<String> docsString = new ArrayList<String>();

		Iterable<Document> allDocs = docService.findAll();

		for (Document doc : allDocs) {
			docsString.add(doc.toString());
		}

		return docsString;
	}

}
