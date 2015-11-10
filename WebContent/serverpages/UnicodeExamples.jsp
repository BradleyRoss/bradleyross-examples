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
<% 
    request.setCharacterEncoding("UTF-8");
    String unicodeValue = new String();
    String method = request.getMethod();
    if (method.equalsIgnoreCase("GET")) {
    	unicodeValue = URLDecoder.decode(request.getParameter("INPUT"), "UTF-8");
    } else if (method.equalsIgnoreCase("POST")) {
    	unicodeValue = request.getParameter("INPUT");
    }
    
%>
<p>Method: <%= request.getMethod() %></p>
<p>Processed value: <%= unicodeValue %></p>
<h2>References</h2>
<p><a href="http://www.hypergurl.com/urlencode.html" target="_blank"> 
http://www.hypergurl.com/urlencode.html</a></p>
<p><a href="http://andre.arko.net/2012/09/18/force-encoding-in-javascript/" target="_blank"> 
http://andre.arko.net/2012/09/18/force-encoding-in-javascript/</a></p>
<p><a href="http://www.456bereastreet.com/archive/201006/be_careful_with_non-ascii_characters_in_urls/" target="_blank">
http://www.456bereastreet.com/archive/201006/be_careful_with_non-ascii_characters_in_urls/</a></p>
<p><a href="http://stackoverflow.com/questions/19086190/request-getparameter-does-not-display-properly-character-encoding-in-java-serv" target="_blank">
http://stackoverflow.com/questions/19086190/request-getparameter-does-not-display-properly-character-encoding-in-java-serv
</a></p>
<p><a href="http://docs.oracle.com/javaee/6/api/javax/servlet/descriptor/JspPropertyGroupDescriptor.html" target="_blank">
   http://docs.oracle.com/javaee/6/api/javax/servlet/descriptor/JspPropertyGroupDescriptor.html</a></p>
<p>A Unicode character string encoded using UTF-8 contains characters that are illegal in URI
   strings.  The string must therefore be encoded (escaped) on the requesting page and
   decoded (unescaped) on the server when using the GET method.</p>
<ul>

<li>If method is GET, parameters must be encoded in JavaScript using encodeURI and 
    decoded in JSP using URLDecoder.decode to avoid characters which
    are illegal in URI.</li>
<li>If method is POST, there are two options.  The first is to encode in JavaScript and
    decode in Java.  The second choice is to skip both processes.</li>
</ul>
<p>According to <a href="http://stackoverflow.com/questions/6336923/how-can-i-cleanly-set-the-pageencoding-of-all-my-jsps" target="_blank">
   http://stackoverflow.com/questions/6336923/how-can-i-cleanly-set-the-pageencoding-of-all-my-jsps</a>, the parameters for a group
   of JSP page directives can be set using a <code>&lt;jsp-config&gt;</code> block in web.xml</p>
<p><a href="http://docs.oracle.com/cd/E13222_01/wls/docs92/webapp/overview.html" target="_blank">
   http://docs.oracle.com/cd/E13222_01/wls/docs92/webapp/overview.html</a></p>
<p><a href="http://docs.oracle.com/cd/E24329_01/web.1211/e21049/web_xml.htm#WBAPP531" target="_blank">
   http://docs.oracle.com/cd/E24329_01/web.1211/e21049/web_xml.htm#WBAPP531</a></p>
<p><code>&lt;jsp-config&gt;<br />
   &nbsp;&nbsp;&nbsp;&lt;jsp-property-group&gt;<br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;url-pattern&gt;*.jsp&lt;/url-pattern&gt;<br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;page-encoding&gt;UTF-8&lt;/page-encoding&gt;<br />
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;default-content-type&gt;text/html&lt;/default-content-type&gt;<br />   
   &nbsp;&nbsp;&nbsp;&lt;/jsp-property-group&gt;<br />
   &lt;/jsp-config&gt;</code></p>
</body>
</html>