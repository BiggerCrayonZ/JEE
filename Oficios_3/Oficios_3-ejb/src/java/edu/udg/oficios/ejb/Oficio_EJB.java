/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.ejb;
//--------------------------------------------------------------------------//

import edu.udg.core.data.model.Message;

//--------------------------------------------------------------------------//
import edu.udg.oficios.dao.DOCUMENTO_IMPL_DAO;
import edu.udg.oficios.data.Oficio;
import edu.udg.oficios.dao.OFICIODAO;
import edu.udg.oficios.dao.OFICIO_IMPL_DAO;
import edu.udg.oficios.data.Documento;
//--------------------------------------------------------------------------//
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
@Stateless
public class Oficio_EJB implements OficioEjbLocal {

    public Oficio_EJB() {
    }

    //--------------------------------------------------------------------------//
    //Agregar Oficio
    //--------------------------------------------------------------------------//
    @Override
    public Message agregar(Oficio oficio) {
	System.out.println("? EJB - AGREGAR OFICIO ?");
	Message mensaje = new Message();
	try {
	    OFICIO_IMPL_DAO dao = new OFICIO_IMPL_DAO();
	    dao.insert(oficio);
	    //----------------------------------------------------------------------//
	    //Cola de mensajes
	    //----------------------------------------------------------------------//
	    mensaje.setStatus(Boolean.TRUE);
	    mensaje.setMessage("Listo");
	    mensaje.setObject(oficio);
	} catch (Exception ex) {
	    System.out.println("EXCEPCION SISTEMA OFICIOS EJB.AGREGAR : " + ex.getLocalizedMessage());
	    mensaje.setStatus(Boolean.FALSE);
	    mensaje.setMessage("Error no especificado al momento de agregar el oficio");
	    mensaje.setObject(null);
	}

	return mensaje;
    }

    //--------------------------------------------------------------------------//
    //Modificar oficio
    //--------------------------------------------------------------------------//
    @Override
    public Message modificar(Oficio oficio) {
	Message mensaje = new Message();
	try {
	    OFICIO_IMPL_DAO dao = new OFICIO_IMPL_DAO();
	    dao.update(oficio);
	    //----------------------------------------------------------------------//
	    //Cola de mensajes
	    //----------------------------------------------------------------------//
	    mensaje.setStatus(Boolean.TRUE);
	    mensaje.setMessage("Listo");
	    mensaje.setObject(oficio);
	} catch (Exception ex) {
	    System.out.println("EXCEPCION SISTEMA OFICIOS EJB.MODIFICAR : " + ex.getLocalizedMessage());
	    mensaje.setStatus(Boolean.FALSE);
	    mensaje.setMessage("Error no especificado al momento de modificar el oficio");
	    mensaje.setObject(null);
	}

	return mensaje;
    }

    //--------------------------------------------------------------------------//
    //Eliminar Oficio
    //--------------------------------------------------------------------------//
    @Override
    public Message eliminar(Oficio oficio) {
	System.out.println("*/*/ ENTRANDO A ELIMINAR OFICIO POR EJB");
	Message mensaje = new Message();
	try {
	    OFICIO_IMPL_DAO dao = new OFICIO_IMPL_DAO();
	    dao.delete(oficio);
	    //----------------------------------------------------------------------//
	    //Cola de mensajes
	    //----------------------------------------------------------------------//
	    mensaje.setStatus(Boolean.TRUE);
	    mensaje.setMessage("Listo");
	    mensaje.setObject(oficio);
	} catch (Exception ex) {
	    System.out.println("EXCEPCION SISTEMA OFICIOS EJB.ELIMINAR : " + ex.getLocalizedMessage());
	    mensaje.setStatus(Boolean.FALSE);
	    mensaje.setMessage("Error no especificado al momento de eliminar el oficio");
	    mensaje.setObject(null);
	}

	return mensaje;
    }

    //--------------------------------------------------------------------------//
    //Borrado de oficios multiple
    //--------------------------------------------------------------------------//
    @Override
    public Message borradoMultiple(List<Oficio> oficios) {
	Message mensaje = new Message();
	try {
	    OFICIO_IMPL_DAO dao = new OFICIO_IMPL_DAO();
	    for (int x = 0; x < oficios.size(); x++) {
		dao.delete(oficios.get(x));
	    }
	    mensaje.setStatus(Boolean.TRUE);
	    mensaje.setMessage("Listo");
	    mensaje.setObject(oficios);
	} catch (Exception ex) {
	    System.out.println("### ExceptionOficios - SistemaEJB.borrado multiple | " + ex.getLocalizedMessage());
	    mensaje.setStatus(Boolean.FALSE);
	    mensaje.setMessage("Error en la eliminación de Oficios no especificado");
	    mensaje.setObject(null);
	}
	return mensaje;
    }

    //--------------------------------------------------------------------------//
    //Buscar todos los oficios
    //--------------------------------------------------------------------------//
    @Override
    public Message buscasTodos() {
	System.out.println("## BUSCARTODOS()-EJB --- ##");
	List<Oficio> oficios;
	Message mensaje = new Message();
	try {
	    OFICIODAO dao = new OFICIO_IMPL_DAO();
	    oficios = dao.buscasTodos();
	    int size = oficios.size();
	    System.out.println("+++OficiosEJB - BuscarTodos: " + size);

	    //----------------------------------------------------------------------//
	    //Cola de mensajes
	    //----------------------------------------------------------------------//
	    mensaje.setStatus(Boolean.TRUE);
	    mensaje.setMessage("Listo");
	    mensaje.setObject(oficios);
	} catch (Exception ex) {
	    System.out.println("Funcion SistemaEJB.buscastodos | Excepcion: " + ex.getLocalizedMessage());
	    mensaje.setStatus(Boolean.FALSE);
	    mensaje.setMessage("Error no especificado al buscar todos los oficios");
	    mensaje.setObject(null);
	}

	return mensaje;
    }

    @Override
    public Message BuscarID(Long oficioid) {
	Message msj = new Message();
	String id;
	Oficio oficio;
	try {
	    OFICIO_IMPL_DAO dao = new OFICIO_IMPL_DAO();
	    id = Long.toString(oficioid);
	    oficio = dao.buscarId(oficioid);
	    //----------------------------------------------------------------------//
	    //Cola de mensajes
	    //----------------------------------------------------------------------//
	    msj.setStatus(Boolean.TRUE);
	    msj.setMessage("OK");
	    msj.setObject(oficio);
	    System.out.println("DAO IMPLEMEMTADO CORRECTAMENTE");
	} catch (Exception ex) {
	    ex.printStackTrace();
	    msj.setStatus(Boolean.FALSE);
	    msj.setMessage("Error no especificado");
	    msj.setObject(null);
	}
	return msj;
    }

    @Override
    public Message cargarPorEstatus(String estatus) {
	List<Oficio> oficios;
	Message msj = new Message();
	try {
	    OFICIODAO dao = new OFICIO_IMPL_DAO();
	    oficios = dao.byEstatus(estatus);
	    System.out.println(" ### OFICIOSEJB - BUSCARTODOS:" + oficios.size());
	    //----------------------------------------------------------------------//
	    //Cola de mensajes
	    //----------------------------------------------------------------------//
	    msj.setStatus(Boolean.TRUE);
	    msj.setMessage("Listo");
	    msj.setObject(oficios);
	} catch (Exception ex) {
	    System.out.println(" ERROR ### ExceptionOficios - SistemaEJB.cargarPorEstatus | " + ex.getLocalizedMessage());
	    msj.setStatus(Boolean.FALSE);
	    msj.setMessage("Error no especificado al Cargar por Estatus");
	    msj.setObject(null);
	}
	return msj;
    }

    @Override
    public Message cargarDocumento(Documento documento) {
	Message msj = new Message();
	try {
	    DOCUMENTO_IMPL_DAO dao = new DOCUMENTO_IMPL_DAO();
	    dao.insert(documento);
	    msj.setStatus(true);
	    msj.setMessage("OK");
	    msj.setObject(documento);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    msj.setStatus(false);
	    msj.setMessage("Error no especificado");
	    msj.setObject(null);
	}
	return msj;
    }

    @Override
    public Message eliminarDocumento(Documento documento) {
	Message msj = new Message();
	try {
	    DOCUMENTO_IMPL_DAO dao = new DOCUMENTO_IMPL_DAO();
	    dao.delete(documento);
	    msj.setStatus(true);
	    msj.setMessage("OK");
	    msj.setObject(documento);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    msj.setStatus(false);
	    msj.setMessage("Error no especificado");
	    msj.setObject(null);
	}
	return msj;
    }

    @Override
    public Message buscarDocumento(Long oficioid) {
	Message msj = new Message();
	String id;
	Documento documento;
	try {
	    DOCUMENTO_IMPL_DAO dao = new DOCUMENTO_IMPL_DAO();
	    id = Long.toString(oficioid);
	    documento = dao.buscarId(oficioid);
	    //----------------------------------------------------------------------//
	    //Cola de mensajes
	    //----------------------------------------------------------------------//
	    msj.setStatus(Boolean.TRUE);
	    msj.setMessage("OK");
	    msj.setObject(documento);
	    System.out.println("DAO IMPLEMEMTADO CORRECTAMENTE");
	} catch (Exception ex) {
	    ex.printStackTrace();
	    msj.setStatus(Boolean.FALSE);
	    msj.setMessage("Error no especificado");
	    msj.setObject(null);
	}
	return msj;
    }
    
    

}
