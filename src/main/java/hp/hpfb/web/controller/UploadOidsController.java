package hp.hpfb.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import hp.hpfb.web.service.utils.Utilities;

@Controller
public class UploadOidsController {
	private static Logger logger = LogManager.getLogger(UploadOidsController.class);

	@Autowired
	private Utilities utilities;
	@RequestMapping(value="/admin/loadOIDS", method=RequestMethod.GET)
    public String loadOids(Model model, HttpServletRequest req) throws Exception {
        model.addAttribute("files", null);
		return "uploadOIDS";
    }
	@RequestMapping(value="/admin/loadOIDS", method=RequestMethod.POST)
    public ResponseEntity<Object> saveOids(@RequestParam("files") MultipartFile file, Model model, HttpServletRequest req) throws Exception {
        String dir = utilities.OIDS_DIR;
        logger.info("File name: " + file.getOriginalFilename());
        Path userPath = Paths.get(dir, file.getOriginalFilename());
        byte[] bytes;
		try {
			bytes = file.getBytes();
	        Files.write(userPath, bytes);
		} catch (Exception e) {
			logger.error("Error (in UploadOidsController): " + StringUtils.join(e.getStackTrace(), "\n"));
			throw new Exception(e);
		}
		return new ResponseEntity<>(file.getOriginalFilename(),HttpStatus.OK);
    }
    public List<String> loadAll(String root){
		File dir = new File(root);
		String[] list = dir.list();
		if(list != null && list.length > 0) {
			return Arrays.stream(list).map(item -> "/spl-validator/admin/oidFile/".concat(item)).collect(Collectors.toList());
		} else {
			return null;
		}
    }

}
