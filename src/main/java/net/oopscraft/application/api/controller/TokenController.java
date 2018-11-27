package net.oopscraft.application.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sign")
public class TokenController {

	public ResponseEntity<?> signIn() throws Exception {
		return null;
	}
	
	public ResponseEntity<?> signOut() throws Exception {
		return null;
	}

}
