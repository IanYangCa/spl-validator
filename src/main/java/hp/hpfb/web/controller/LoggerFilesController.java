package hp.hpfb.web.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hp.hpfb.web.model.UserFile;
import hp.hpfb.web.service.utils.Utilities;

@Controller
public class LoggerFilesController {
	private static Logger logger = LogManager.getLogger(Utilities.class);
	@Autowired
	private Utilities utilities;

	@RequestMapping(value="/admin/logfiles", method=RequestMethod.GET)
    public String renderXml(Model model, HttpServletRequest req){
		model.addAttribute("userFile", new UserFile());
		String sessionId = req.getSession().getId();
      String userPath = utilities.UPLOADED_FOLDER + sessionId + Utilities.FILE_SEPARATOR; //"../logs"; //getLogsRoot("../logs");
      try {
    	  model.addAttribute("logDir", userPath);
    	  model.addAttribute("files", loadAll(userPath, sessionId));
		  return "logfiles";
      } catch(Exception e) {
			model.addAttribute("errorMsg",  "Errors:\n" + e.getClass().getSimpleName() + "\n" + StringUtils.join(e.getStackTrace(), "\n"));
			return "error";
      }
    }
    @RequestMapping("/admin/logfile/{sessionId:.+}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String sessionId, @PathVariable String filename, HttpServletRequest req) {
        Resource file;
		try {
			file = loadAsResource(filename, sessionId);
	        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"").body(file);
		} catch (Exception e) {
			logger.error("Error in LoadBusinessRuleController: " + StringUtils.join(e.getStackTrace(), "\n"));
		}
		return null;
    }
    public Resource loadAsResource(String filename, String sessionId) throws Exception {
        try {
            Path file = load(utilities.UPLOADED_FOLDER + sessionId + Utilities.FILE_SEPARATOR + filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new Exception("Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new Exception("Could not read file: " + filename, e);
        }
    }
    public Path load(String filename) {
        return Paths.get(utilities.SRC_RULES_DIR).resolve(filename);
    }
    public List<String> loadAll(String root, String sessionId){
    	
		File dir = new File(root);
		if(dir != null && dir.isDirectory()) {
			String[] list = dir.list();
			String path = "/admin/logfile/" + sessionId;
			if(list != null && list.length > 0) {
				return Arrays.stream(list).map(item -> path.concat(item)).collect(Collectors.toList());
			}
		} else {
			logger.info("Error: Dir(" + dir + ") is not directory!");
			System.out.println("Error: Dir(" + dir + ") is not directory!");
		}
		return null;
    }
}
