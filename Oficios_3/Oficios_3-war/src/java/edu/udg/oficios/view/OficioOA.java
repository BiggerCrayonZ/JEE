package edu.udg.oficios.view;

import edu.udg.oficios.ejb.Oficio_EJB;
import edu.udg.core.data.model.Access;
import edu.udg.core.data.model.Message;
import edu.udg.core.data.model.User;
import edu.udg.core.portal.util.Utileria;
import edu.udg.core.remote.ejb.Service;
import edu.udg.oficios.data.Archivo;
import edu.udg.oficios.data.Oficio;
import edu.udg.oficios.data.Documento;
import edu.udg.oficios.ejb.OficioEjbLocal;
import edu.udg.oficios.ejb.archivoEjb;
import edu.udg.oficios.ejb.archivoEjbLocal;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import edu.udg.core.data.model.Unity;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

//@ManagedBean(name ="OA")
@Named("OficioOA")
@ViewScoped
public class OficioOA implements Serializable {

    //Servicios
    private User USER;
    private String USER_NAME;
    private String rolEntrada;
    private List<String> opcionesEntidades;

    //VARIABLE SUPER GLOBALES
    private String FECHA;
    private String CLASE;
    private String PRIORI;
    private int VERSION;
    //Variables de segundo plano
    private Oficio selectId;
    private Archivo selectArchivoId;
    private Documento selectDocumentId;
    private StreamedContent documentoDescargado;
    private Long tempArchivoId;
    private List<Oficio> selectIds;
    private String accion;
    private final Utileria util;
    private Oficio oficio;
    private Archivo archivo;
    private List<Oficio> listaOficios;
    private static final String path_dir = "reportes";
    @EJB
    private OficioEjbLocal oficioEjb;
    private archivoEjbLocal archivoEJB;
    private Message msj;
    private boolean filtro1 = true;
    private boolean filtro2 = false;
    private boolean filtro3 = false;
    private boolean permisoListOficInst;
    private boolean permisoAgregar;
    private boolean permisoAñadir;
    private boolean permisoEliminar;
    private int tabindex = 0;
    private long idUnico;
    private long idUnicoDocumento;
    //Dedicados a subir un documento
    private Documento documento;
    private UploadedFile file;
    //Banderas
    private boolean editionButton = false;
    private boolean showdetails = false;
    private boolean previewButton = false;
    private boolean finishOfic = false;
    private boolean fechaVencimiento = false;

    public OficioOA() {
	this.util = new Utileria();
	System.out.println("test");
    }

    @PostConstruct
    public void inicia() {
	Service s = new Service();
	Service sBuscar = new Service();
	Access acceso = util.obtenerAccesoSesion();
	String appId = acceso.getAppId();
	String token = acceso.getToken();
	String rolid = acceso.getRolId();

	inicializarOficio();
	listaOficios = new ArrayList<>();
	selectIds = new ArrayList<>();
	selectId = new Oficio();
	selectDocumentId = new Documento();
	selectArchivoId = new Archivo();
	msj = new Message();
	oficioEjb = new Oficio_EJB();
	archivoEJB = new archivoEjb();
	obtenerOficios();
	permisoListOficInst = s.validaAction(token, appId, util.obtenerModulo().replace("/", ""), "LISTCLASIFYOFIC");
	permisoAgregar = s.validaAction(token, appId, util.obtenerModulo().replace("/", ""), "CAPTURAROFICIO");
	System.out.println("PERMISO AGREGAR: " + permisoAgregar);
	System.out.println("### Iniciar APLICACION de CARGA EH INICIALIZACIÓN DE OFICIOS");
	String ROL = util.obtenerAccesoSesion().getRolId();
	rolEntrada = ROL;
	validarPermisoAñadirOficio();
	validarPermisoEliminarOficio();
	
	opcionesEntidades = new ArrayList<>();
	try {
	    Message msg = s.obtenerUnidadesApp(token, appId);
	    List<Unity> entidades = (List<edu.udg.core.data.model.Unity>) msg.getObject();
	    for (Unity ent : entidades) {
		opcionesEntidades.add(ent.getLabel());
		System.out.println("ENT: " + ent.getLabel() + "Añadido");
	    }
	    System.out.println("OPCIONES: " + opcionesEntidades);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	USER = util.obtenerAccesoSesion().getUser();
	System.out.println("USUARIO: " + USER);
	String nombreUser = USER.getName();
	USER_NAME = USER.getName();
	System.out.println("USUARIO NOMBRE: " + nombreUser);
	System.out.println("USUARIO ROL: " + ROL);

    }

    // - - - - - - - - Métodos de acceso a datos - - - - - - - - - - - -
    private void obtenerOficios() {
	msj = oficioEjb.buscasTodos();
	if (msj.isStatus()) {
	    listaOficios = (List<Oficio>) msj.getObject();
	} else {
	    mandarGrowl(true, true);
	}
    }

    private void agregarOficio() {
	oficio.setOficio_tipo(0);
	validarOficioBorrador();
	msj = oficioEjb.agregar(oficio);
	if (msj.isStatus()) {
	    msj.setMessage("¡Oficio Adjuntado con Exito!");
	    mandarGrowl(true, true);
	} else {
	    mandarGrowl(true, true);
	}

    }

    private void eliminarOficio() {
	System.out.println("//////  ELIMINANDO OFICIO  ////////");
	int tmpTipo = selectId.getOficio_tipo();
	System.out.println("OFICIO TIPO SELECCIONADO: " + tmpTipo);
	if (tmpTipo == 1) {
	    selectArchivoId.setArchivoId(selectId.getArchivoId());
	    System.out.println("ARCH ID: " + selectDocumentId.getOficioid());
	    //
	    msj = archivoEJB.eliminar(selectArchivoId);
	    msj = oficioEjb.eliminar(selectId);
	} else if (tmpTipo == 0) {
	    this.selectDocumentId.setOficioid(this.selectId.getOficioId());
	    System.out.println("DOC ID: " + selectDocumentId);
	    //
	    msj = oficioEjb.eliminar(selectId);
	    System.out.println("PREVIO A BORRAR EL ID: " + this.selectDocumentId.getOficioid());
	    msj = oficioEjb.eliminarDocumento(selectDocumentId);

	}

    }

    private void modificarOficio() {
	msj = oficioEjb.modificar(oficio);
    }

    private void borradoMultipleOficio() {
	msj = oficioEjb.borradoMultiple(selectIds);
    }

    private void cargarUnicoOficio() {
	msj = oficioEjb.BuscarID(idUnico);
	oficio = (Oficio) msj.getObject();
    }

    private void cargaClasificacion(String estatus) {
	msj = oficioEjb.cargarPorEstatus(estatus);
	if (msj.isStatus()) {
	    listaOficios = (List<Oficio>) msj.getObject();
	} else {
	    mandarGrowl(true, true);
	}
    }

    private void cargarDocumento() {
	msj = oficioEjb.cargarDocumento(documento);
    }

    private void cargarUnicoDocumento() throws IOException {
	idUnicoDocumento = selectId.getOficioId();
	System.out.println("## entrando a cargarUnicoDocumento ID: " + idUnicoDocumento);
	msj = oficioEjb.buscarDocumento(idUnicoDocumento);
	Documento docuTemp;
	docuTemp = (Documento) msj.getObject();
	byte[] sink = docuTemp.getDocumento();
	InputStream stream = new ByteArrayInputStream(sink);
	documentoDescargado = new DefaultStreamedContent(stream, "application/pdf");
    }

    // - - - - - - - - - Métodos de validación - - - - - - - - - - - -
    private void validarAgregarOficio() {

	System.out.println("??Validamos Oficios al agregar ??");
	System.out.println("VERSION = 1");
	this.oficio.setOficio_version(1);
	msj = validarDatosOficio();
	if (msj.isStatus()) {
	    agregarOficio();
	} else {
	    mandarGrowl(true, true);
	}
    }

    private void validarCargarUnicoOficio() {
	msj = validarIdUnico();
	if (msj.isStatus()) {
	    cargarUnicoOficio();
	} else {
	    System.out.println("NO SE PUDO REALIZAR VALIDACIÓN");
	}
    }

    private void validarargaUnicoDocumento() throws IOException {
	msj = validarIdUnicoDocumento();
	if (msj.isStatus()) {
	    cargarUnicoDocumento();
	} else {
	    System.out.println("NO SE PUDO REALIZAR VALIDACIÓN");
	}
    }

    private void validarEliminarOficio() {
	msj = validarIdOficio();
	if (msj.isStatus()) {
	    eliminarOficio();
	} else {
	    mandarGrowl(true, true);
	}
    }

    private void validarModificarOficio() {
	msj = validarIdOficio();
	if (msj.isStatus()) {
	    modificarOficio();
	} else {
	    mandarGrowl(true, true);
	}
    }

    private void inicializarOficio() {
	oficio = new Oficio();
	selectArchivoId = new Archivo();
	System.out.println("OFICIO LISTO ");
    }

    private void validadorMensaje() {
	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Información lista, haga click en Previsualizar"));
	previewButton = true;
    }

    private Message validarDatosOficio() {
	Message regresar = new Message();
	regresar.setStatus(true);
	if (oficio != null) {
	    if (oficio.getAsunto() == null || oficio.getAsunto().isEmpty()) {
		regresar.setStatus(false);
		regresar.setMessage("El asunto es un dato obligatorio");
	    }
	} else {
	    regresar.setStatus(false);
	    regresar.setMessage("Registro incorrecto");
	}
	if (oficio.getFecha_vencimiento() == null) {
	    System.out.println("FECHA NULA ----0");
	    Calendar fechaNula = Calendar.getInstance();
	    fechaNula.set(1990, 00, 01);
	    this.oficio.setFecha_vencimiento(fechaNula.getTime());
	    this.oficio.setOficio_bandera_vencimiento(0);
	} else {
	    this.oficio.setOficio_bandera_vencimiento(1);
	}
	return regresar;
    }

    private Message validarIdOficio() {
	Message regresar = new Message();
	regresar.setStatus(true);
	if (oficio != null) {
	    if (oficio.getOficioId() == null || oficio.getOficioId() == 0) {
		regresar.setStatus(false);
		regresar.setMessage("Fallo en la accion");
	    }
	} else {
	    regresar.setStatus(false);
	    regresar.setMessage("Fallo en la accion");
	}
	return regresar;
    }

    private Message validarIdUnico() {
	Message regresar = new Message();
	regresar.setStatus(true);
	if (idUnico < 0) {
	    regresar.setStatus(false);
	    regresar.setMessage("Oficio no encontrado");
	}
	return regresar;
    }

    private Message validarIdUnicoDocumento() {
	Message regresar = new Message();
	regresar.setStatus(true);
	if (idUnicoDocumento < 0) {
	    regresar.setStatus(false);
	    regresar.setMessage("Documento No Encontrado");
	}
	return regresar;
    }

    private void validarOficioBorrador() {
	if (!rolEntrada.equalsIgnoreCase("SOP_ADMIN") && !rolEntrada.equalsIgnoreCase("SOP_OFICPART")) {
	    oficio.setOficio_borrador(0);
	    System.out.println("OFICIO BORRADOR");
	} else {
	    oficio.setOficio_borrador(1);
	    System.out.println("OFICIO OFICIAL");
	}
    }

    private void validarCargarDocumento() {
	if (documento != null) {
	    cargarDocumento();
	} else {
	    mandarGrowl(true, true);
	}
    }

    public Oficio enviarOficio() {
	return this.oficio;
    }

    // - - - - - - - - - - Métodos de vista - - - - - - - - - - - - -
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

    public void handleVerificarDatosAgregados(ActionEvent event) {
	System.out.println("&& ID_OFICIO: ");
    }

    public void handlePerfilarRegistro(ActionEvent event) {
	editionButton = false;
	inicializarOficio();
    }

    public void handleGuardarAgregarOficio(ActionEvent event) {
	System.out.println("??? ENTRANDO AL HANDLEGUARDARAGREGAROFICIO ???");
	validarAgregarOficio();
//	validarCargarDocumento();
    }

    public void handleNuevoOficio(ActionEvent event) {
	System.out.println("$$$ ENTRANDO A NUEVOOFICIO HANDLE $$$");
	agregarOficio();
    }

    public void handleValidarAgregarOficio(ActionEvent event) {
	System.out.println("??? ENTRANDO AL HANDLEVALIDARAGREGAROFICIO ???");
	validarDatosOficio();
	validadorMensaje();

//	validarCargarDocumento();
    }

    public void handleEliminarOficio(ActionEvent event) {
	validarEliminarOficio();
    }

    public void handleModificarOficio(ActionEvent event) {
	validarModificarOficio();
    }

    public void handleBorradoMultipleOficio(ActionEvent event) {
	borradoMultipleOficio();
    }

    public void handleCargarUnicoOficio() {
	validarCargarUnicoOficio();
    }

    public void handleCargarUnicoDocumento() throws IOException {
	validarargaUnicoDocumento();
    }

    public void handleCargarDocumento(FileUploadEvent file) {
	System.out.println("VERSION = 1");
	this.oficio.setOficio_version(1);
	validarProcesoCreacionID();
	System.out.println("//ENTRANDO AL HANDLE DE CARGAR DOCUMENTOS//");
	documento = convierteUploadedToDocto(file.getFile());
	validarCargarDocumento();
    }

    public void handleVerificarID(ActionEvent event) {
	validarVerificacionID();
    }

    public void handleEliminarUnicoOficio(ActionEvent event) {
	eliminarOficio();
	selectId = null;
	selectArchivoId = null;

    }

    public void mensajeConfirmacion() {
	addMessage("Oficio Creado", "Error, verificar datos");
    }

    public void confirmarRegistro(ActionEvent event) {
	this.finishOfic = true;
    }

    public void addMessage(String summary, String detail) {
	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
	FacesContext.getCurrentInstance().addMessage(null, message);
    }

    // - - - - - - - - - Getters and Setters - - - - - - - - - - - - -
    public List<String> getEstatusrow() {
	return oficio.getEstatusrowstatic();
    }

    public Oficio getOficio() {
	return oficio;
    }

    public void setOficio(Oficio oficio) {
	this.oficio = oficio;
    }

    public List<Oficio> getOficios() {
	return listaOficios;
    }

    public void setOficios(List<Oficio> oficios) {
	this.listaOficios = oficios;
    }

    public Oficio getSelectId() {
	return selectId;
    }

    public void setSelectId(Oficio selectId) {
	this.selectId = selectId;
    }

    public List<Oficio> getSelectIds() {
	return selectIds;
    }

    public void setSelectIds(List<Oficio> selectIds) {
	this.selectIds = selectIds;
    }

    public boolean isFiltro1() {
	return filtro1;
    }

    public void setFiltro1(boolean filtro1) {
	this.filtro1 = filtro1;
    }

    public boolean isFiltro2() {
	return filtro2;
    }

    public void setFiltro2(boolean filtro2) {
	this.filtro2 = filtro2;
    }

    public boolean isFiltro3() {
	return filtro3;
    }

    public void setFiltro3(boolean filtro3) {
	this.filtro3 = filtro3;
    }

    public int getTabindex() {
	return tabindex;
    }

    public void setTabindex(int tabindex) {
	this.tabindex = tabindex;
    }

    public long getIdUnico() {
	return idUnico;
    }

    public void setIdUnico(long idUnico) {
	this.idUnico = idUnico;
    }

    public UploadedFile getFile() {
	return file;
    }

    public void setFile(UploadedFile file) {
	this.file = file;
    }

    public boolean isEditionButton() {
	return editionButton;
    }

    public void setEditionButton(boolean editionButton) {
	this.editionButton = editionButton;
    }

    public boolean isShowdetails() {
	return showdetails;
    }

    public void setShowdetails(boolean showdetails) {
	this.showdetails = showdetails;
    }

    public void reset() {
	RequestContext.getCurrentInstance().reset("registro");
    }

    public boolean isPreviewButton() {
	return previewButton;
    }

    public void setPreviewButton(boolean previewButton) {
	this.previewButton = previewButton;
    }

    public Archivo getSelectArchivoId() {
	return selectArchivoId;
    }

    public void setSelectArchivoId(Archivo selectArchivoId) {
	this.selectArchivoId = selectArchivoId;
    }

    public long getIdUnicoDocumento() {
	return idUnicoDocumento;
    }

    public void setIdUnicoDocumento(long idUnicoDocumento) {
	this.idUnicoDocumento = idUnicoDocumento;
    }

    public StreamedContent getDocumentoDescargado() {
	return documentoDescargado;
    }

    public void setDocumentoDescargado(StreamedContent documentoDescargado) {
	this.documentoDescargado = documentoDescargado;
    }

    public String getRolEntrada() {
	return rolEntrada;
    }

    public void setRolEntrada(String rolEntrada) {
	this.rolEntrada = rolEntrada;
    }

    public boolean isPermisoAñadir() {
	return permisoAñadir;
    }

    public void setPermisoAñadir(boolean permisoAñadir) {
	this.permisoAñadir = permisoAñadir;
    }

    public boolean isPermisoEliminar() {
	return permisoEliminar;
    }

    public void setPermisoEliminar(boolean permisoEliminar) {
	this.permisoEliminar = permisoEliminar;
    }

    public boolean isFinishOfic() {
	return finishOfic;
    }

    public void setFinishOfic(boolean finishOfic) {
	this.finishOfic = finishOfic;
    }

    public boolean isFechaVencimiento() {
	return fechaVencimiento;
    }

    public void setFechaVencimiento(boolean fechaVencimiento) {
	this.fechaVencimiento = fechaVencimiento;
    }

    public List<String> getOpcionesEntidades() {
	return opcionesEntidades;
    }

    public String getUSER_NAME() {
	return USER_NAME;
    }

    public void tabular(ActionEvent event) {
	String accion = event.getComponent().getAttributes().get("i").toString();
	switch (accion) {

	    case "Todos":
		tabindex = 0;
		obtenerOficios();
		break;
	    case "Enviados":
		filtro1 = false;
		filtro2 = true;
		filtro3 = false;
		tabindex = 1;
		cargaClasificacion(accion);
		break;

	    case "Recibidos":
		filtro1 = false;
		filtro2 = false;
		filtro3 = true;
		tabindex = 2;
		cargaClasificacion(accion);
		break;

	    case "Asignados":
		filtro1 = true;
		filtro2 = false;
		filtro3 = false;
		tabindex = 3;
		cargaClasificacion(accion);
		break;

	    case "En Seguimiento":
		filtro1 = false;
		filtro2 = false;
		filtro3 = true;
		tabindex = 4;
		cargaClasificacion(accion);
		break;
	}

	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, accion, null);
	FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void dobleClick(SelectEvent event) {
	oficio = (Oficio) event.getObject();
	showdetails = true;
    }

    public void showDetailsActiva(ActionEvent event) {
	String show = event.getComponent().getAttributes().get("show").toString();
	if (show.equals("true") && !show.equals("")) {
	    showdetails = true;
	} else {
	    showdetails = false;
	}
    }

    public Documento convierteUploadedToDocto(UploadedFile file) {
	Documento docto = new Documento();
	Long idDocumento = validarCreacionIDDocumento();
	try {
	    docto.setAdjuntoid(idDocumento);
	    docto.setNombre(file.getFileName());
	    docto.setDocumento(IOUtils.toByteArray(file.getInputstream()));
	    docto.setOficioid(this.oficio.getOficioId());
	} catch (IOException e) {
	    System.out.println("Error en el archivo");
	}
	return docto;
    }

    public void edicionActiva(ActionEvent event) {
	String edita = event.getComponent().getAttributes().get("edita").toString();
	if (edita.equals("true") && !edita.equals("") && edita != null) {
	    editionButton = true;
	}
    }

    private void validarPermisoAñadirOficio() {
	if (rolEntrada.equalsIgnoreCase("SOP_ADMIN")) {
	    permisoAñadir = true;
	} else if (rolEntrada.equalsIgnoreCase("SOP_OFICPART")) {
	    permisoAñadir = true;
	} else if (rolEntrada.equalsIgnoreCase("SOP_OFICWRITER")) {
	    permisoAñadir = true;
	} else if (rolEntrada.equalsIgnoreCase("SOP_OFICREADER")) {
	    permisoAñadir = false;
	}
	System.out.println("PERMISO AÑADIR: " + permisoAñadir);
    }

    private void validarPermisoEliminarOficio() {
	if (rolEntrada.equalsIgnoreCase("SOP_ADMIN")) {
	    permisoEliminar = true;
	} else if (rolEntrada.equalsIgnoreCase("SOP_OFICPART")) {
	    permisoEliminar = true;
	} else if (rolEntrada.equalsIgnoreCase("SOP_OFICWRITER")) {
	    permisoEliminar = true;
	} else if (rolEntrada.equalsIgnoreCase("SOP_OFICREADER")) {
	    permisoEliminar = false;
	}
	System.out.println("PERMISO ELIMINAR: " + permisoEliminar);
    }

    private void procesoCreacionID(String cabecera, String Fecha, String clase, String priori, String version, String aleatorio) {
	String CABECERA = cabecera;
	String INICIAL = "";
	String concat = INICIAL.concat(CABECERA);
	String concat2 = concat.concat(Fecha);
	String concat3 = concat2.concat(clase);
	String concat4 = concat3.concat(priori);
	String concat5 = concat4.concat(version);

	//SETEAMOS EL VALOR DEL FOLIO
	this.oficio.setFolio(concat5);
	System.out.println("RESULTADO FOLIO ID: " + oficio.getFolio());
	//FINALIZAMOS EL ID
	String concat6 = concat5.concat(aleatorio);
	Long idOficioLong = Long.valueOf(concat6);
	this.oficio.setOficioId(idOficioLong);
	System.out.println("RESULTADO OFICIO ID: " + idOficioLong);
    }

    private void procesoVerificacionID(String cabecera, String Fecha, String clase, String priori, String version) {
	String CABECERA = cabecera;
	String INICIAL = "";
	String concat = INICIAL.concat(CABECERA);
	String concat2 = concat.concat(Fecha);
	String concat3 = concat2.concat(clase);
	String concat4 = concat3.concat(priori);
	String concat5 = concat4.concat(version);

	Long idOficioLong = Long.valueOf(concat5);
	this.oficio.setOficioId(idOficioLong);
	System.out.println("RESULTADO: " + idOficioLong + " Temporal");
    }

    private void validarProcesoCreacionID() {
	System.out.println("??	CREAMOS Oficio para agregar ID		??");
	//CABECERA
	int arrayC[] = new int[3];
	int n = 0;
	String CABECERA;
	Random rndC = new Random();
	for (int i = 0; i < 3; i++) {
	    n = (int) (rndC.nextDouble() * 10) + 1;
	    arrayC[i] = n;
	}
	CABECERA = Arrays.toString(arrayC).replaceAll("\\[|\\]|,|\\s", "");
	System.out.println("ARRAY: " + CABECERA);
	//FECHA
	Date today;
	SimpleDateFormat formatter;
	SimpleDateFormat formatterOfic;
	formatter = new SimpleDateFormat("ddMMyy");
	formatterOfic = new SimpleDateFormat("yyyyMMdd");
	today = new Date();
	FECHA = formatter.format(today);
	//SETEAMOS AL OFICIO
	this.oficio.setFecha_oficio(today);
	//INSTACIAMOS FECHA
	//PRIORI
	int intemp = this.oficio.getPrioridad_id();
	PRIORI = String.valueOf(intemp);
	//CLASE
	intemp = this.oficio.getClass_id();
	CLASE = String.valueOf(intemp);
	//VERSION
	VERSION = this.oficio.getOficio_version();
	//ARRAY ALEATORIO
	int array[] = new int[6];
	n = 0;
	String ALEATORIO;
	Random rnd = new Random();
	for (int i = 0; i < 6; i++) {
	    n = (int) (rnd.nextDouble() * 10);
	    array[i] = n;
	}
	ALEATORIO = Arrays.toString(array).replaceAll("\\[|\\]|,|\\s", "");
	System.out.println("ARRAY: " + ALEATORIO);
	//MANDAMOS RESULTADO
	procesoCreacionID(CABECERA, FECHA, PRIORI, CLASE, String.valueOf(VERSION), ALEATORIO);
    }

    private Long validarCreacionIDDocumento() {
	System.out.println("??	Validamos Documento para agregar ID		??");
	//CABECERA
	int arrayC[] = new int[8];
	int n = 0;
	String CABECERA;
	Random rndC = new Random();
	for (int i = 0; i < 6; i++) {
	    n = (int) (rndC.nextDouble() * 10) + 1;
	    arrayC[i] = n;
	}
	CABECERA = Arrays.toString(arrayC).replaceAll("\\[|\\]|,|\\s", "");
	System.out.println("ARRAY: " + CABECERA);
	Long idDocumentoLong = Long.valueOf(CABECERA);
	System.out.println("RESULTADO DOCUMENTO ID: " + idDocumentoLong);
	return idDocumentoLong;

    }

    private void validarVerificacionID() {
	System.out.println("??	Validamos Oficio para agregar ID		??");
	//FECHA
	Date today;
	SimpleDateFormat formatter;
	formatter = new SimpleDateFormat("ddMMyy");
	today = new Date();
	FECHA = formatter.format(today);
	//INSTACIAMOS FECHA
	//PRIORI
	int intemp = this.oficio.getPrioridad_id();
	PRIORI = String.valueOf(intemp);
	//CLASE
	intemp = this.oficio.getClass_id();
	CLASE = String.valueOf(intemp);
	//VERSION
	VERSION = this.oficio.getOficio_version();

    }

}
