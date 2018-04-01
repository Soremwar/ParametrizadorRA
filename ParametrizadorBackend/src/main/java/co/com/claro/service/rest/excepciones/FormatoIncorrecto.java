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
public class FormatoIncorrecto extends RuntimeException{

    
    public FormatoIncorrecto() {
        super();
    }
    public FormatoIncorrecto(String msg)   {
        super(msg);
    }
    public FormatoIncorrecto(String msg, Exception e)  {
        super(msg, e);
    }
    
}
