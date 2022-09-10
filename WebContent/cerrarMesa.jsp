<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.*, diseno.telecomandas.dominio.Manager" %>

<%
	response.addHeader("Access-Control-Allow-Origin", "*");
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	int idMesa=jso.getInt("_id");
	
	JSONObject respuesta=new JSONObject();
	try {
		Manager.get().cerrarMesa(idMesa);
		respuesta.put("resultado", "OK");
	}
	catch (Exception e) {
		respuesta.put("resultado", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
%>

<%= respuesta %>
