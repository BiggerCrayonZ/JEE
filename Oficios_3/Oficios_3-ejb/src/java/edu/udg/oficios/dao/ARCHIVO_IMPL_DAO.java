/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.dao;

import edu.udg.oficios.data.Archivo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Raúl E. Reza del Castillo <raul.reza.delcastillo@gmail.com>
 */
public class ARCHIVO_IMPL_DAO extends DAOFACTORY<Archivo> implements ARCHIVODAO {

    //Variables
//    private static final String SQL_INSERT
//	    = "insert into oficpart.oficio_archivo("
//	    + "id_archivo, "
//	    + "usuario_destino, "
//	    + "departamento_destino, "
//	    + "archivo_saludo, "
//	    + "archivo_cuerpo, "
//	    + "archivo_despedida, "
//	    + "usuario_emisor, "
//	    + "departamento_emisor) "
//	    + "values( "
//	    + ":archivoId, "
//	    + ":usuario_destino, "
//	    + ":departamento_destino, "
//	    + ":archivo_saludo, "
//	    + ":archivo_cuerpo, "
//	    + ":archivo_despedida, "
//	    + ":usuario_emisor, "
//	    + ":departamento_emisor)";
    private static final String SQL_INSERT
	    = "insert all into oficpart.oficio_archivo("
	    + "id_archivo, "
	    + "usuario_destino, "
	    + "departamento_destino, "
	    + "archivo_saludo, "
	    + "archivo_cuerpo, "
	    + "archivo_despedida, "
	    + "usuario_emisor, "
	    + "departamento_emisor) "
	    + "values( "
	    + ":archivoId, "
	    + ":usuario_destino, "
	    + ":departamento_destino, "
	    + ":archivo_saludo, "
	    + ":archivo_cuerpo, "
	    + ":archivo_despedida, "
	    + ":usuario_emisor, "
	    + ":departamento_emisor) "
	    //Aqui la inserción de la clase oficio
	    + " into oficpart.oficio("
	    + "oficio_id, "
	    + "oficio_folio, "
	    + "oficio_asunto, "
	    + "oficio_fecha_oficio, "
	    + "oficio_fecha_vencimiento, "
	    + "oficio_observaciones, "
	    + "oficio_layer, "
	    + "oficio_class, "
	    + "oficio_priori, "
	    + "oficio_archivo, "
	    + "oficio_tipo, "
	    + "oficio_version, "
	    + "oficio_borrador, "
	    + "oficio_bandera_vencimiento) "
	    + " values( "
	    + ":oficioId, "
	    + ":folio, "
	    + ":asunto, "
	    + ":fecha_oficio, "
	    + ":fecha_vencimiento, "
	    + ":observaciones, "
	    + ":layer, "
	    + ":class, "
	    + ":priori, "
	    + ":oficio_archivo, "
	    + ":oficio_tipo, "
	    + ":oficio_version, "
	    + ":oficio_borrador, "
	    + ":oficio_bandera_vencimiento) "
	    + "select * from dual";
    private static final String SQL_DELETE
	    = " delete from oficpart.oficio_archivo "
	    + "  where id_archivo = :archivoId";

    private static final String SQL_UPDATE
	    = " update oficpart.oficio_archivo "
	    + "set usuario_destino = :usuario_destino, "
	    + "departamento_destino = :departamento_destino, "
	    + "archivo_saludo = :archivo_saludo, "
	    + "archivo_cuerpo = :archivo_cuerpo, "
	    + "archivo_despedida = :archivo_despedida, "
	    + "usuario_emirsor = :usuario_emisor, "
	    + "departamento_emisor = :departamento_emirsor"
	    + " where id_archivo = :archivoId ";

    private static final String SQL_ALL = "";

    private static final String SQL_BUSCAR_ID = "";

    private static final String SQL_BY_STATUS = "";

    //Constructores
    public ARCHIVO_IMPL_DAO() {
	super(SQL_ALL, SQL_INSERT, SQL_UPDATE, SQL_DELETE, SQL_BUSCAR_ID, SQL_BY_STATUS);
    }

    //METODOS DE SOBREESCRITURA
    @Override
    Map<String, Object> convertObjtoParm(Archivo object) {
	System.out.println("###### CONVERSION DE OBJETOS A PARAMETROS ###### ??");
	Map<String, Object> params = new HashMap<>();
	params.put("archivoId", object.getArchivoId());
	params.put("usuario_destino", object.getUsuario_destino());
	params.put("departamento_destino", object.getDepartamento_destino());
	params.put("archivo_saludo", object.getArchivo_saludo());
	params.put("archivo_cuerpo", object.getArchivo_cuerpo());
	params.put("archivo_despedida", object.getArchivo_despedida());
	params.put("usuario_emisor", object.getUsuario_emirsor());
	params.put("departamento_emisor", object.getDepartamento_emirsor());
	//Datos del apartado de oficio
	params.put("oficioId", object.getOficioId());
	params.put("folio", object.getFolio());
	params.put("asunto", object.getAsunto());
	params.put("fecha_oficio", convertDate(object.getFecha_oficio()));
	params.put("fecha_vencimiento", convertDate(object.getFecha_vencimiento()));
	params.put("observaciones", object.getObservaciones());
	params.put("layer", 1);
	params.put("class", object.getClass_id());
	params.put("priori", object.getPrioridad_id());
	params.put("oficio_archivo", object.getOficio_archivo());
	params.put("oficio_tipo", object.getOficio_tipo());
	params.put("oficio_version", object.getOficio_version());
	params.put("oficio_borrador", object.getOficio_borrador());
	params.put("oficio_bandera_vencimiento", object.getOficio_bandera_vencimiento());

	System.out.println("PARAMETROS CONVERTIDOS: " + params);

	return params;
    }

    @Override
    Archivo conevrtDbToOjb(ResultSet resultSet) throws SQLException {
	Archivo archivo = new Archivo();

	//seteamos y verificamos
	archivo.setArchivoId(resultSet.getLong("archivoId"));
	System.out.println("ARCHIVO ID: " + archivo.getArchivoId());
	//
	archivo.setUsuario_destino(resultSet.getString("usuario_destino"));
	System.out.println("ARCHIVO USUARIO DESTINO: " + archivo.getUsuario_destino());
	//
	archivo.setDepartamento_destino(resultSet.getString("departamento_destino"));
	System.out.println("ARCHIVO DEPARTAMENTO DESTINO: " + archivo.getDepartamento_destino());
	//
	archivo.setArchivo_saludo(resultSet.getString("archivo_saludo"));
	System.out.println("ARCHIVO SALUDO: " + archivo.getArchivo_saludo());
	//
	archivo.setArchivo_cuerpo(resultSet.getString("archivo_cuerpo"));
	System.out.println("ARCHIVO CUERPO: " + archivo.getArchivo_cuerpo());
	//
	archivo.setArchivo_despedida(resultSet.getString("archivo_despedida"));
	System.out.println("ARCHIVO DESPEDIDA: " + archivo.getArchivo_despedida());
	//
	archivo.setUsuario_emirsor(resultSet.getString("usuario_emirsor"));
	System.out.println("ARCHIVO USUARIO EMISOR: " + archivo.getUsuario_emirsor());
	//
	archivo.setDepartamento_emirsor(resultSet.getString("departamento_emirsor"));
	System.out.println("ARCHIVO DEPARTAMENTO EMISOR: " + archivo.getDepartamento_emirsor());
	//retornamos archivo
	return archivo;
    }

}
