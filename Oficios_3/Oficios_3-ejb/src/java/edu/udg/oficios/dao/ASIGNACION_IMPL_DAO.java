/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.udg.oficios.dao;

import edu.udg.oficios.data.Asignacion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public class ASIGNACION_IMPL_DAO extends DAOFACTORY<Asignacion> implements ASIGNACIONDAO {
    //sql
    private static final String SQL_INSERT 
	    ="insert into oficpart.asignacion("
	    + "asig_id, "
	    + "asig_oficio_id, "
	    + "asig_emisor, "
	    + "asig_dep_emisor, "
	    + "asig_asignacion, "
	    + "asig_dep_asignado, "
	    + "asig_tipo, "
	    + "asig_fecha) "
	    + "values( :asig_id, "
	    + ":asig_oficio_id, "
	    + ":asig_emisor, "
	    + ":asig_dep_emisor, "
	    + ":asig_asignado, "
	    + ":asig_dep_asignado, "
	    + ":asig_tipo, "
	    + ":asig_fecha)";
    
    private static final String SQL_UPDATE 
	    ="update oficpart.asignacion "
	    + "set asig_emisor = :asig_emisor, "
	    + "	asig_dep_emisor = :asig_dep_emisor, "
	    + "	asig_asignacion = :asig_asignado, "
	    + "	asig_dep_asignado = :asig_dep_asignado, "
	    + "	asig_tipo = :asig_tipo, "
	    + "	asig_fecha = :asig_fecha "
	    + "where asig_oficio_id = :asig_oficio_id";
    
    private static final String SQL_DELETE 
	    ="delete from oficpart.asignacion "
	    + "	where asig_oficio_id = :asig_oficio_id";
    
    private static final String SQL_ALL 
	    ="select asig_id asig_id, "
	    + "asig_oficio_id asig_oficio_id, "
	    + "asig_emisor asig_emisor, "
	    + "asig_dep_emisor asig_dep_emisor, "
	    + "asig_asignacion asig_asignacion, "
	    + "asig_dep_asignado asig_dep_asignado, "
	    + "asig_tipo asig_tipo, "
	    + "asig_fecha asig_fecha "
	    + "from oficpart.asignacion "
	    + "COMMIT";
    
    private static final String SQL_BUSCAR_OFICIO_ID 
	    = SQL_ALL
	    + " where asig_oficio_id = :asig_oficio_id";
    
    private static final String SQL_BY_STATUS ="";

    public ASIGNACION_IMPL_DAO() {
	super(SQL_ALL, SQL_INSERT, SQL_UPDATE, SQL_DELETE, SQL_BUSCAR_OFICIO_ID, SQL_BY_STATUS);
    }
    

    @Override
    Map<String, Object> convertObjtoParm(Asignacion object) {
	//Objetos
	System.out.println("######CONVERSION DE OBJETOS A PARAMETROS #########  ???");
	Map<String, Object> params = new HashMap<>();
	
	//Put
	params.put("asig_id", object.getAsig_id());
	params.put("asig_oficio_id", object.getAsig_oficio_id());
	params.put("asig_emisor", object.getAsig_emisor());
	params.put("asig_dep_emisor", object.getAsig_dep_emisor());
	params.put("asig_asignacion", object.getAsig_asignado());
	params.put("asig_dep_asignado", object.getAsig_dep_asignado());
	params.put("asig_tipo", object.getAsig_tipo());
	params.put("asig_fecha", convertDate(object.getAsig_fecha()));
	
	//Resultado
	System.out.println("PARAMETROS CONVERTIDOS: " + params);
	return params;
    }

    @Override
    Asignacion conevrtDbToOjb(ResultSet resultSet) throws SQLException {
	//Objetos
	System.out.println("######CONVERSION DE PARAMETROS A OBJETOS #########");
	Asignacion asignacion = new Asignacion();
	
	//Push
	asignacion.setAsig_id(resultSet.getLong("asig_id"));
	System.out.println("ASIG IDEN: " + asignacion.getAsig_id());
	
	asignacion.setAsig_oficio_id(resultSet.getLong("asig_oficio_id"));
	System.out.println("ASIG OFIC: " + asignacion.getAsig_oficio_id());
	
	asignacion.setAsig_emisor(resultSet.getString("asig_emisor"));
	System.out.println("ASIG EMIS: " + asignacion.getAsig_emisor());
	
	asignacion.setAsig_dep_emisor(resultSet.getString("asig_dep_emisor"));
	System.out.println("ASIG DEPE: " + asignacion.getAsig_dep_emisor());
	
	asignacion.setAsig_asignado(resultSet.getString("asig_asignacion"));
	System.out.println("ASIG ASIG: " + asignacion.getAsig_asignado());
	
	asignacion.setAsig_dep_asignado(resultSet.getString("asig_dep_asignado"));
	System.out.println("ASIG DEPA: " + asignacion.getAsig_dep_asignado());
	
	asignacion.setAsig_tipo(resultSet.getString("asig_tipo"));
	System.out.println("ASIG TIPO: " + asignacion.getAsig_tipo());
	
	asignacion.setAsig_fecha(resultSet.getDate("asig_fecha"));
	System.out.println("ASIG FECH: " + asignacion.getAsig_fecha());
	
	//Resultado
	return asignacion;
    }

    @Override
    public Asignacion buscaAsignacion(Long asig_oficioId) {
	return find(SQL_BUSCAR_OFICIO_ID, asig_oficioId);
    }

    @Override
    public List<Asignacion> byOficio(Long oficioId) {
	return list(SQL_BUSCAR_OFICIO_ID, oficioId);
    }
}
