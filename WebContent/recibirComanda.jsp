<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.*, java.util.Iterator , diseno.telecomandas.dominio.Manager" %>

<%
	response.addHeader("Access-Control-Allow-Origin", "*");
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	int idMesa=jso.getInt("id");
	JSONArray platosMesa=jso.getJSONArray("platos");
	
	JSONObject respuesta=new JSONObject();
	try {
		Manager.get().recibirComanda(idMesa,platosMesa);
		respuesta.put("resultado", "OK");
	}
	catch (Exception e) {
		respuesta.put("resultado", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
%>

<%= respuesta %>
