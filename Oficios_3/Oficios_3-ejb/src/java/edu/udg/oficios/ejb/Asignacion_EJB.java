/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.ejb;

import edu.udg.core.data.model.Message;
import edu.udg.oficios.dao.ASIGNACIONDAO;
import edu.udg.oficios.dao.ASIGNACION_IMPL_DAO;
import edu.udg.oficios.data.Asignacion;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
@Stateless
public class Asignacion_EJB implements AsignacionEjbLocal {

    @Override
    public Message agregar(Asignacion asignacion) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message modificar(Asignacion asignacion) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message eliminar(Asignacion asignacion) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message buscarByOficio(Long oficioId) {
	List<Asignacion> asignaciones;
	Message msj = new Message();
	try {
	    ASIGNACIONDAO dao = new ASIGNACION_IMPL_DAO();
	    asignaciones = dao.byOficio(oficioId);
	    System.out.println(" ### ASIGNACION_EJB - ASIGNACIONES:" + asignaciones.size());
	    //----------------------------------------------------------------------//
	    //Cola de mensajes
	    //----------------------------------------------------------------------//
	    msj.setStatus(Boolean.TRUE);
	    msj.setMessage("Listo");
	    msj.setObject(asignaciones);
	} catch (Exception ex) {
	    System.out.println(" ERROR ### ExceptionAsignacion - SistemaEJB.buscarByOficio | " + ex.getLocalizedMessage());
	    msj.setStatus(Boolean.FALSE);
	    msj.setMessage("Error no especificado al Cargar por Estatus");
	    msj.setObject(null);
	}
	return msj;
    }

}
