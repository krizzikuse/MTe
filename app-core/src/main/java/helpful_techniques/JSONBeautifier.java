/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpful_techniques;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class JSONBeautifier {
    public static String format(String input) {  //beautifies the json String to make it easily readable
        input = input.replaceAll("\\{", "\\{\n");
        input = input.replaceAll(",", ",\n");
        input = input.replaceAll("\\}", "\n\\}");
        String[] split = input.split("\n");
        int parentcnt = 0;
        String pretty = "";
        for (String line : split) {
            if (line.contains("}"))
                parentcnt--;
            String tabs = new String(new char[parentcnt]).replace("\0", "\t");            
            line = tabs+line;
            pretty += line + "\n";  
            if (line.contains("{"))
                parentcnt++;            
        }    
        return pretty;
    }
}
