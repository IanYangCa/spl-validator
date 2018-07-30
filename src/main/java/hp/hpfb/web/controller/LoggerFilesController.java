package hp.hpfb.web.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hp.hpfb.web.model.UserFile;

@Controller
public class LoggerFilesController {

	@RequestMapping(value="/admin/logfiles", method=RequestMethod.GET)
    public String renderXml(Model model, HttpServletRequest req) throws Exception {
		model.addAttribute("userFile", new UserFile());
      String userPath = "../logs/";
      model.addAttribute("files", loadAll(userPath));
		return "logfiles";
    }
    public List<String> loadAll(String root){
    	
    	try {
    		File dir = new File(root);
    		String[] list = dir.list(new FilenameFilter() {
    			@Override
    			public boolean accept(File dir, String name) {
    				return name.endsWith(".log");
    			}
    		});
    		return Arrays.stream(list).map(item -> "/spl-validator/admin/logfile/".concat(item)).collect(Collectors.toList());
		} catch(Throwable e) {
			e.printStackTrace();
		}
    	return null;
    }

}
