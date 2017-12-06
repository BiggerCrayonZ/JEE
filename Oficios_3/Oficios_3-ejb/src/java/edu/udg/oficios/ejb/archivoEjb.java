/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.ejb;

//----------------------------------------//
import edu.udg.core.data.model.Message;
//----------------------------------------//
import edu.udg.oficios.data.Archivo;
import edu.udg.oficios.dao.ARCHIVO_IMPL_DAO;
//----------------------------------------//
import javax.ejb.Stateless;
//----------------------------------------//

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
@Stateless
public class archivoEjb implements archivoEjbLocal {

    //Constructores
    public archivoEjb() {
    }

    //Metodos
    //--------------------------------------------------------------------------//
    //Agregar Archivo
    //--------------------------------------------------------------------------//
    @Override
    public Message agregar(Archivo archivo) {
	System.out.println("? EJB - AGREGAR ARCHIVO ?");
	Message mensaje = new Message();
	try {
	    ARCHIVO_IMPL_DAO dao = new ARCHIVO_IMPL_DAO();
	    dao.insert(archivo);
	    //----------------------------------------------------------------------//
	    //Cola de mensajes
	    //----------------------------------------------------------------------//
	    mensaje.setStatus(Boolean.TRUE);
	    mensaje.setMessage("Listo");
	    mensaje.setObject(archivo);
	} catch (Exception ex) {
	    System.out.println("EXCEPCION SISTEMA ARCHIVOS EJB.AGREGAR : " + ex.getLocalizedMessage());
	    mensaje.setStatus(Boolean.FALSE);
	    mensaje.setMessage("Error no especificado al momento de agregar el oficio");
	    mensaje.setObject(null);
	}
	return mensaje;
    }
    


    @Override
    public Message eliminar(Archivo archivo) {
	System.out.println("*/*/ ENTRANDO A ELIMINAR ARCHIVO POR EJB");
	Message mensaje = new Message();
	try {
	    ARCHIVO_IMPL_DAO dao = new ARCHIVO_IMPL_DAO();
	    dao.delete(archivo);
	    //----------------------------------------------------------------------//
	    //Cola de mensajes
	    //----------------------------------------------------------------------//
	    mensaje.setStatus(Boolean.TRUE);
	    mensaje.setMessage("Listo");
	    mensaje.setObject(archivo);
	} catch (Exception ex) {
	    System.out.println("EXCEPCION SISTEMA OFICIOS EJB.ELIMINAR : " + ex.getLocalizedMessage());
	    mensaje.setStatus(Boolean.FALSE);
	    mensaje.setMessage("Error no especificado al momento de eliminar el archivo");
	    mensaje.setObject(null);
	}

	return mensaje;
    }

    @Override
    public Message modificar(Archivo archivo) {
	Message mensaje = new Message();
	try {
	    ARCHIVO_IMPL_DAO dao = new ARCHIVO_IMPL_DAO();
	    dao.update(archivo);
	    //----------------------------------------------------------------------//
	    //Cola de mensajes
	    //----------------------------------------------------------------------//
	    mensaje.setStatus(Boolean.TRUE);
	    mensaje.setMessage("Listo");
	    mensaje.setObject(archivo);
	} catch (Exception ex) {
	    System.out.println("EXCEPCION SISTEMA OFICIOS EJB.MODIFICAR : " + ex.getLocalizedMessage());
	    mensaje.setStatus(Boolean.FALSE);
	    mensaje.setMessage("Error no especificado al momento de modificar el oficio");
	    mensaje.setObject(null);
	}

	return mensaje;
    }

}
