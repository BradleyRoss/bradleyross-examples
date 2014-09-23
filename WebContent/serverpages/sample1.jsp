<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://bradleyross.github.com/examples" prefix="ex" %> 
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>center tag</title>
<style type="text/css">
.border { border: black 2px solid;}
</style>
</head>
<body>
<p> A tag handler bradleyross.j2ee.tags.CenterTag was created
    that surrounds the area between the start and end tag
    with tags that center the material.</p>
<p>Not centered</p>
<ex:center className="border">
<p>Centered line.  This line is between the start and end tags for ex:center.  That causes this 
   line to be centered.</p>
</ex:center>
<ex:center className="border" align="right">
<p>Right justified -  This line is between the<br /> start and 
   end tags for ex:center.  That causes this 
   line to be right justified.</p>
</ex:center>
<ex:center className="border" align="left">
left justified
</ex:center>
</body>
</html>