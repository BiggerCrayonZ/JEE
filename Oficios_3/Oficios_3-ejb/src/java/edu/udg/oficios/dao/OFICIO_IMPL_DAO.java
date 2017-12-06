/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.dao;

import edu.udg.oficios.data.Oficio;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public class OFICIO_IMPL_DAO extends DAOFACTORY<Oficio> implements OFICIODAO {

    private Date fecha_oficio;
    private static final String SQL_INSERT
	    = " insert into oficpart.oficio("
	    + "oficio_id, "
	    + "oficio_folio, "
	    + "oficio_asunto, "
	    + "oficio_fecha_oficio, "
	    + "oficio_fecha_vencimiento, "
	    + "oficio_observaciones, "
	    + "oficio_layer, "
	    + "oficio_class, "
	    + "oficio_priori, "
	    + "oficio_tipo, "
	    + "oficio_borrador, "
	    + "oficio_version, "
	    + "oficio_bandera_vencimiento) "
	    + " values( :oficioId, "
	    + ":folio, "
	    + ":asunto, "
	    + ":fecha_oficio, "
	    + ":fecha_vencimiento, "
	    + ":observaciones, "
	    + ":layer, :class, "
	    + ":priori, "
	    + ":oficio_tipo, "
	    + ":oficio_borrador, "
	    + ":oficio_version, "
	    + ":oficio_bandera_vencimiento)";

    private static final String SQL_UPDATE
	    = " update oficpart.oficio "
	    + "    set oficio_folio = :folio, "
	    + "        oficio_asunto = :asunto, "
	    + "        oficio_fecha_oficio = :fecha_oficio, "
	    + "        oficio_fecha_vencimiento = :fecha_vencimiento, "
	    + "        oficio_observaciones = :observaciones, "
	    + "        oficio_class = :class, "
	    + "        oficio_layer = :layer, "
	    + "        oficio_priori = :priori, "
	    + "        oficio_tipo = :oficio_tipo, "
	    + "        oficio_borrador = :oficio_borrador, "
	    + "        oficio_version = :oficio_version, "
	    + "        oficio_bandera_vencimiento = :oficio_bandera_vencimiento "
	    + "  where oficio_id = :oficioId";
    private static final String SQL_DELETE
	    = " delete from oficpart.oficio "
	    + "  where oficio_id = :oficioId";

    private static final String SQL_ALL
	    = " select oficio_id oficioId, "
	    + "oficio_folio folio, "
	    + "oficio_asunto asunto, "
	    + "oficio_fecha_oficio fecha_oficio, "
	    + "oficio_fecha_vencimiento fecha_vencimiento, "
	    + "oficio_observaciones observaciones, "
	    + "oficio_layer layer,"
	    + "oficio_class class,"
	    + "oficio_priori priori,"
	    + "oficio_archivo archivoId, "
	    + "oficio_tipo oficio_tipo, "
	    + "oficio_borrador oficio_borrador, "
	    + "oficio_version oficio_version, "
	    + "oficio_bandera_vencimiento oficio_bandera_vencimiento "
	    + "from oficpart.oficio "
	    + "COMMIT";
    private static final String SQL_BUCAR_ID
	    = SQL_ALL
	    + " where oficio_id = :oficioId";
    private static final String SQL_BY_ESTATUS
	    = SQL_ALL
	    + " where oficio_layer= :layer ";

    public OFICIO_IMPL_DAO() {
	super(SQL_ALL, SQL_INSERT, SQL_UPDATE, SQL_DELETE, SQL_BUCAR_ID, SQL_BY_ESTATUS);
    }

    @Override
    Map<String, Object> convertObjtoParm(Oficio object) {
	System.out.println("######CONVERSION DE OBJETOS A PARAMETROS #########  ???");
	Map<String, Object> params = new HashMap<>();
	params.put("oficioId", object.getOficioId());
	params.put("folio", object.getFolio());
	params.put("asunto", object.getAsunto());
	params.put("fecha_oficio", convertDate(object.getFecha_oficio()));
	params.put("fecha_vencimiento", convertDate(object.getFecha_vencimiento()));
	params.put("observaciones", object.getObservaciones());
	params.put("layer", 1);
	params.put("class", object.getClass_id());
	params.put("priori", object.getPrioridad_id());
	params.put("archivoId", object.getOficioId());
	params.put("oficio_tipo", object.getOficio_tipo());
	params.put("oficio_borrador", object.getOficio_borrador());
	params.put("oficio_version", object.getOficio_version());
	params.put("oficio_bandera_vencimiento", object.getOficio_bandera_vencimiento());

	System.out.println("PARAMETROS CONVERTIDOS: " + params);

	return params;
    }

    @Override
    Oficio conevrtDbToOjb(ResultSet resultSet) throws SQLException {
	System.out.println("######CONVERSION DE PARAMETROS A OBJETOS #########");
	Oficio oficio = new Oficio();
	oficio.setOficioId(resultSet.getLong("oficioid"));
	System.out.println("OFICIO id: " + oficio.getOficioId());
	oficio.setFolio(resultSet.getString("folio"));
	System.out.println("OFICIO folio: " + oficio.getFolio());
	oficio.setAsunto(resultSet.getString("asunto"));
	System.out.println("OFICIO asunto: " + oficio.getAsunto());
	oficio.setFecha_oficio(resultSet.getDate("fecha_oficio"));
	System.out.println("OFICIO fecha de oficio: " + oficio.getFecha_oficio());
	oficio.setFecha_vencimiento(resultSet.getDate("fecha_vencimiento"));
	System.out.println("OFICIO fecha de vencimiento: " + oficio.getFecha_vencimiento());
	oficio.setObservaciones(resultSet.getString("observaciones"));
	System.out.println("OFICIO observaciones: " + oficio.getObservaciones());
	switch (resultSet.getInt("layer")) {
	    case 1:
		oficio.setEstatus("Nuevo");
		break;
	    case 2:
		oficio.setEstatus("En Proceso");
		break;
	    case 3:
		oficio.setEstatus("En Espera");
		break;
	    case 4:
		oficio.setEstatus("En Suspendido");
		break;
	    case 5:
		oficio.setEstatus("En Terminado");
		break;
	    case 6:
		oficio.setEstatus("En Cancelado");
		break;
	    default:
		oficio.setEstatus("Sin Estatus");
		break;
	}
	System.out.println("OFICIO layer:  " + oficio.getEstatus());
	switch (resultSet.getInt("class")) {
	    case 1:
		oficio.setClase("Memorándum");
		break;
	    case 2:
		oficio.setClase("Oficio");
		break;
	    case 3:
		oficio.setClase("Invitación");
		break;
	    case 4:
		oficio.setClase("Académico");
		break;
	    case 5:
		oficio.setClase("Personal");
		break;
	    default:
		oficio.setClase("Sin Clase");
		break;
	}
	System.out.println("OFICIO clase:  " + oficio.getClase());
	switch (resultSet.getInt("priori")) {
	    case 1:
		oficio.setPrioridad("Baja");
		break;
	    case 2:
		oficio.setPrioridad("Media");
		break;
	    case 3:
		oficio.setPrioridad("Alta");
		break;
	    case 4:
		oficio.setPrioridad("Urgente");
		break;
	    default:
		oficio.setPrioridad("Sin Prioridad");
		break;
	}
	System.out.println("OFICIO prioridad:  " + oficio.getPriori());
	oficio.setArchivoId(resultSet.getLong("archivoId"));
	System.out.println("OFICIO ARCHIVOID:  " + oficio.getArchivoId());
	oficio.setOficio_tipo(resultSet.getInt("oficio_tipo"));
	System.out.println("OFICIO TIPO DATO:  " + oficio.getOficio_tipo());
	switch (resultSet.getInt("oficio_tipo")) {
	    case 0:
		oficio.setOficio_tipo_string("Oficio Adjuntado al Sistema");
		break;
	    case 1:
		oficio.setOficio_tipo_string("Oficio Creado en el Sistema");
		break;
	}
	System.out.println("OFICIO TIPO:  " + oficio.getOficio_tipo_string());
	oficio.setOficio_borrador(resultSet.getInt("oficio_borrador"));
	switch (resultSet.getInt("oficio_borrador")) {
	    case 0:
		oficio.setOficio_borrador_string("Oficio Borrador");
		break;
	    case 1:
		oficio.setOficio_borrador_string("Oficio Oficial");
		break;
	}
	System.out.println("OFICIO BORRADOR:  " + oficio.getOficio_borrador());
	System.out.println("OFICIO TIPO BORRADOR:  " + oficio.getOficio_borrador_string());
	oficio.setOficio_version(resultSet.getInt("oficio_version"));
	System.out.println("OFICIO VERSION:  " + oficio.getOficio_version());
	oficio.setOficio_bandera_vencimiento(resultSet.getInt("oficio_bandera_vencimiento"));
	System.out.println("OFICIO VENCIMEINTO:  " + oficio.getOficio_bandera_vencimiento());
	return oficio;
    }

    @Override
    public List<Oficio> buscasTodos() {
	System.out.println("PUNTO DE RETURN DE SQL");
	return findAll();
    }

    @Override
    public Oficio buscarId(Long oficioId) {
	return find(SQL_BUCAR_ID, oficioId);
    }

    @Override
    public List<Oficio> byEstatus(String estatus) {
	return list(SQL_BY_ESTATUS, estatus);
    }

}
