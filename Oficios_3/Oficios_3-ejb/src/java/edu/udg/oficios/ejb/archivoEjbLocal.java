/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.ejb;

import edu.udg.core.data.model.Message;
import edu.udg.oficios.data.Archivo;
import javax.ejb.Local;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
@Local
public interface archivoEjbLocal {
    
    public Message agregar(Archivo archivo);
    
    public Message eliminar(Archivo archivo);
    
    public Message modificar(Archivo archivo);
    
}
