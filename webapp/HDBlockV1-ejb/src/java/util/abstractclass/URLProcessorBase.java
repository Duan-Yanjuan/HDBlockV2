/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.abstractclass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


/**
 *
 * @author David
 */
public abstract class URLProcessorBase implements URLProcessor {
    
    public void process(URL url) throws IOException {
        
        /*HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      //  URLConnection urlConnection =  url.openConnection();
        InputStream input = urlConnection.getInputStream();
        
        urlConnection.setRequestMethod("GET");*/
        
    URLConnection yc = url.openConnection();
    yc.setRequestProperty("Accept", "application/xml");
    InputStream input = yc.getInputStream();
    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
    String inputLine;
    while ((inputLine = in.readLine()) != null) 
        System.out.println(inputLine);
    in.close();
        
        
        
        
        //urlConnection.setR
        

        try{
            processURLData(input);
        } finally {
            input.close();
        }
    }

    protected abstract void processURLData(InputStream input)
        throws IOException;

    
}
