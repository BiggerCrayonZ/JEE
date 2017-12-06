/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.dao;

import edu.udg.oficios.data.Oficio;
import java.util.List;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public interface OFICIODAO {

    List<Oficio> buscasTodos();

    Oficio buscarId(Long oficioId);

    int update(Oficio objeto);

    int insert(Oficio objeto);

    int delete(Oficio objeto);

    List<Oficio> byEstatus(String estatus);
    
}
