/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.dao;

import edu.udg.oficios.data.Asignacion;
import java.util.List;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public interface ASIGNACIONDAO {

    Asignacion buscaAsignacion(Long asig_oficioId);

    int update(Asignacion objeto);

    int insert(Asignacion objeto);

    int delete(Asignacion objeto);
    
    List<Asignacion> byOficio (Long oficioId);

}
