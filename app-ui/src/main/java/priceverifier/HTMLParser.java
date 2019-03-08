/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package priceverifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class HTMLParser {
    public static String download2String(String urlstr) {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        String content = "";
        try {
            url = new URL(urlstr);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            LogManager.getLogger(HTMLParser.class.getSimpleName()).log(Level.WARN, "HTMLParser.download2String(String url) called, but shouldnt be called anymore!!!");
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
//                LogManager.getLogger(HTMLParser.class.getSimpleName()).log(Level.WARN, line);
                content += line;
            }
        } catch (MalformedURLException mue) {
             mue.printStackTrace();
        } catch (IOException ioe) {
             ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
        return content;
    }
    /*
    private Pattern poeAppItemPattern;
    private final static String poeAppPattern = "^(.*\\s)?(.+): (\\s*?wtb\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.+?)\\s+?\\(stash\\s+?\"(.*?)\";\\s+?left\\s+?(\\d+?),\\s+?top\\s+(\\d+?)\\)\\s*?(.*))$";


    this.poeAppItemPattern = Pattern.compile(poeAppPattern);

    Matcher poeAppItemMatcher = poeAppItemPattern.matcher(fullMessage);
    if (poeAppItemMatcher.find()) {
                ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
                tradeNotification.setWhisperNickname(poeAppItemMatcher.group(2));    
    */
    
    private static final String poeTradeItemSearchPatternString = "O).*poe\\.trade/search\\?(.*)";
    private static Pattern poeTradeItemSearchPattern = Pattern.compile(poeTradeItemSearchPatternString);
    
    public static String currencyPoeTrade_GetSource(String url, boolean skipPayload) {
        //String payloadPat = url.matches("O).*poe\.trade/search\?(.*)");
        Matcher payloadPat = poeTradeItemSearchPattern.matcher(url);
        
        String payload = "";
        if (skipPayload==false)
            payload = payloadPat.group(1);
        
        String postData = payload;
        int payLength = postData.length();
        String options = "";
        
        ArrayList<String> reqHeaders = new ArrayList<String>();
        reqHeaders.add("User-Agent:Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
        reqHeaders.add("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        reqHeaders.add("Accept-Encoding:gzip, deflate");
        reqHeaders.add("Accept-Language:de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");
        reqHeaders.add("Connection:keep-alive");
        reqHeaders.add("Referer:http://currency.poe.trade");
        reqHeaders.add("Upgrade-Insecure-Requests:1");
        
        //String html = 
        return("");
    }
    //CurrencyPoeTrade_GetSource(url, skipPayload=False) {
    //    RegExMatch(url, "O).*poe\.trade/search\?(.*)", payloadPat)
    //    if (skipPayload = False)
    //        payload := payloadPat.1
    //
    //    postData 	:= payload
    //	payLength	:= StrLen(postData)
    //	options	    := ""
    //
    //	reqHeaders	:= []
    //	reqHeaders.push("User-Agent:Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
    //	reqHeaders.push("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
    //	reqHeaders.push("Accept-Encoding:gzip, deflate")
    //	reqHeaders.push("Accept-Language:de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4")
    //	reqHeaders.push("Connection:keep-alive")
    //	reqHeaders.push("Referer:http://currency.poe.trade")
    //	reqHeaders.push("Upgrade-Insecure-Requests:1")
    //
    //    html := cURL_Download(url, postData, reqHeaders, options, false)
    //
    //    return html
    //}    
  
    private String stdOutStream(String cmd) throws IOException {
        String line;  
        OutputStream stdin = null;  
        InputStream stderr = null;  
        InputStream stdout = null;  
        
        String stdOutString = "";
        String stdErrString = "";
        
        // launch EXE and grab stdin/stdout and stderr  
        Process process = Runtime.getRuntime ().exec (cmd);  
        stdin = process.getOutputStream ();  
        stderr = process.getErrorStream ();  
        stdout = process.getInputStream ();  

        // "write" the parms into stdin  
        //        line = "param1" + "\n";     
        //        stdin.write(line.getBytes() );  
        //        stdin.flush();  
        //
        //        line = "param2" + "\n";  
        //        stdin.write(line.getBytes() );  
        //        stdin.flush();  
        //
        //        line = "param3" + "\n";  
        //        stdin.write(line.getBytes() );  
        //        stdin.flush();  

        stdin.close();  

        // clean up if any output in stdout  
        BufferedReader brCleanUp =   
        new BufferedReader (new InputStreamReader (stdout));  
        while ((line = brCleanUp.readLine ()) != null) {  
        //System.out.println ("[Stdout] " + line);  
            stdOutString += line;
        }  
        brCleanUp.close();  

        // clean up if any output in stderr  
        brCleanUp =   
        new BufferedReader (new InputStreamReader (stderr));  
        while ((line = brCleanUp.readLine ()) != null) {  
        //System.out.println ("[Stderr] " + line);  
            stdErrString += line;
        }  
        brCleanUp.close();     
        
        return(stdOutString);
    }
    //RegExMatch can be used just like in ahk for ease of porting 
    // (match $1 in ahk is allMatches.get(1) here) => "indices not zero based", so to say
    // IMPORTANT: allMatches.get(0) contains the total number of matches in string-form
    // e.g. [0] = "<number of matches>", [1] is same as $1 in ahk :)
    public ArrayList<String> regExMatch(String string,String pattern) {
        ArrayList<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile(pattern).matcher(string);
        while (m.find()) {
            allMatches.add(m.group());
        }
        allMatches.add(0, ""+allMatches.size());
        return allMatches;
    }
    
    private class ReturnCurl {
        public boolean returnCurl_bool;
        public String returnCurl;
    }
    
    public void cURL_Download(  //TODO: check EACH AND EVERY PORTED REGEX!
            String url,String ioData, String[] ioHdr, String options, 
            boolean useFallback, boolean critical, boolean binaryDL, 
            String errorMsg, Object reqHeadersCurl, 
            boolean handleAccessForbidden, ReturnCurl returnCurl) {
    //the following variables need to be passed as Object 
    //  in order to modify the data directly inside the method (by calling a set-function of the objects), 
    //  instead of having to return them!
    //ByRef ioHdr
    //ByRef reqHeadersCurl
    //ByRef returnCurl
        for(int i=0;i<2;i++) {
        // Loop, 2 
        // {            
        // curl		:= """" PROGRAM.CURL_EXECUTABLE """ "
        // headers	:= ""
        // cookies	:= ""
        // For key, val in ioHdr {  
        //      val := Trim(RegExReplace(val, "i)(.*?)\s*:\s*(.*)", "$1:$2"))
        //	If (A_Index = 2 and RegExMatch(val, "i)^Host:.*")) {
        //          ; make sure that the host header is not included on the second try (empty first response)
        //      } Else {
        //          headers .= "-H """ val """ "	
        //	}		
        //		
        //	If (RegExMatch(val, "i)^Cookie:(.*)", cookie)) {
        //          cookies .= cookie1 " "		
        //	}
        // }
        // cookies := StrLen(cookies) ? "-b """ Trim(cookies) """ " : ""
            //TODO! let's see regarding curl -> whether we can use java-stuff instead!
            final String CURL_EXECUTABLE = "curl.exe";
            String curl = "\"" + CURL_EXECUTABLE + "\" ";
            String headers = "";
            String cookies = "";
            String redirect = "";
            
            int j = 1;
            for (String entry : ioHdr) {
                //In the replacement string, we can refer to captured groups from the regular expression. For example, the following expression removes instances of the HTML 'bold' tag from a string, but leaves the text inside the tag intact:
                //str = str.replaceAll("<b>([^<]*)</b>", "$1");                
                /* String key = entry.getKey(); */ String val = entry;
                val = val.replaceAll("i)(.*?)\\s*:\\s*(.*)", "$1:$2"); val = val.trim();
                if ((j==2) && val.matches("i)^Host:.*")) {
                    // make sure that the host header is not included on the second try (empty first response)
                } else {
                    headers += "-H \" "+val+" \" "; //i think: '-H " val " '      ==> TODO?
                }
                
                if (val.matches("i)^Cookie:(.*)"))
                    cookies += regExMatch(val,"i)^Cookie:(.*)").get(1)+" "; // TODO ? -> don't think it's supposed to be cookie1 - but maybe it's something i don't quite get (yet)
                    //cookies .= cookie1 " "
            }
            cookies = cookies.length() > 0 ? "-b \"" + cookies.trim() + "\" " : "";
            
            redirect = "L";
            boolean preventErrorMsg = false;
            int validateResponse = 1;
            String commandData = ""; //TODO ? -> dont know what that guys doing at this point -> drops it before using it in any way?
            String commandHdr = "";  // TODO ? => ^ same here...
            String requestType="";
            boolean skipRetHeaders=false;
            String savePath = "";
            int timeout = 0;
            if (options.length()>0) {
                for (String a_LoopField : options.split("\n")) { //HELLA FUCKING IMPORTANT! : The escape sequence \K is similar to a look-behind assertion because it causes any previously-matched characters to be omitted from the final matched string. For example, foo\Kbar matches "foobar" but reports that it has matched "bar".
                    if (a_LoopField.matches("?iSaveAs:[ \t]*[^\r\n]+")) { // TODO! -> "i)" means case insensitive matching -> need to check every ported RegEx again...
                        savePath = regExMatch(a_LoopField, "?iSaveAs:[ \t]*([^\r\n]+)").get(1);    //see https://alvinalexander.com/blog/post/java/java-how-case-insensitive-search-string-matches-method for explanation regarding "?i" => TODO?
                        commandData += " " + a_LoopField + " ";
                        commandHdr += "";
                    }
                    if (a_LoopField.matches("?iRedirect:\\sFalse"))
                        redirect = "";
                    if (a_LoopField.matches("?iPreventErrorMsg"))
                        preventErrorMsg = true;
                    if (a_LoopField.matches("?iRequestType:(.*)"))
                        requestType = regExMatch(a_LoopField,"?iRequestType:(.*)").get(1);
                    if (a_LoopField.matches("?iReturnHeaders:(.*skip.*)"))
                        skipRetHeaders = true;
                    if (a_LoopField.matches("?iTimeOut:(.*)"))
                        timeout = Integer.parseInt(regExMatch(a_LoopField,"?iTimeOut:(.*)").get(1).trim());
                    if (a_LoopField.matches("?i:ValidateResponse:(.*)"))
                        if (regExMatch(a_LoopField,"?iValidateResponse:(.*)").get(1).trim().equals("false"))
                            validateResponse = 0;
                }    
            }
            //		redirect := "L"
            //		PreventErrorMsg := false
            //		validateResponse := 1
            //		If (StrLen(options)) {
            //			Loop, Parse, options, `n 
            //			{
            //				If (RegExMatch(A_LoopField, "i)SaveAs:[ \t]*\K[^\r\n]+", SavePath)) {
            //					commandData	.= " " A_LoopField " "
            //					commandHdr	.= ""	
            //				}
            //				If (RegExMatch(A_LoopField, "i)Redirect:\sFalse")) {
            //					redirect := ""
            //				}
            //				If (RegExMatch(A_LoopField, "i)PreventErrorMsg")) {
            //					PreventErrorMsg := true
            //				}
            //				If (RegExMatch(A_LoopField, "i)RequestType:(.*)", match)) {
            //					requestType := Trim(match1)
            //				}
            //				If (RegExMatch(A_LoopField, "i)ReturnHeaders:(.*skip.*)")) {
            //					skipRetHeaders := true
            //				}
            //				If (RegExMatch(A_LoopField, "i)TimeOut:(.*)", match)) {
            //					timeout := Trim(match1)
            //				}
            //				If (RegExMatch(A_LoopField, "i)ValidateResponse:(.*)", match)) {
            //					If (Trim(match1) = "false") {
            //						validateResponse := 0
            //					}				
            //				}	
            //			}			
            //		}            
            
            if (!(timeout>0))
                timeout = 30;
            
            //Map <String,String> e = new HashMap<String,String>();
            try {
                commandData	= "";		// console curl command to return data/content 
    		commandHdr	= "";		// console curl command to return headers
                if (binaryDL) {
                    commandData += " -" + redirect + "Jkv ";	// save as file
                    if (savePath.length()>0) {
                        commandData += "-o " + savePath + " ";  // set target destination and name
                    }
                } else {
                    commandData += " -" + redirect + "ks --compressed ";
                    if (requestType.equals("GET")) {
                        commandHdr  += " -k" + redirect + "s ";
                    } else {
                        commandHdr  += " -I" + redirect + "ks ";
                    }
                }
                if (headers.length()>0) {
                    if (! requestType.equals("GET")) {
                        commandData += headers;
                        commandHdr  += headers;	
                    }				
                    if (cookies.length()>0) {
                        commandData += cookies;
                        commandHdr  += cookies;
                    }
                }
                if ((ioData.length()>0) && (!requestType.equals("GET"))) {
                    if (requestType.equals("POST")) {
                        commandData += "-X POST ";
                    }
                    commandData += "--data \"" + ioData + "\" ";
                } else if (ioData.length()>0) {
                    url += url + "?" + ioData;
                }
    			
                if (binaryDL) {
                    commandData	+= "--connect-timeout " + timeout + " ";
                    commandData	+= "--connect-timeout " + timeout + " ";
                } else {				
                    commandData	+= "--max-time " + timeout + " ";
                    commandHdr	+= "--max-time " + timeout + " ";
                }
                
                // get data
                String html	= stdOutStream(curl + "\"" + url + "\"" + commandData);
                //html := ReadConsoleOutputFromFile(commandData """" url """", "commandData") ; alternative function
    			
                if (returnCurl.returnCurl_bool) {
                    returnCurl.returnCurl = "curl " + "\"" + url + "\"" + commandData;
                }
    
                // get return headers in seperate request
                if (! binaryDL && ! skipRetHeaders) {
                    if ((ioData.length()>0) && (!requestType.equals("GET"))) {
                        commandHdr = curl + "\"" + url + "?" + ioData + "\"" + commandHdr;    // add payload to url since you can't use the -I argument with POST requests					
                    } else {
                        commandHdr = curl + "\"" + url + "\"" + commandHdr;
                    }
                    ioHdr = stdOutStream(commandHdr).split("\n");
                    //ioHrd := ReadConsoleOutputFromFile(commandHdr, "commandHdr") ; alternative function
                } else {
                    ioHdr = html.split("\n");
                }
    
                reqHeadersCurl = commandHdr;
            } catch (Exception e) {
    
            }
        }
    }
}
            // check if response has a good status code or is valid JSON (shouldn't be an erroneous response in that case)
            //ArrayList<String> goodStatusCode = regExMatch((String.join("",ioHdr)), "?iHTTP/1.1 (200 OK|302 Found)");
            
            //try {
            //    isJSON = isObject(JSON.Load(ioHdr))
    //		} Catch er {
    //			
    //		}
    //		
    //		If ((Strlen(ioHdr) and goodStatusCode) or (StrLen(ioHdr) and isJSON) or not validateResponse) {		
    //			Break	; only go into the second loop if the respone is empty or has a bad status code (possible problem with the added host header)
    //		}
    //	}
    //
    //	;goodStatusCode := RegExMatch(ioHdr, "i)HTTP\/1.1 (200 OK|302 Found)")
    //	If (RegExMatch(ioHdr, "i)HTTP\/1.1 403 Forbidden") and not handleAccessForbidden) {
    //		PreventErrorMsg		:= true
    //		handleAccessForbidden	:= "403 Forbidden"
    //	}
    //	If (!binaryDL) {
    //		; Use fallback download if curl fails
    //		If ((not goodStatusCode or e.what) and useFallback) {
    //			cURL_DownloadFallback(url, html, e, critical, ioHdr, PreventErrorMsg)
    //		} Else If (not goodStatusCode and e.what) {
    //			cURL_ThrowError(e, false, ioHdr, PreventErrorMsg)
    //		}
    //	}
    //	; handle binary file downloads
    //	Else If (not e.what) {
    //		; check returned request headers
    //		ioHdr := cURL_ParseReturnedHeaders(ioHdr)
    //		
    //		goodStatusCode := RegExMatch(ioHdr, "i)HTTP\/1.1 (200 OK|302 Found)")
    //		If (not goodStatusCode) {
    //			MsgBox, 16,, % "Error downloading file to " SavePath
    //			Return "Error: Wrong Status"
    //		}
    //		
    //		; compare file sizes
    //		FileGetSize, sizeOnDisk, %SavePath%
    //		RegExMatch(ioHdr, "i)Content-Length:\s(\d+)(k|m)?", size)
    //		size := Trim(size1)
    //		If (Strlen(size2)) {
    //			size := size2 = "k" ? size * 1024 : size * 1024 * 1024
    //			sizeVariation := Round(size * 99.8 / 100) - size
    //		}		
    //		
    //		; give the comparison some leeway in case of the extracted filesize from the response headers being 
    //		; imprecise (shown in kilobyte/megabyte)
    //		If (sizeVariation) {
    //			If (not (sizeOnDisk > (size - sizeVariation) and sizeOnDisk < (size + sizeVariation))) {
    //				html := "Error: Different Size"
    //			}
    //		} Else {
    //			If (size != sizeOnDisk) {
    //				html := "Error: Different Size"
    //			}
    //		}
    //	} Else {
    //		cURL_ThrowError(e, false, ioHdr, PreventErrorMsg)
    //	}
    //	
    //	Return html
    //}                    
            


            
    //}
    //
            
    //cURL_Download(url, ioData, ByRef ioHdr, options, useFallback = true, critical = false, binaryDL = false, errorMsg = "", ByRef reqHeadersCurl = "", handleAccessForbidden = true, ByRef returnCurl = false) {
    //	/*	Credits: POE-TradeMacro
    //		https://github.com/PoE-TradeMacro/POE-TradeMacro
    //
    //
    //		url		= download url
    //		ioData	= uri encoded postData 
    //		ioHdr	= array of request headers
    //		options	= multiple options separated by newline (currently only "SaveAs:",  "Redirect:true/false")
    //		
    //		useFallback = Use UrlDownloadToFile if curl fails, not possible for POST requests or when cookies are required 
    //		critical	= exit macro if download fails
    //		binaryDL	= file download (zip for example)
    //		errorMsg	= optional error message, will be added to default message
    //		reqHeadersCurl = returns the returned headers from the curl request 
    //		handleAccessForbidden = "true" throws an error message if "403 Forbidden" is returned, "false" prevents it, returning "403 Forbidden" to enable custom error handling
    //	*/
    //	global PROGRAM
    //
    //	; https://curl.haxx.se/download.html -> https://bintray.com/vszakats/generic/curl/
    //	Loop, 2 
    //	{
    //		curl		:= """" PROGRAM.CURL_EXECUTABLE """ "
    //		headers	:= ""
    //		cookies	:= ""
    //		For key, val in ioHdr {
    //			val := Trim(RegExReplace(val, "i)(.*?)\s*:\s*(.*)", "$1:$2"))
    //			If (A_Index = 2 and RegExMatch(val, "i)^Host:.*")) {    //this a_index is inside the nested loop, so it concerns the nested loop :)
    //				; make sure that the host header is not included on the second try (empty first response)
    //			} Else {
    //				headers .= "-H """ val """ "	
    //			}		
    //			
    //			If (RegExMatch(val, "i)^Cookie:(.*)", cookie)) {
    //				cookies .= cookie1 " "		
    //			}
    //		}
    //		cookies := StrLen(cookies) ? "-b """ Trim(cookies) """ " : ""
    //		
    //		redirect := "L"
    //		PreventErrorMsg := false
    //		validateResponse := 1
    //		If (StrLen(options)) {
    //			Loop, Parse, options, `n 
    //			{
    //				If (RegExMatch(A_LoopField, "i)SaveAs:[ \t]*\K[^\r\n]+", SavePath)) {
    //					commandData	.= " " A_LoopField " "
    //					commandHdr	.= ""	
    //				}
    //				If (RegExMatch(A_LoopField, "i)Redirect:\sFalse")) {
    //					redirect := ""
    //				}
    //				If (RegExMatch(A_LoopField, "i)PreventErrorMsg")) {
    //					PreventErrorMsg := true
    //				}
    //				If (RegExMatch(A_LoopField, "i)RequestType:(.*)", match)) {
    //					requestType := Trim(match1)
    //				}
    //				If (RegExMatch(A_LoopField, "i)ReturnHeaders:(.*skip.*)")) {
    //					skipRetHeaders := true
    //				}
    //				If (RegExMatch(A_LoopField, "i)TimeOut:(.*)", match)) {
    //					timeout := Trim(match1)
    //				}
    //				If (RegExMatch(A_LoopField, "i)ValidateResponse:(.*)", match)) {
    //					If (Trim(match1) = "false") {
    //						validateResponse := 0
    //					}				
    //				}	
    //			}			
    //		}
    //		If (not timeout) {
    //			timeout := 30
    //		}
    //
    //		e := {}
    //		Try {		
    //			commandData	:= ""		; console curl command to return data/content 
    //			commandHdr	:= ""		; console curl command to return headers
    //			If (binaryDL) {
    //				commandData .= " -" redirect "Jkv "		; save as file
    //				If (SavePath) {
    //					commandData .= "-o """ SavePath """ "	; set target destination and name
    //				}
    //			} Else {
    //				commandData .= " -" redirect "ks --compressed "
    //				If (requestType = "GET") {
    //					commandHdr  .= " -k" redirect "s "
    //				} Else {
    //					commandHdr  .= " -I" redirect "ks "
    //				}
    //			}			
    //			
    //			If (StrLen(headers)) {
    //				If (not requestType = "GET") {
    //					commandData .= headers
    //					commandHdr  .= headers	
    //				}				
    //				If (StrLen(cookies)) {
    //					commandData .= cookies
    //					commandHdr  .= cookies
    //				}
    //			}
    //			If (StrLen(ioData) and not requestType = "GET") {
    //				If (requestType = "POST") {
    //					commandData .= "-X POST "
    //				}
    //				commandData .= "--data """ ioData """ "
    //			} Else If (StrLen(ioData)) {
    //				url := url "?" ioData
    //			}
    //			
    //			If (binaryDL) {
    //				commandData	.= "--connect-timeout " timeout " "
    //				commandData	.= "--connect-timeout " timeout " "
    //			} Else {				
    //				commandData	.= "--max-time " timeout " "
    //				commandHdr	.= "--max-time " timeout " "
    //			}
    //
    //			; get data
    //			html	:= StdOutStream(curl """" url """" commandData)
    //			;html := ReadConsoleOutputFromFile(commandData """" url """", "commandData") ; alternative function
    //			
    //			If (returnCurl) {
    //				returnCurl := "curl " """" url """" commandData
    //			}
    //			
    //			; get return headers in seperate request
    //			If (not binaryDL and not skipRetHeaders) {
    //				If (StrLen(ioData) and not requestType = "GET") {
    //					commandHdr := curl """" url "?" ioData """" commandHdr		; add payload to url since you can't use the -I argument with POST requests					
    //				} Else {
    //					commandHdr := curl """" url """" commandHdr
    //				}
    //				ioHdr := StdOutStream(commandHdr)
    //				;ioHrd := ReadConsoleOutputFromFile(commandHdr, "commandHdr") ; alternative function
    //			} Else {
    //				ioHdr := html
    //			}
    //			
    //			reqHeadersCurl := commandHdr
    //		} Catch e {
    //
    //		}
    //		
    //		; check if response has a good status code or is valid JSON (shouldn't be an erroneous response in that case)
    //		goodStatusCode := RegExMatch(ioHdr, "i)HTTP\/1.1 (200 OK|302 Found)")
    //		Try {
    //			isJSON := isObject(JSON.Load(ioHdr))
    //		} Catch er {
    //			
    //		}
    //		
    //		If ((Strlen(ioHdr) and goodStatusCode) or (StrLen(ioHdr) and isJSON) or not validateResponse) {		
    //			Break	; only go into the second loop if the respone is empty or has a bad status code (possible problem with the added host header)
    //		}
    //	}
    //
    //	;goodStatusCode := RegExMatch(ioHdr, "i)HTTP\/1.1 (200 OK|302 Found)")
    //	If (RegExMatch(ioHdr, "i)HTTP\/1.1 403 Forbidden") and not handleAccessForbidden) {
    //		PreventErrorMsg		:= true
    //		handleAccessForbidden	:= "403 Forbidden"
    //	}
    //	If (!binaryDL) {
    //		; Use fallback download if curl fails
    //		If ((not goodStatusCode or e.what) and useFallback) {
    //			cURL_DownloadFallback(url, html, e, critical, ioHdr, PreventErrorMsg)
    //		} Else If (not goodStatusCode and e.what) {
    //			cURL_ThrowError(e, false, ioHdr, PreventErrorMsg)
    //		}
    //	}
    //	; handle binary file downloads
    //	Else If (not e.what) {
    //		; check returned request headers
    //		ioHdr := cURL_ParseReturnedHeaders(ioHdr)
    //		
    //		goodStatusCode := RegExMatch(ioHdr, "i)HTTP\/1.1 (200 OK|302 Found)")
    //		If (not goodStatusCode) {
    //			MsgBox, 16,, % "Error downloading file to " SavePath
    //			Return "Error: Wrong Status"
    //		}
    //		
    //		; compare file sizes
    //		FileGetSize, sizeOnDisk, %SavePath%
    //		RegExMatch(ioHdr, "i)Content-Length:\s(\d+)(k|m)?", size)
    //		size := Trim(size1)
    //		If (Strlen(size2)) {
    //			size := size2 = "k" ? size * 1024 : size * 1024 * 1024
    //			sizeVariation := Round(size * 99.8 / 100) - size
    //		}		
    //		
    //		; give the comparison some leeway in case of the extracted filesize from the response headers being 
    //		; imprecise (shown in kilobyte/megabyte)
    //		If (sizeVariation) {
    //			If (not (sizeOnDisk > (size - sizeVariation) and sizeOnDisk < (size + sizeVariation))) {
    //				html := "Error: Different Size"
    //			}
    //		} Else {
    //			If (size != sizeOnDisk) {
    //				html := "Error: Different Size"
    //			}
    //		}
    //	} Else {
    //		cURL_ThrowError(e, false, ioHdr, PreventErrorMsg)
    //	}
    //	
    //	Return html
    //}    
//}
