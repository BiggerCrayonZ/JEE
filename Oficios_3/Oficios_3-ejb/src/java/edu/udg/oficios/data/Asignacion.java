/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.udg.oficios.data;

import java.util.Date;

/**
 * 
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public class Asignacion {
    
    //Campos
    private Long asig_id;
    private Long asig_oficio_id;
    private String asig_emisor;
    private String asig_dep_emisor;
    private String asig_asignado;
    private String asig_dep_asignado;
    private String asig_tipo;
    private Date asig_fecha;
    
    //Constructor

    public Asignacion() {
    }
    
    
    //Entradas y salidas

    public Long getAsig_id() {
	return asig_id;
    }

    public void setAsig_id(Long asig_id) {
	this.asig_id = asig_id;
    }

    public Long getAsig_oficio_id() {
	return asig_oficio_id;
    }

    public void setAsig_oficio_id(Long asig_oficio_id) {
	this.asig_oficio_id = asig_oficio_id;
    }

    public String getAsig_emisor() {
	return asig_emisor;
    }

    public void setAsig_emisor(String asig_emisor) {
	this.asig_emisor = asig_emisor;
    }

    public String getAsig_dep_emisor() {
	return asig_dep_emisor;
    }

    public void setAsig_dep_emisor(String asig_dep_emisor) {
	this.asig_dep_emisor = asig_dep_emisor;
    }

    public String getAsig_asignado() {
	return asig_asignado;
    }

    public void setAsig_asignado(String asig_asignado) {
	this.asig_asignado = asig_asignado;
    }

    public String getAsig_dep_asignado() {
	return asig_dep_asignado;
    }

    public void setAsig_dep_asignado(String asig_dep_asignado) {
	this.asig_dep_asignado = asig_dep_asignado;
    }

    public String getAsig_tipo() {
	return asig_tipo;
    }

    public void setAsig_tipo(String asig_tipo) {
	this.asig_tipo = asig_tipo;
    }

    public Date getAsig_fecha() {
	return asig_fecha;
    }

    public void setAsig_fecha(Date asig_fecha) {
	this.asig_fecha = asig_fecha;
    }
    

}
