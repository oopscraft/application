package net.oopscraft.application.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.oopscraft.application.common.JsonConverter;
import net.oopscraft.application.common.ValueMap;

@Controller
@RequestMapping("/api")
public class TokenController {

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE) 
	public ResponseEntity<?> signIn() throws Exception {
		ValueMap responseMap = new ValueMap();
		responseMap.set("message", "Hellow111~");
		return new ResponseEntity<>(JsonConverter.toJson(responseMap),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/lock", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE) 
	public ResponseEntity<?> test() throws Exception {
		
		
		
		
		ValueMap responseMap = new ValueMap();
		responseMap.set("message", "Hellow111~");
		return new ResponseEntity<>(JsonConverter.toJson(responseMap),HttpStatus.OK);
	}

}
