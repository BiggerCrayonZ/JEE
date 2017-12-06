/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.ejb;

import edu.udg.core.data.model.Message;
import edu.udg.oficios.data.Documento;
import edu.udg.oficios.data.Oficio;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author udg
 */
@Local
public interface OficioEjbLocal {

    public Message agregar(Oficio oficio);

    public Message modificar(Oficio oficio);

    public Message borradoMultiple(List<Oficio> oficios);

    public Message buscasTodos();

    public Message BuscarID(Long oficioid);

    public Message cargarPorEstatus(String estatus);

    public Message eliminar(Oficio oficio);
    
    public Message cargarDocumento(Documento documento);
    
    public Message eliminarDocumento(Documento documento);
    
    public Message buscarDocumento(Long oficioid);
}