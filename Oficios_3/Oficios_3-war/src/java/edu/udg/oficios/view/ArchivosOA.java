/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.view;

import edu.udg.core.data.model.Access;
import edu.udg.core.data.model.Message;
import edu.udg.core.data.model.Unity;
import edu.udg.core.data.model.User;
import edu.udg.core.remote.ejb.Service;
import edu.udg.oficios.data.Archivo;
import edu.udg.oficios.data.Documento;
import edu.udg.oficios.data.Oficio;
import edu.udg.core.portal.util.Utileria;
import edu.udg.oficios.ejb.OficioEjbLocal;
import edu.udg.oficios.ejb.Oficio_EJB;
import edu.udg.oficios.ejb.archivoEjb;
import edu.udg.oficios.ejb.archivoEjbLocal;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
@Named(value = "ArchivosOA")
@ViewScoped

public class ArchivosOA implements Serializable {

    //Usuario
    private User USER;
    private String rolEntrada;

    //Variables y constantes
    private Archivo archivo;
    private Oficio oficio;
    private String accion;
    private final Utileria util;
    private String idCreado;
    //Servicios
    private Service s;
    private List<String> opcionesEntidades;
    //Permisos
    private boolean permisoNuevo;
    //Confirmaciones
    private boolean archivoCreadoExito;

    @EJB
    private archivoEjbLocal archivoEjb;
    private OficioEjbLocal oficioEjb;
    private Message msj;
    private Message msjDoc;
    private boolean permisoAgregarArchivo;
    private boolean permisoModificarArchivo;
    private boolean permisoFinal;
    private boolean permisoAgregar;
    private boolean permisoAñadir;
    private boolean verificarOficioNuevo = true;
    private boolean verificarOficioModificado = false;
    //Parametros para pdf
    private Long identificador;
    private String separador;
    private static final String DESTINATION_DIR_PATH = "reportes";
    private StreamedContent archivoPDF;
    private StreamedContent PDFtemp;
    //Dedicados al documento
    private Documento documento;
    private String FECHA;
    private String PRIORI;
    private String CLASE;
    private int VERSION;

    //Constructores
    public ArchivosOA() {
	this.util = new Utileria();
    }

    @PostConstruct
    public void iniciaArchivo() {
	s = new Service();
	Access acceso = util.obtenerAccesoSesion();
	String appId = acceso.getAppId();
	String token = acceso.getToken();
	String rolid = acceso.getRolId();
	inicializarArchivo();
	msj = new Message();
	msjDoc = new Message();
	archivo = new Archivo();
	oficio = new Oficio();
	oficioEjb = new Oficio_EJB();
	archivoEjb = new archivoEjb();
	documento = new Documento();
	archivoCreadoExito = false;
	System.out.println("### METODO PARA INICIAR ARCHIVOOA");
	//Servicios
	permisoNuevo = s.validaAction(token, appId, util.obtenerModulo().replace("/", ""), "CAPTURAROFICIO");
	String ROL = util.obtenerAccesoSesion().getRolId();
	rolEntrada = ROL;
	validarPermisoAñadirOficio();
	System.out.println("PERMISO AGREGAR: " + permisoAgregar);
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
	System.out.println("USUARIO NOMBRE: " + nombreUser);
	System.out.println("USUARIO ROL: " + ROL);

	//Seteamos valores por default
	archivo.setUsuario_emirsor(nombreUser);

    }

    
    
    //En uso
    public StreamedContent metodoNuevo(ActionEvent event) {
	System.out.println("### METODO NUEVO DE ARCHIVO #######################################");
	this.archivo.setOficio_version(1);
	this.archivo.setOficio_tipo(0);
	validarOficioBorrador();
	validarProcesoCreacionID();
	validarProcesoCreacionArchivoID();
	agregarArchivo();
	validarProcesoInsercionDatos();
	return archivoPDF;
    }
    
    public void handleFinalizarDocumento(ActionEvent event){
	System.out.println("FINALIZAMOS EL DOCUMENTO");
	validarCargaArchivo();
    }
    
    //Falta de Asignación en la vista

    public void metodoModifi(ActionEvent event) {
	System.out.println("### METODO MODIFICACIÓN DE ARCHIVO #################################");
	modificarArchivo();
    }

    public StreamedContent metodoModificacion(ActionEvent event) {
	System.out.println("### METODO MODIFICACIÓN DE ARCHIVO #################################");
	modificarArchivo();
	descargarPDFListener();
	return archivoPDF;
    }

    //Metodos de inicialización
    private void inicializarArchivo() {
	archivo = new Archivo();

    }

    private void validarOficioBorrador() {
	if (!rolEntrada.equalsIgnoreCase("SOP_ADMIN") && !rolEntrada.equalsIgnoreCase("SOP_OFICPART")) {
	    archivo.setOficio_borrador(0);
	    System.out.println("OFICIO BORRADOR");
	} else {
	    archivo.setOficio_borrador(1);
	    System.out.println("OFICIO OFICIAL");
	}
    }

    //Metodos de Acceso a datos
    private void agregarArchivo() {
	identificador = archivo.getOficioId();
	archivo.setOficio_tipo(1);
	System.out.println("&&&&&&& IDENTIFICADOR: " + identificador);
	System.out.println("Mandamos archivo a ejb");
	//Seteamos el id del archivo ligandolo en ambas clases
	archivo.setOficio_archivo(this.archivo.getArchivoId());
	validarOficioBorrador();
	this.oficio.setOficio_version(1);

	msj = archivoEjb.agregar(archivo);
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

    private void modificarArchivo() {
	identificador = archivo.getOficioId();
	System.out.println("&&&&&&& IDENTIFICADOR DE MODIFICACIÓN: " + identificador);
	System.out.println("Mandamos archivo a ejb");
	//Seteamos el id del archivo ligandolo en ambas clases
	archivo.setOficio_archivo(this.archivo.getArchivoId());
	//Seteamos las demas modificaciones
	oficio.setArchivoId(this.archivo.getArchivoId());
	oficio.setAsunto(this.archivo.getAsunto());
	oficio.setClass_id(this.archivo.getClass_id());
	oficio.setPrioridad_id(this.archivo.getPrioridad_id());
	oficio.setLayer_id(this.archivo.getLayer_id());
	//Fechas
	oficio.setFecha_oficio(this.archivo.getFecha_oficio());
	oficio.setFecha_vencimiento(this.archivo.getFecha_vencimiento());
	//Folios y observaciones
	oficio.setFolio(this.archivo.getFolio());
	oficio.setObservaciones(this.archivo.getObservaciones());
	//ID DEL OFICIO
	oficio.setOficioId(this.archivo.getOficioId());

	//Metodo de actualización de oficio
	msj = oficioEjb.modificar(oficio);
	//Método para actializar el archivo
	msj = archivoEjb.modificar(archivo);

    }

    //Metodos de validación
//    private void validarArchivo() {
//	System.out.println("??Validamos Archivo al agregar ??");
//
//	msj = validarDatosArchivo();
//	if (msj.isStatus()) {
//	    agregarArchivo();
//	} else {
//	    System.out.println("MANDA ERROR POR ANEXO Y VALIDACIÓN DE DATOS");
//	}
//    }
    //Métodos de vista
    public void save() {
	FacesContext.getCurrentInstance().addMessage(null,
		new FacesMessage("!El Oficio " + archivo.getAsunto() + " fue creado con exito¡"));
    }

    public void mensajeCorrectio() {
	if (archivo == null) {
	    FacesContext.getCurrentInstance().addMessage(null,
		    new FacesMessage("Datos Incompletos, revise su información y de click en Verificar de nuevo."));
	    System.out.println("PERMISO : " + permisoAgregarArchivo);

	} else {
	    this.setPermisoAgregarArchivo(true);
	    System.out.println("PERMISO : " + permisoAgregarArchivo);
	    this.setPermisoModificarArchivo(false);
	    System.out.println("PERMISO MODIFICAR: " + permisoModificarArchivo);
	    FacesContext.getCurrentInstance().addMessage(null,
		    new FacesMessage("!El Oficio " + archivo.getAsunto() + " esta listo, haz click en Guardar¡"));
	    if (archivo.getFecha_vencimiento() == null) {
		System.out.println("FECHA NULA ----0");
		Calendar fechaNula = Calendar.getInstance();
		fechaNula.set(1990, 00, 01);
		this.archivo.setFecha_vencimiento(fechaNula.getTime());
		this.archivo.setOficio_bandera_vencimiento(0);
	    } else {
		System.out.println("FECHA NO NULA ----0");
		this.archivo.setOficio_bandera_vencimiento(1);
	    }
	}

    }

    public void mensajeCorrectioModificacion() {
	if (archivo == null) {
	    FacesContext.getCurrentInstance().addMessage(null,
		    new FacesMessage("Datos Incompletos, revise su información y de click en Verificar de nuevo."));
	    System.out.println("PERMISO : " + permisoAgregarArchivo);
	} else {
	    this.setPermisoAgregarArchivo(false);
	    System.out.println("PERMISO : " + permisoAgregarArchivo);
	    this.setPermisoModificarArchivo(true);
	    System.out.println("PERMISO MODIFICAR: " + permisoModificarArchivo);
	    FacesContext.getCurrentInstance().addMessage(null,
		    new FacesMessage("!El Oficio " + archivo.getAsunto() + " esta listo para ser Modificado¡"));
	}
    }

    public void corregirArchivoPermisos() {
	this.setVerificarOficioNuevo(false);
	System.out.println("PERMISO VERIFICAR NUEVO: " + verificarOficioNuevo);
	this.setVerificarOficioModificado(false);
	System.out.println("PERMISO VERIFICAR MODIFICADO: " + verificarOficioModificado);
	this.setPermisoAgregarArchivo(false);
	System.out.println("PERMISO AGREGAR ARCHIVO: " + permisoAgregarArchivo);
	FacesContext.getCurrentInstance().addMessage(null,
		new FacesMessage("!El Oficio " + archivo.getAsunto() + " será modificado"));

    }

    public void crearCallBackParam(Boolean callBackParamValue) {
	RequestContext context = RequestContext.getCurrentInstance();
	context.addCallbackParam("valido", callBackParamValue);
    }

//    public void enviaCorreo(ActionEvent event) {
//	List<String> destinos = new ArrayList<String>();
//	destinos.add("georgegm2009@hotmail.com");
//	new AdminCorreo().enviar(new Correo(destinos, "Pruebas de Correo", "Hola desde el servidor", null));
//    }
    //Handles
    public void actionNuevoArchivo(ActionEvent event) {
	agregarArchivo();
    }

    public void handlePerfilarRegistroArchivo(ActionEvent event) {
	inicializarArchivo();
    }

    //Getter and Setter
    public boolean isPermisoFinal() {
	return permisoFinal;
    }

    public void setPermisoFinal(boolean permisoFinal) {
	this.permisoFinal = permisoFinal;
    }

    public Archivo getArchivo() {
	return archivo;
    }

    public void setArchivo(Archivo archivo) {
	this.archivo = archivo;
    }

    public String getAccion() {
	return accion;
    }

    public void setAccion(String accion) {
	this.accion = accion;
    }

    public archivoEjbLocal getArchivoEjb() {
	return archivoEjb;
    }

    public void setArchivoEjb(archivoEjbLocal archivoEjb) {
	this.archivoEjb = archivoEjb;
    }

    public Message getMsj() {
	return msj;
    }

    public void setMsj(Message msj) {
	this.msj = msj;
    }

    public boolean isPermisoAgregarArchivo() {
	return permisoAgregarArchivo;
    }

    public void setPermisoAgregarArchivo(boolean permisoAgregarArchivo) {
	this.permisoAgregarArchivo = permisoAgregarArchivo;
    }

    public StreamedContent getArchivoPDF() {
	return archivoPDF;
    }

    public void setArchivoPDF(StreamedContent archivoPDF) {
	this.archivoPDF = archivoPDF;
    }

    public boolean isPermisoModificarArchivo() {
	return permisoModificarArchivo;
    }

    public void setPermisoModificarArchivo(boolean permisoModificarArchivo) {
	this.permisoModificarArchivo = permisoModificarArchivo;
    }

    public boolean isVerificarOficioNuevo() {
	return verificarOficioNuevo;
    }

    public void setVerificarOficioNuevo(boolean verificarOficioNuevo) {
	this.verificarOficioNuevo = verificarOficioNuevo;
    }

    public boolean isVerificarOficioModificado() {
	return verificarOficioModificado;
    }

    public void setVerificarOficioModificado(boolean verificarOficioModificado) {
	this.verificarOficioModificado = verificarOficioModificado;
    }

    public User getUSER() {
	return USER;
    }

    public void setUSER(User USER) {
	this.USER = USER;
    }

    public String getRolEntrada() {
	return rolEntrada;
    }

    public void setRolEntrada(String rolEntrada) {
	this.rolEntrada = rolEntrada;
    }

    public Oficio getOficio() {
	return oficio;
    }

    public void setOficio(Oficio oficio) {
	this.oficio = oficio;
    }

    public String getIdCreado() {
	return idCreado;
    }

    public void setIdCreado(String idCreado) {
	this.idCreado = idCreado;
    }

    public Service getS() {
	return s;
    }

    public void setS(Service s) {
	this.s = s;
    }

    public boolean isPermisoNuevo() {
	return permisoNuevo;
    }

    public void setPermisoNuevo(boolean permisoNuevo) {
	this.permisoNuevo = permisoNuevo;
    }

    public OficioEjbLocal getOficioEjb() {
	return oficioEjb;
    }

    public void setOficioEjb(OficioEjbLocal oficioEjb) {
	this.oficioEjb = oficioEjb;
    }

    public Message getMsjDoc() {
	return msjDoc;
    }

    public void setMsjDoc(Message msjDoc) {
	this.msjDoc = msjDoc;
    }

    public boolean isPermisoAgregar() {
	return permisoAgregar;
    }

    public void setPermisoAgregar(boolean permisoAgregar) {
	this.permisoAgregar = permisoAgregar;
    }

    public boolean isPermisoAñadir() {
	return permisoAñadir;
    }

    public void setPermisoAñadir(boolean permisoAñadir) {
	this.permisoAñadir = permisoAñadir;
    }

    public Long getIdentificador() {
	return identificador;
    }

    public void setIdentificador(Long identificador) {
	this.identificador = identificador;
    }

    public String getSeparador() {
	return separador;
    }

    public void setSeparador(String separador) {
	this.separador = separador;
    }

    public StreamedContent getPDFtemp() {
	return PDFtemp;
    }

    public void setPDFtemp(StreamedContent PDFtemp) {
	this.PDFtemp = PDFtemp;
    }

    public Documento getDocumento() {
	return documento;
    }

    public void setDocumento(Documento documento) {
	this.documento = documento;
    }

    public List<String> getOpcionesEntidades() {
	return opcionesEntidades;
    }

    public boolean isArchivoCreadoExito() {
	return archivoCreadoExito;
    }

    public void setArchivoCreadoExito(boolean archivoCreadoExito) {
	this.archivoCreadoExito = archivoCreadoExito;
    }

    private Message validarDatosArchivo() {
	Message regresar = new Message();
	regresar.setStatus(true);

	if (archivo != null) {
	    regresar.setStatus(false);
	    regresar.setMessage("Registro incorrecto");
	}

	if (archivo.getArchivo_despedida() == null || archivo.getArchivo_despedida().isEmpty()) {
	    archivo.setArchivo_despedida("");
	    System.out.println("%% ---------------DESPEDIDA MODIFICADA -----------------%%");
	}
	return regresar;
    }

    private void inicializarSeparador() {
	separador = FacesContext.getCurrentInstance().getExternalContext().getRealPath(DESTINATION_DIR_PATH);
	if (separador.contains("\\")) {
	    separador = "\\";
	} else {
	    separador = "/";
	}
    }

    //Metodología para PDF
    private StreamedContent descargarPDFListener() {
	FacesContext context = FacesContext.getCurrentInstance();
	Map<String, String> params = context.getExternalContext().getRequestParameterMap();
	//Añadimos el archivo
	byte[] bufferPDF;
	String reporteNombre = "reporte_archivo_2.jasper";
	Long comprobanteId;
	if (archivo == null) {
	    crearCallBackParam(false);
	} else {
	    comprobanteId = identificador;
	    System.out.println("OFICIO ID COMPROBANTE: " + comprobanteId);
	    try {
		bufferPDF = getPDFOutputStream(reporteNombre, new BigDecimal(comprobanteId), null).toByteArray();
		InputStream stream = new ByteArrayInputStream(bufferPDF);
		archivoPDF = new DefaultStreamedContent(stream, "application/pdf", "SOP " + comprobanteId + ".pdf");
		System.out.println("ARCHIVO-PDF : " + archivoPDF);
		PDFtemp = archivoPDF;
		System.out.println("PDF-TEMP : " + PDFtemp);
	    } catch (Exception e) {
		System.out.println("ERROR DESCARGARPDFLISTENER: " + e.getLocalizedMessage());
	    }
	}
	return archivoPDF;
    }

    public ByteArrayOutputStream getPDFOutputStream(String nombreReporte, BigDecimal comprobanteId, Connection aConn) throws Exception {
	inicializarSeparador();
	String rutaReportes = FacesContext.getCurrentInstance().getExternalContext().getRealPath(DESTINATION_DIR_PATH) + separador;
	Connection conn;
	JasperPrint jasperPrint;
	JasperReport jasperReport;
	HashMap jasperParameter;

//	JRPdfExporter pdfExporter = new JRPdfExporter();
	ByteArrayOutputStream pdfReport = new ByteArrayOutputStream();
	//Conexión a base de datos
	if ((aConn == null) || (aConn.isClosed())) {
	    InitialContext ctx = new InitialContext();
	    DataSource ds = (DataSource) ctx.lookup("jdbc/oficioESCEV");
	    conn = ds.getConnection();
	} else {
	    conn = aConn;
	}

	System.out.println("BANDERA - 0");
	System.out.println("..RUTA: " + rutaReportes + nombreReporte);
	try {
	    System.out.println("............................................................ENTRANDO AL TRY-CATCH DEL PDFOUTPUT");
	    jasperParameter = new HashMap();
	    //DATO IMPORTANTE FALTANTES
	    jasperParameter.put("id", (Long) comprobanteId.longValue());
	    System.out.println("BANDERA - 1");
	    rutaReportes = rutaReportes + nombreReporte;
	    jasperReport = (JasperReport) JRLoader.loadObjectFromFile(rutaReportes);
	    System.out.println("BANDERA - 2 , " + jasperReport);
	    jasperPrint = JasperFillManager.fillReport(jasperReport, jasperParameter, conn);
//	    jasperPrint = JasperFillManager.fillReport(jasperReport, jasperParameter, conn);
	    System.out.println("BANDERA - 3");
	    JasperExportManager.exportReportToPdfStream(jasperPrint, pdfReport);
	    System.out.println("FINALIZA EXPORTER: ");
	} catch (Exception e) {
	    System.out.println("ERROR GETPDFOUTPOUT");
	    e.printStackTrace();
	}
	return pdfReport;
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
	this.archivo.setFecha_oficio(today);
	//INSTACIAMOS FECHA
	//PRIORI
	int intemp = this.archivo.getPrioridad_id();
	PRIORI = String.valueOf(intemp);
	//CLASE
	intemp = this.archivo.getClass_id();
	CLASE = String.valueOf(intemp);
	//VERSION
	VERSION = this.archivo.getOficio_version();
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

    private void procesoCreacionID(String cabecera, String Fecha, String clase, String priori, String version, String aleatorio) {
	String CABECERA = cabecera;
	String INICIAL = "";
	String concat = INICIAL.concat(CABECERA);
	String concat2 = concat.concat(Fecha);
	String concat3 = concat2.concat(clase);
	String concat4 = concat3.concat(priori);
	String concat5 = concat4.concat(version);

	//SETEAMOS EL VALOR DEL FOLIO
	this.archivo.setFolio(concat5);
	System.out.println("RESULTADO FOLIO ID: " + oficio.getFolio());
	//FINALIZAMOS EL ID
	String concat6 = concat5.concat(aleatorio);
	Long idOficioLong = Long.valueOf(concat6);
	this.archivo.setOficioId(idOficioLong);
	System.out.println("RESULTADO OFICIO ID: " + idOficioLong);
    }

    private void validarProcesoCreacionArchivoID() {
	Long idArchivoNuevo = procesoCreacionIDArchivo();
	this.archivo.setArchivoId(idArchivoNuevo);
    }

    private Long procesoCreacionIDArchivo() {
	System.out.println("??	CREAMOS ARCHIVO para agregar su ID		??");
	//CABECERA
	int arrayC[] = new int[6];
	int n = 0;
	String CABECERA;
	Random rndC = new Random();
	for (int i = 0; i < 6; i++) {
	    n = (int) (rndC.nextDouble() * 10) + 1;
	    arrayC[i] = n;
	}
	CABECERA = Arrays.toString(arrayC).replaceAll("\\[|\\]|,|\\s", "");
	System.out.println("ARRAY: " + CABECERA);
	Long idArchivoLong = Long.valueOf(CABECERA);
	System.out.println("RESULTADO ARCHIVO ID: " + idArchivoLong);
	return idArchivoLong;
    }

    private void validarProcesoInsercionDatos() {
	if (msj.isStatus()) {
	    archivoCreadoExito = true;
	    descargarPDFListener();
	    validarCargaArchivo();
	} else {
	    archivoCreadoExito = false;
	    mandarGrowl(true, true);
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

    private void validarCargaArchivo() {
	System.out.println("ENTRANDO AL VALIDADOR");
	    documento = convertirStreamedToDoc(PDFtemp);
	    validarCargaDocumento();
    }

    private Documento convertirStreamedToDoc(StreamedContent PDF) {
	Documento docto = new Documento();
	try {
	    docto.setAdjuntoid(3);
	    docto.setNombre(PDF.getName());
	    docto.setOficioid(this.archivo.getOficioId());
	    docto.setDocumento(IOUtils.toByteArray(PDF.getStream()));
	    System.out.println("INPUTSTREAM" + Arrays.toString(docto.getDocumento()));
	} catch (Exception e) {
	    System.out.println("Error en el archivo");
	}
	return docto;
    }

    private void validarCargaDocumento() {
	if (documento != null) {
	    cargarDocumento();
	} else {
	    mandarGrowl(true, true);
	}
    }

    private void cargarDocumento() {
	System.out.println(" %%%%%%% ENTRANDO AL CARGA DEL DOCUMENTO");
	msj = oficioEjb.cargarDocumento(documento);
	System.out.println("INPUTSTREAM" + PDFtemp.getStream());
    }

}
