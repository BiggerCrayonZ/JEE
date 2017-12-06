/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.dao;

import edu.udg.oficios.data.Documento;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public interface DOCUMENTODAO {

    int update(Documento objeto);

    int insert(Documento objeto);

    int delete(Documento objeto);
    
    Documento buscarId(Long oficioId);

}
