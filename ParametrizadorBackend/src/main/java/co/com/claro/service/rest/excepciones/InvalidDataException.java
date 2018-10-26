package co.com.claro.service.rest.excepciones;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andres
 */
public class InvalidDataException extends RuntimeException{
    
    public InvalidDataException() {
        super();
    }
    public InvalidDataException(String msg)   {
        super(msg);
    }
    public InvalidDataException(String msg, Exception e)  {
        super(msg, e);
    }
    
}
