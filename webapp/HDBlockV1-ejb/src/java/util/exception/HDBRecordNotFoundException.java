/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author steph
 */
public class HDBRecordNotFoundException extends Exception{
     public HDBRecordNotFoundException() {
    }

    public HDBRecordNotFoundException(String message) {
        super(message);
    }
}
