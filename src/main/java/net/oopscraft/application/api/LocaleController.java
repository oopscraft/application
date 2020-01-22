package net.oopscraft.application.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.util.LocaleUtility;

@Controller
@RequestMapping("/api/locale")
public class LocaleController {

	/**
	 * getLocales
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "locales", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getLocales() throws Exception {
		return new ResponseEntity<>(JsonConverter.toJson(LocaleUtility.getLocales()), HttpStatus.OK);
	}

	/**
	 * getLanguages
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="languages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getLanguages() throws Exception {
		return new ResponseEntity<>(JsonConverter.toJson(LocaleUtility.getLanguages()), HttpStatus.OK);
	}

}
