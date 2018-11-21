package net.oopscraft.application.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sign")
public class SignController {

	public ResponseEntity<?> signIn() throws Exception {
		return null;
	}
	
	public ResponseEntity<?> signOut() throws Exception {
		return null;
	}

}
