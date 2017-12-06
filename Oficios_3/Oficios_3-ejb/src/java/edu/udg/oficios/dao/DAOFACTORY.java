/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.udg.oficios.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Raúl Reza
 */
public abstract class DAOFACTORY<T> {

    private static final String DATASOURCE_CONTEXT = "jdbc/oficioESCEV";
    private final String SQL_ALL;
    private final String SQL_INSERT;
    private final String SQL_UPDATE;
    private final String SQL_DELETE;
    private final String SQL_FIND_ID;
    private final String SQL_FILTER_BY_STATUS;

    public DAOFACTORY(String sql_all, String sql_insert, String sql_update, String sql_delete, String sql_buscar_id, String sql_by_estatus) {
	this.SQL_ALL = sql_all;
	this.SQL_INSERT = sql_insert;
	this.SQL_UPDATE = sql_update;
	this.SQL_DELETE = sql_delete;
	this.SQL_FIND_ID = sql_buscar_id;
	this.SQL_FILTER_BY_STATUS = sql_by_estatus;

    }
    //--------------------------------------------------------------------------//
    //Metodos de insercion actualización y eliminación
    //--------------------------------------------------------------------------//

    public int insert(T object) throws DAOEXCEPTION {
	try {
	    return executeUpdate(SQL_INSERT, convertObjtoParm(object));
	} catch (SQLException | NamingException e) {
	    throw new DAOEXCEPTION(e);
	}
    }

    public int update(T object) throws DAOEXCEPTION {
	try {
	    return executeUpdate(SQL_UPDATE, convertObjtoParm(object));
	} catch (SQLException | NamingException e) {
	    throw new DAOEXCEPTION(e);
	}
    }

    public int delete(T object) throws DAOEXCEPTION {
	try {
	    return executeUpdate(SQL_DELETE, convertObjtoParm(object));
	} catch (SQLException | NamingException e) {
	    throw new DAOEXCEPTION(e);
	}
    }

    //--------------------------------------------------------------------------//
    //Metodo de Busqueda individual en ejecución de query
    //--------------------------------------------------------------------------//
    public T find(String sql, Object... values) throws DAOEXCEPTION {
	T object = null;
	try {
	    object = executeQuery(sql, values);
	} catch (SQLException | NamingException e) {
	    throw new DAOEXCEPTION(e);
	}
	return object;
    }

    //--------------------------------------------------------------------------//
    //Consulta completa
    //--------------------------------------------------------------------------//
    public List<T> findAll() throws DAOEXCEPTION {
	System.out.println("# FUNCION FIND ALL#");
//	return (List<T>) convertObjtoParm((T) list(SQL_ALL));
	return list(SQL_ALL);
    }

    //--------------------------------------------------------------------------//
    //Ejecucion lista de query´s
    //--------------------------------------------------------------------------//
    public List<T> list(String sql, Object... values) throws DAOEXCEPTION {
	List<T> list = null;
	try {
	    System.out.println("# FUNCION LIST#");
	    list = executeQueryList(sql, values);
	} catch (SQLException | NamingException e) {
	    throw new DAOEXCEPTION(e);
	}
	return list;
    }

    //--------------------------------------------------------------------------//
    //Ejecución de query con un solo resultado
    //--------------------------------------------------------------------------//
    public Object singleResult(String sql, Object... values) throws DAOEXCEPTION {
	Object object = null;
	try {
	    object = executeQuerySingleResult(sql, values);
	} catch (NamingException | SQLException e) {
	    throw new DAOEXCEPTION(e);
	}
	return object;
    }

    //--------------------------------------------------------------------------//
    //Seteo de resultados
    //--------------------------------------------------------------------------//
    public List<T> resultSet(String sql, Object... values) throws DAOEXCEPTION {
	List<T> objects = null;
	try {
	    objects = executeQueryList(sql, values);
	} catch (NamingException | SQLException e) {
	    throw new DAOEXCEPTION(e);
	}
	return objects;
    }

    //--------------------------------------------------------------------------//
    //Metodos de ejecución de Query´s
    //--------------------------------------------------------------------------//
    public int executeUpdate(String sql, Map<String, Object> params) throws SQLException, NamingException {
	Connection conecction = null;
	CallableStatement statement = null;
	ResultSet resultSet = null;
	try {
	    conecction = getConnection();
	    statement = conecction.prepareCall(sql);
	    setParams(statement, params, sql);
	    return statement.executeUpdate();
	} finally {
	    if (resultSet != null) {
		resultSet.close();
	    }
	    if (statement != null) {
		statement.close();
	    }
	    if (conecction != null) {
		conecction.close();
	    }
	}
    }

    private T executeQuery(String sql, Object... values) throws SQLException, NamingException {
	List<T> list = executeQueryList(sql, values);
	return list.size() > 0 ? list.get(0) : null;
    }

    public List<T> executeQueryList(String sql, Object... values) throws SQLException, NamingException {
	Connection conecction = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	List<T> list = null;
	try {
	    conecction = getConnection();
	    statement = conecction.prepareStatement(sql);
	    setValues(statement, values);
	    resultSet = statement.executeQuery();
	    list = new ArrayList<>();
	    while (resultSet.next()) {
		T Object = conevrtDbToOjb(resultSet);
		list.add(Object);
	    }
	} finally {
	    if (resultSet != null) {
		resultSet.close();
	    }
	    if (statement != null) {
		statement.close();
	    }
	    if (conecction != null) {
		conecction.close();
	    }
	}
	return list;
    }

    private Object executeQuerySingleResult(String sql, Object... values) throws SQLException, NamingException {
	Connection conecction = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	Object object = null;

	try {
	    conecction = getConnection();
	    statement = conecction.prepareStatement(sql);
	    setValues(statement, values);
	    resultSet = statement.executeQuery();

	    if (resultSet.next()) {
		object = resultSet.getObject(1);
	    }
	} finally {
	    if (resultSet != null) {
		resultSet.close();
	    }
	    if (statement != null) {
		statement.close();
	    }
	    if (conecction != null) {
		conecction.close();
	    }
	}
	return object;

    }

    //--------------------------------------------------------------------------//
    //Metodos de conexion y seteo
    //--------------------------------------------------------------------------//
    private Connection getConnection() throws NamingException, SQLException {
	InitialContext initialContext = new InitialContext();
	DataSource datasource = (DataSource) initialContext.lookup(DATASOURCE_CONTEXT);
	return datasource.getConnection();
    }

    private void setValues(PreparedStatement statement, Object... values) throws SQLException {
	for (int i = 0; i < values.length; i++) {
	    statement.setObject(i + 1, values[i]);
	}
    }

    private void setParams(CallableStatement statement, Map<String, Object> values, String sql) throws SQLException {
	String[] split = sql.replaceAll(",", " ").split(" ");
	List<String> nameParams = new ArrayList<>();
	System.out.println("PARAMETROS POR CONVERTIR: " + nameParams.toString());
	for (String string : split) {
	    if (string.startsWith(":")) {
		nameParams.add(
			string.
			replaceAll(",", "").
			replaceAll(":", "").
			replaceAll("\\)", "").
			replaceAll(";", ""));
	    }
	}
	System.out.println(nameParams.toString());

	for (String string : nameParams) {
	    Object object = values.get(string);
	    System.out.println("Key:" + string + "->Value:" + object.toString());
	    statement.setObject(string, object);
	}
    }

    //--------------------------------------------------------------------------//
    //Conversiones de tipos
    //--------------------------------------------------------------------------//
    abstract Map<String, Object> convertObjtoParm(T object);

    abstract T conevrtDbToOjb(ResultSet resultSet) throws SQLException;

    //--------------------------------------------------------------------------//
    //Metodo de conversión de fecha
    //--------------------------------------------------------------------------//
    public java.sql.Date convertDate(java.util.Date dateUtil) {
	if (dateUtil != null) {
	    return new java.sql.Date(dateUtil.getTime());
	} else {
	    return null;
	}
    }

}
