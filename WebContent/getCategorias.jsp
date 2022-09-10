<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.*, diseno.telecomandas.dominio.Manager" %>

<%
	response.addHeader("Access-Control-Allow-Origin", "*");
	JSONArray resultado=Manager.get().getCategorias();
%>

<%= resultado %>
