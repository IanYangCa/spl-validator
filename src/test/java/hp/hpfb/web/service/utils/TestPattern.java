package hp.hpfb.web.service.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;

public class TestPattern {
	String test = "C:\\Users\\hcuser\\Downloads\\test\\data\\input\\2.zip";
	String img = "C:\\Users\\hcuser\\Downloads\\test\\data\\input\\2.BMP";
	public static Pattern zipPattern = Pattern.compile(".+\\.zip$", Pattern.CASE_INSENSITIVE);
	public static Pattern imgPatterns = Pattern.compile("\\.jpg$|\\.jpeg$|\\.png$|\\.gif$|\\.bmp$", Pattern.CASE_INSENSITIVE);

	@Test
	public void testImg() {
		Matcher matcher = imgPatterns.matcher(img);
		if (matcher.find()) {
            System.out.println("Start index: " + matcher.start());
            System.out.println(" End index: " + matcher.end() + " ");
            System.out.println(matcher.group());
            System.out.println("size: " + test.length());
        } else {
        	System.out.println("IMG Not Match!!");
        }
	}
	
	@Test
	public void testZip() {
		Matcher matcher = zipPattern.matcher(test);
		if (matcher.find()) {
            System.out.println("Start index: " + matcher.start());
            System.out.println(" End index: " + matcher.end() + " ");
            System.out.println(matcher.group());
            System.out.println("size: " + test.length());
        } else {
        	System.out.println("ZIP Not Match!!");
        }
	}
	@Test
	public void testFormat() {
		for(int i = 0 ; i < 10; i++) {
			System.out.println("i: " + String.format(".%03d", i));
		}
	}
	
	@Test
	public void testFileList() {
		File dir = new File("c:/temp/rules");
		String[] list = dir.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("business", 0);
			} 
			
		});
//		Arrays.asList(list).forEach(item -> System.out.println(item));
		List<String> result = Arrays.stream(list).map(item -> "/businessRule/".concat(item)).collect(Collectors.toList());
		result.forEach(item -> System.out.println(item));
	}
}
