/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.dao;

/**
 *
 * 
 */
public class DAOEXCEPTION extends RuntimeException {

    //Declaramos constantes en la implementación
    private static final long serialVersionUID = 1L;

    //Contructores
    //Constructor de recepción nula
//    public DAOEXCEPTION() {
//    }
    //Retorna el mensaje bajo concepto de la superclase
    public DAOEXCEPTION(String message) {
	super(message);
    }
    
    
    public DAOEXCEPTION(Throwable cause) {
	super(cause);
    }

}
