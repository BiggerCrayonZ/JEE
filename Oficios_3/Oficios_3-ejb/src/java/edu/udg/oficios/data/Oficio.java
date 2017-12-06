/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.data;


import java.io.Serializable;
import java.util.Date;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public class Oficio implements Serializable {

    private Long oficioId;
    private String folio;
    private String asunto;
    private Date fecha_oficio;
    private Date fecha_vencimiento;
    private String observaciones;
    private int layer_id;
    private String estatus;
    private int class_id;
    private String clase;
    private int prioridad_id;
    private String priori;
    private static String[] estatusrowstatic = {"Nuevo", "En Proceso", "En Espera", "Suspendido", "Termiando", "Cancelado"};
    private Long archivoId;
    private int oficio_tipo;
    private String oficio_tipo_string;
    private int oficio_borrador;
    private String oficio_borrador_string;
    private int oficio_version;
    private int oficio_bandera_vencimiento;

    public Oficio() {
    }

    public Long getOficioId() {
	return oficioId;
    }

    public void setOficioId(Long oficioId) {
	this.oficioId = oficioId;
    }

    public String getFolio() {
	return folio;
    }

    public void setFolio(String folio) {
	this.folio = folio;
    }

    public String getAsunto() {
	return asunto;
    }

    public void setAsunto(String asunto) {
	this.asunto = asunto;
    }

    public Date getFecha_oficio() {
	return fecha_oficio;
    }

    public void setFecha_oficio(Date fecha_oficio) {
	this.fecha_oficio = fecha_oficio;
    }

    public Date getFecha_vencimiento() {
	return fecha_vencimiento;
    }

    public void setFecha_vencimiento(Date fecha_vencimiento) {
	this.fecha_vencimiento = fecha_vencimiento;
    }

    public String getObservaciones() {
	return observaciones;
    }

    public void setObservaciones(String observaciones) {
	this.observaciones = observaciones;
    }

    public int getLayer_id() {
	return layer_id;
    }

    public void setLayer_id(int layer_id) {
	this.layer_id = layer_id;
    }

    public String getEstatus() {
	return estatus;
    }

    public void setEstatus(String estatus) {
	this.estatus = estatus;
    }

    public List<String> getEstatusrowstatic() {
	return Arrays.asList(estatusrowstatic);
    }

    public int getClass_id() {
	return class_id;
    }

    public void setClass_id(int class_id) {
	this.class_id = class_id;
    }

    public String getClase() {
	return clase;
    }

    public void setClase(String clase) {
	this.clase = clase;
    }

    public int getPrioridad_id() {
	return prioridad_id;
    }

    public void setPrioridad_id(int prioridad_id) {
	this.prioridad_id = prioridad_id;
    }

    public String getPriori() {
	return priori;
    }

    public void setPrioridad(String priori) {
	this.priori = priori;
    }

    public Long getArchivoId() {
	return archivoId;
    }

    public void setArchivoId(Long archivoId) {
	this.archivoId = archivoId;
    }

    public int getOficio_tipo() {
	return oficio_tipo;
    }

    public void setOficio_tipo(int oficio_tipo) {
	this.oficio_tipo = oficio_tipo;
    }

    public String getOficio_tipo_string() {
	return oficio_tipo_string;
    }

    public void setOficio_tipo_string(String oficio_tipo_string) {
	this.oficio_tipo_string = oficio_tipo_string;
    }

    public int getOficio_borrador() {
	return oficio_borrador;
    }

    public void setOficio_borrador(int oficio_borrador) {
	this.oficio_borrador = oficio_borrador;
    }

    public String getOficio_borrador_string() {
	return oficio_borrador_string;
    }

    public void setOficio_borrador_string(String oficio_borrador_string) {
	this.oficio_borrador_string = oficio_borrador_string;
    }

    public int getOficio_version() {
	return oficio_version;
    }

    public void setOficio_version(int oficio_version) {
	this.oficio_version = oficio_version;
    }

    public int getOficio_bandera_vencimiento() {
	return oficio_bandera_vencimiento;
    }

    public void setOficio_bandera_vencimiento(int oficio_bandera_vencimiento) {
	this.oficio_bandera_vencimiento = oficio_bandera_vencimiento;
    }

}
