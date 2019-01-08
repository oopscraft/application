package net.oopscraft.application.api.controller;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.core.ValueMap;

@Controller
@RequestMapping("/api/file")
public class FileController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

	/**
	 * Uploads file
	 * @param multipartFile
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> upload(
		@RequestParam("file")MultipartFile multipartFile,
		MultipartHttpServletRequest request
	) throws Exception {

		// sets file info
		ValueMap responseMap = new ValueMap();
		responseMap.set("name", multipartFile.getName());
		responseMap.set("originalFilename", multipartFile.getOriginalFilename());
		responseMap.set("contentType", multipartFile.getContentType());
		responseMap.set("size", multipartFile.getSize());
		
		// generates temporary file name
		UUID uuid = UUID.randomUUID();
		String tempFileName = uuid.toString().replaceAll("-", "");
		responseMap.set("tempFileName", tempFileName);
		
		// uploads file
		File tempFile = new File(".file" + File.separator + tempFileName);
		FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), tempFile);
		
		// sends response
		LOGGER.debug("{}", new TextTable(responseMap));
		return new ResponseEntity<>(JsonUtils.toJson(responseMap), HttpStatus.OK);
	}

}
