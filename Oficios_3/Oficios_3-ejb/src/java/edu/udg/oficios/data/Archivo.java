/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public class Archivo implements Serializable {

    //Valores insertados en la tabla de OFICIO_ARCHIVO
    private Long archivoId;
    private String usuario_destino;
    private String departamento_destino;
    private String archivo_saludo;
    private String archivo_cuerpo;
    private String archivo_despedida;
    private String usuario_emirsor;
    private String departamento_emirsor;

    //Valores insertados en la tabla de OFICIO
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
    private Long oficio_archivo;
    private int oficio_tipo;
    private int oficio_version;
    private int oficio_borrador;
    private int oficio_bandera_vencimiento;

    //Constructores
    public Archivo() {
    }

    //Getter and Setter
    public Long getArchivoId() {
	return archivoId;
    }

    public void setArchivoId(Long archivoId) {
	this.archivoId = archivoId;
    }

    public String getUsuario_destino() {
	return usuario_destino;
    }

    public void setUsuario_destino(String usuario_destino) {
	this.usuario_destino = usuario_destino;
    }

    public String getDepartamento_destino() {
	return departamento_destino;
    }

    public void setDepartamento_destino(String departamento_destino) {
	this.departamento_destino = departamento_destino;
    }

    public String getArchivo_saludo() {
	return archivo_saludo;
    }

    public void setArchivo_saludo(String archivo_saludo) {
	this.archivo_saludo = archivo_saludo;
    }

    public String getArchivo_cuerpo() {
	return archivo_cuerpo;
    }

    public void setArchivo_cuerpo(String archivo_cuerpo) {
	this.archivo_cuerpo = archivo_cuerpo;
    }

    public String getArchivo_despedida() {
	return archivo_despedida;
    }

    public void setArchivo_despedida(String archivo_despedida) {
	this.archivo_despedida = archivo_despedida;
    }

    public String getUsuario_emirsor() {
	return usuario_emirsor;
    }

    public void setUsuario_emirsor(String usuario_emirsor) {
	this.usuario_emirsor = usuario_emirsor;
    }

    public String getDepartamento_emirsor() {
	return departamento_emirsor;
    }

    public void setDepartamento_emirsor(String departamento_emirsor) {
	this.departamento_emirsor = departamento_emirsor;
    }

    public void setArchivoId(String idCreado) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public void setPriori(String priori) {
	this.priori = priori;
    }

    public Long getOficio_archivo() {
	return oficio_archivo;
    }

    public void setOficio_archivo(Long oficio_archivo) {
	this.oficio_archivo = oficio_archivo;
    }

    public int getOficio_tipo() {
	return oficio_tipo;
    }

    public void setOficio_tipo(int oficio_tipo) {
	this.oficio_tipo = oficio_tipo;
    }

    public int getOficio_version() {
	return oficio_version;
    }

    public void setOficio_version(int oficio_version) {
	this.oficio_version = oficio_version;
    }

    public int getOficio_borrador() {
	return oficio_borrador;
    }

    public void setOficio_borrador(int oficio_borrador) {
	this.oficio_borrador = oficio_borrador;
    }

    public int getOficio_bandera_vencimiento() {
	return oficio_bandera_vencimiento;
    }

    public void setOficio_bandera_vencimiento(int oficio_bandera_vencimiento) {
	this.oficio_bandera_vencimiento = oficio_bandera_vencimiento;
    }
    
    

}
