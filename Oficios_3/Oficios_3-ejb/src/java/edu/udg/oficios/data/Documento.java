/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.udg.oficios.data;

import java.io.Serializable;

/**
 * 
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public class Documento implements Serializable {
    private long adjuntoid;
    private String nombre;
    private byte[] documento;
    private long oficioid;
    
    public Documento(){
	
    }

    public long getAdjuntoid() {
	return adjuntoid;
    }

    public void setAdjuntoid(long adjuntoid) {
	this.adjuntoid = adjuntoid;
    }


    public String getNombre() {
	return nombre;
    }

    public void setNombre(String nombre) {
	this.nombre = nombre;
    }

    public byte[] getDocumento() {
	return documento;
    }

    public void setDocumento(byte[] documento) {
	this.documento = documento;
    }

    public long getOficioid() {
	return oficioid;
    }

    public void setOficioid(long oficioid) {
	this.oficioid = oficioid;
    }
    
    

}
