/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.dao;

import edu.udg.oficios.data.Documento;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public class DOCUMENTO_IMPL_DAO extends DAOFACTORY<Documento> implements DOCUMENTODAO {

    private static final String SQL_INSERT
	    = " insert into oficpart.adjunto(adjunto_id, adjunto_nombre, adjunto_blob, adjunto_oficio_id) "
	    + " values( :adjuntoId, :nombre, :documento, :oficioId ) ";
    private static final String SQL_UPDATE = "";

    private static final String SQL_DELETE = "delete from oficpart.adjunto where adjunto_oficio_id = :oficioid";

    private static final String SQL_ALL = "";

    private static final String SQL_BUSCAR_ID = "SELECT"
	    + "    adjunto_id adjunto_id, "
	    + "    adjunto_nombre nombre, "
	    + "    adjunto_blob documento, "
	    + "    adjunto_oficio_id oficioid "
	    + "FROM "
	    + "    oficpart.adjunto "
	    + "WHERE "
	    + "    adjunto_oficio_id = :oficioid";

    private static final String SQL_BY_STATUS = "";

    public DOCUMENTO_IMPL_DAO() {
	super(SQL_ALL, SQL_INSERT, SQL_UPDATE, SQL_DELETE, SQL_BUSCAR_ID, SQL_BY_STATUS);
    }

    @Override
    Map<String, Object> convertObjtoParm(Documento object) {
	Map<String, Object> params = new HashMap<>();

	params.put("adjuntoId", object.getAdjuntoid());
	params.put("nombre", object.getNombre());
	params.put("documento", object.getDocumento());
	params.put("oficioId", object.getOficioid());

	return params;
    }

    @Override
    Documento conevrtDbToOjb(ResultSet resultSet) throws SQLException {
	System.out.println("######CONVERSION DE PARAMETROS A OBJETOS #########");
	Documento documento = new Documento();
	documento.setAdjuntoid(resultSet.getLong("adjunto_id"));
	documento.setNombre(resultSet.getString("nombre"));
	documento.setDocumento(resultSet.getBytes("documento"));
	documento.setOficioid(resultSet.getLong("oficioid"));
	return documento;
    }

    @Override
    public Documento buscarId(Long oficioId) {
	return find(SQL_BUSCAR_ID, oficioId);
    }

}
