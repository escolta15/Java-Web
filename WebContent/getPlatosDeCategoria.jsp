<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.*, diseno.telecomandas.dominio.Manager" %>

<%
	response.addHeader("Access-Control-Allow-Origin", "*");
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	
	String idCategoria=jso.getString("idCategoria");
	JSONArray categorias=Manager.get().getPlatosDeCategoria(idCategoria);
%>

<%= categorias %>