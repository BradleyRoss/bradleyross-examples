<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.net.URLDecoder"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Showing Unicode values</title>
</head>
<body>
<p>These are some Unicode characters written using the Unicode literals with both the hexadecimal
   and decimal representation.</p>
<table> 
<tr><td>Using hex</td><td>Using dec</td><td>Description</td></tr>
<tr><td>&#x0100;</td><td>&#0256;</td><td>Latin capital letter a with macron</td></tr>
<tr><td>&#x0101;</td><td>&#0257;</td><td>Latin small letter a with macron</td></tr>
<tr><td>&#x0112;</td><td>&#0274;</td><td>Latin capital letter e with macron</td></tr>
<tr><td>&#x0113;</td><td>&#0275;</td><td>Latin small letter e with macron</td></tr>
<tr><td>&#x0114;</td><td>&#0276;</td><td>Latin capital letter e with breve</td></tr>
<tr><td>&#x0115;</td><td>&#0277;</td><td>Latin small letter e with breve</td></tr>
</table>
<% String val =URLDecoder.decode(request.getParameter("INPUT"), "UTF-8"); %>
<p>Test: <% out.print(val); %></p>

<h2>References</h2>
<p><a href="http://www.hypergurl.com/urlencode.html" target="_blank"> http://www.hypergurl.com/urlencode.html</a></p>
<p><a href="http://andre.arko.net/2012/09/18/force-encoding-in-javascript/" target="_blank"> http://andre.arko.net/2012/09/18/force-encoding-in-javascript/</a></p>

</body>
</html>