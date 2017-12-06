/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.view;

import edu.udg.core.data.model.Access;
import edu.udg.core.portal.util.Utileria;
import edu.udg.core.data.model.Message;
import edu.udg.core.data.model.Unity;
import edu.udg.core.data.model.User;
import edu.udg.core.remote.ejb.Service;
import edu.udg.oficios.data.Asignacion;
import edu.udg.oficios.data.Oficio;
import edu.udg.oficios.ejb.AsignacionEjbLocal;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
@Named(value = "asignacionOA")
@ViewScoped
public class AsignacionOA implements Serializable {

    //Campos
    private final Utileria util;
    private Service s;
    private String rolEntrada;
    private String APPID;
    private String TOKEN;
    private String ROLID;
    private User USER;
    private String USER_NAME;
    private String ROL;
    private String FECHA;

    //Permisos
    private boolean permisoVerTipo;
    private boolean permisoAsignacion;
    private boolean AsignacionOficial;

    @EJB
    private AsignacionEjbLocal asignacionEJB;
    private Message msj;
    private Oficio seleccion;
    private Long idSeleccionado;

    //Data
    private Asignacion asignacion;
    private Oficio oficioSeleccionado;

    //Listas
    private List<Asignacion> listaAsigndosByOficio;
    private List<String> opcionesEntidadesEmisor;

    //Constructor
    public AsignacionOA() {
	this.util = new Utileria();
	System.out.println("TEST UTILERIA");
    }

    @PostConstruct
    public void iniciaAsignacion() {
	s = new Service();
	Access acceso = util.obtenerAccesoSesion();
	String appId = acceso.getAppId();
	APPID = appId;
	String token = acceso.getToken();
	TOKEN = token;
	String rolid = acceso.getRolId();
	ROLID = rolid;
	msj = new Message();
	//Inicializar asignación
	iniciarAsignacion();
	//Inicializamos el oficio por seleccion
	iniciarOficioSeleccionado();
	seleccion = new Oficio();
	rolEntrada = util.obtenerAccesoSesion().getRolId();
	iniciarListaPorOficio();
	validarRol();
	//Llenar campos para la validación de asignaciones
	validarCamposEmisor();
	validacionUsuario();
	validarFecha();
	//Validar permisos
	validarPermisoAsignar();
	validarPermisoAsigacionOficial();
	System.out.println("### METODO PARA INICIAR ASIGNACION-OA");
    }

    public void HandleCargarAsignaciones(ActionEvent event) {
	validarOficioSeleccionado();
    }
    
    public void HandleNuevaAsignacion(ActionEvent event){
	System.out.println("CONFIRMADO");
    }

    private void iniciarListaPorOficio() {
	listaAsigndosByOficio = new ArrayList<>();
	System.out.println("LISTA POR OFICIO INICIALIZADA");
    }

    public Oficio getSeleccion() {
	return seleccion;
    }

    public void setSeleccion(Oficio seleccion) {
	this.seleccion = seleccion;
    }

    public Asignacion getAsignacion() {
	return asignacion;
    }

    public void setAsignacion(Asignacion asignacion) {
	this.asignacion = asignacion;
    }

    public List<Asignacion> getListaAsigndosByOficio() {
	return listaAsigndosByOficio;
    }

    public void setListaAsigndosByOficio(List<Asignacion> listaAsigndosByOficio) {
	this.listaAsigndosByOficio = listaAsigndosByOficio;
    }

    public boolean isPermisoVerTipo() {
	return permisoVerTipo;
    }

    public void setPermisoVerTipo(boolean permisoVerTipo) {
	this.permisoVerTipo = permisoVerTipo;
    }

    public String getRolEntrada() {
	return rolEntrada;
    }

    public void setRolEntrada(String rolEntrada) {
	this.rolEntrada = rolEntrada;
    }

    public String getAPPID() {
	return APPID;
    }

    public void setAPPID(String APPID) {
	this.APPID = APPID;
    }

    public String getTOKEN() {
	return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
	this.TOKEN = TOKEN;
    }

    public String getROLID() {
	return ROLID;
    }

    public void setROLID(String ROLID) {
	this.ROLID = ROLID;
    }

    public User getUSER() {
	return USER;
    }

    public void setUSER(User USER) {
	this.USER = USER;
    }

    public String getUSER_NAME() {
	return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
	this.USER_NAME = USER_NAME;
    }

    public String getROL() {
	return ROL;
    }

    public void setROL(String ROL) {
	this.ROL = ROL;
    }

    public List<String> getOpcionesEntidadesEmisor() {
	return opcionesEntidadesEmisor;
    }

    public void setOpcionesEntidadesEmisor(List<String> opcionesEntidadesEmisor) {
	this.opcionesEntidadesEmisor = opcionesEntidadesEmisor;
    }

    public boolean isPermisoAsignacion() {
	return permisoAsignacion;
    }

    public void setPermisoAsignacion(boolean permisoAsignacion) {
	this.permisoAsignacion = permisoAsignacion;
    }

    public Oficio getOficioSeleccionado() {
	return oficioSeleccionado;
    }

    public void setOficioSeleccionado(Oficio oficioSeleccionado) {
	this.oficioSeleccionado = oficioSeleccionado;
    }

    public String getFECHA() {
	return FECHA;
    }

    public void setFECHA(String FECHA) {
	this.FECHA = FECHA;
    }
    
    

    private void validarOficioSeleccionado() {
	if (seleccion != null) {
	    System.out.println("OFICIO SELECCIONADO");
	    metodoTomaDatosOficio(seleccion.getOficioId());
	} else {
	    System.out.println("OFICIO NO SELECCIONADO");
	}
    }

    private void mandarGrowl(boolean enviarParametro, boolean mostrarMensaje) {
	if (msj.isStatus()) {
	    if (mostrarMensaje) {
		util.mandarGrowl(FacesMessage.SEVERITY_INFO, "", msj.getMessage());
	    }
	} else {
	    util.mandarGrowl(FacesMessage.SEVERITY_ERROR, "", msj.getMessage());
	}

	if (enviarParametro) {
	    RequestContext.getCurrentInstance().addCallbackParam("cerrar", msj.isStatus());
	}
    }

    private void metodoTomaDatosOficio(Long id) {
	System.out.println("ID: " + id);
	if (id != null) {
	    obtenerAsignacionesByOficio(id);
	} else {
	    System.out.println("ERROR DE SELECCION");
	}
    }

    private void obtenerAsignacionesByOficio(Long idAsignacion) {
	msj = asignacionEJB.buscarByOficio(idAsignacion);
	if (msj.isStatus()) {
	    listaAsigndosByOficio = (List<Asignacion>) msj.getObject();
	} else {
	    mandarGrowl(true, true);
	}
    }

    private void validarRol() {
	if (rolEntrada.equalsIgnoreCase("SOP_ADMIN")) {
	    permisoVerTipo = true;
	} else {
	    permisoVerTipo = false;
	}
	System.out.println("PERMISO VER TIPO: " + permisoVerTipo);
    }

    private void validarCamposEmisor() {
	opcionesEntidadesEmisor = new ArrayList<>();
	try {
	    Message msg = s.obtenerUnidadesApp(TOKEN, APPID);
	    List<Unity> entidades = (List<edu.udg.core.data.model.Unity>) msg.getObject();
	    for (Unity ent : entidades) {
		opcionesEntidadesEmisor.add(ent.getLabel());
		System.out.println("ENT: " + ent.getLabel() + "Añadido");
	    }
	    System.out.println("OPCIONES: " + opcionesEntidadesEmisor);
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void validacionUsuario() {
	USER = util.obtenerAccesoSesion().getUser();
	System.out.println("USUARIO: " + USER);
	String nombreUser = USER.getName();
	USER_NAME = USER.getName();
	System.out.println("USUARIO NOMBRE: " + nombreUser);
	System.out.println("USUARIO ROL: " + ROL);

	//Instanciamos en la asignación 
	asignacion.setAsig_emisor(USER_NAME);
    }

    private void iniciarAsignacion() {
	asignacion = new Asignacion();
	System.out.println("ASIGNACIÓN LISTA");
    }

    private void validarPermisoAsignar() {
	if (rolEntrada.equalsIgnoreCase("SOP_ADMIN")) {
	    permisoAsignacion = true;
	    AsignacionOficial = true;
	} else if (rolEntrada.equalsIgnoreCase("SOP_OFICPART")) {
	    permisoAsignacion = true;
	    AsignacionOficial = true;
	} else if (rolEntrada.equalsIgnoreCase("SOP_OFICWRITER")) {
	    permisoAsignacion = true;
	    AsignacionOficial = false;
	} else if (rolEntrada.equalsIgnoreCase("SOP_OFICREADER")) {
	    permisoAsignacion = false;
	    AsignacionOficial = false;
	}
	System.out.println("PERMISO ASIGNAR: " + permisoAsignacion);
    }

    private void iniciarOficioSeleccionado() {
	oficioSeleccionado = new Oficio();
	System.out.println("OFICIO SELECCIONADO LISTO");
    }

    private void validarPermisoAsigacionOficial() {
	if (AsignacionOficial == true) {
	    asignacion.setAsig_tipo("1");
	    System.out.println("TIPO DE ASIGNADO: OFICIAL ");
	} else if (AsignacionOficial == false) {
	    asignacion.setAsig_tipo("0");
	    System.out.println("TIPO DE ASIGNADO: BORRADOR ");
	}

    }

    private void validarFecha() {
	Date today;
	SimpleDateFormat formatter;
	SimpleDateFormat formatterOfic;
	formatter = new SimpleDateFormat("dd/MM/yy");
	formatterOfic = new SimpleDateFormat("yyyyMMdd");
	today = new Date();
	//Seteamos en la asginación
	FECHA = formatter.format(today);
	asignacion.setAsig_fecha(today);
    }

}
