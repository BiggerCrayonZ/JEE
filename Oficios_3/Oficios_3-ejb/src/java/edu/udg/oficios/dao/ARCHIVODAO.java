/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.dao;

import edu.udg.oficios.data.Archivo;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public interface ARCHIVODAO {

    int insert(Archivo archivo);
    
    int update(Archivo archivo);

}
