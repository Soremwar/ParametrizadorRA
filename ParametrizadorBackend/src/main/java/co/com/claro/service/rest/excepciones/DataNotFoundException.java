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
public class DataNotFoundException extends RuntimeException{
    
    public DataNotFoundException() {
        super();
    }
    public DataNotFoundException(String msg)   {
        super(msg);
    }
    public DataNotFoundException(String msg, Exception e)  {
        super(msg, e);
    }
    
}
