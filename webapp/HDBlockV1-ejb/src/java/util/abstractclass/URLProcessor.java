/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.abstractclass;

import java.io.IOException;
import java.net.URL;

/**
 *
 * @author David
 */
public interface URLProcessor {
    public void process(URL url) throws IOException;
}
