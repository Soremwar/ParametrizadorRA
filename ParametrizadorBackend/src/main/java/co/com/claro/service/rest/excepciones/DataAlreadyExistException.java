package co.com.claro.service.rest.excepciones;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andres
 */
public class DataAlreadyExistException extends RuntimeException{
    
    
    public DataAlreadyExistException() {
        super();
    }
    public DataAlreadyExistException(String msg)   {
        super(msg);
    }
    public DataAlreadyExistException(String msg, Exception e)  {
        super(msg, e);
    }
    
}
