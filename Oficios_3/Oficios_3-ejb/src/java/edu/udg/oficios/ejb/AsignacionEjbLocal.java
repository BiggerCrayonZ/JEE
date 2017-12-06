/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.ejb;

import edu.udg.core.data.model.Message;
import edu.udg.oficios.data.Asignacion;
import javax.ejb.Local;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */

@Local
public interface AsignacionEjbLocal {
    
    public Message agregar(Asignacion asignacion);
    
    public Message modificar(Asignacion asignacion);
    
    public Message eliminar(Asignacion asignacion);
    
    public Message buscarByOficio(Long oficioId);
    
}
