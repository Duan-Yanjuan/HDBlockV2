/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.abstractclass;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author David
 */
public class URLProcessorImpl extends URLProcessorBase{
    
    @Override
    protected void processURLData(InputStream input) throws IOException {
        int data = input.read();
        while(data != -1){
            System.out.println((char) data);
            data = input.read();
        }
    }
}
