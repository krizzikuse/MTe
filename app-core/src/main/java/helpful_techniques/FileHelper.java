/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpful_techniques;

import com.mercury.platform.shared.config.MercuryConfigManager;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class FileHelper {
    //TODO! -> find a better version of this in order to definitely find files, 
    // no matter where and when! For now going one step up ('..'),
    // getting the absolute path of that location (which weirdly enough 
    // ends with '..' ) and then appending the parameter path to it should be enough!
    public static String getPathDev(String path) { 
        String basePath = new File("..").getAbsolutePath() + path;
        //System.out.println(basePath);
        return(basePath);
    }
    public static String getPath(String path) { 
        boolean debug=false;
        try{
        while (debug)
            Thread.sleep(1000);
        } catch(Exception ex) {}
        
        String abspath = "";
        try {
            abspath = getUniversalBasePath(path);
        } catch (Exception ex) {
            abspath = new File(".").getAbsolutePath();
        }
        //String basePath = new File(".").getAbsolutePath() + path;
        String basePath = abspath + path;
        
        //System.out.println(basePath);
        return(basePath);
    }
    public static String getUniversalBasePath(String path) throws URISyntaxException { 
            final Class<?> referenceClass = MercuryConfigManager.class;
            final URL url =
                referenceClass.getProtectionDomain().getCodeSource().getLocation();

            final File jarPath = new File(url.toURI()).getParentFile();
            //int lastSlash = jarPath.toString().lastIndexOf("/");
            int lastSlash = jarPath.toString().lastIndexOf("/") != -1 ? jarPath.toString().lastIndexOf("/") : jarPath.toString().lastIndexOf("\\");
            String pathprefix ;
            if (jarPath.toString().substring(lastSlash+1, jarPath.toString().length()).equals("target"))
                pathprefix = "/../../";
            else pathprefix = "/./";
                
            URI upath = URI.create((jarPath.toString().replaceAll("\\\\", "/") + pathprefix).replaceAll(" ", "%20"));
            return(upath.toString().replaceAll("%20", " "));
                      
//        String basePath = new File(".").getAbsolutePath() + path;
//        //System.out.println(basePath);
//        return(basePath);
    }
}
