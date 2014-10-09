<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://bradleyross.github.com/examples" prefix="ex" %> 
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>center tag</title>
<link rel="stylesheet" type="text/css" href="jsp-examples" />
</head>
<body>
<h1>ConvertToComment Tag</h1>
<p>The tag ex:ConvertToComment should convert the text between the start and
   end tags so that it appears on the page as a comment with the control
   characters being escaped.</p>
<hr />
<ex:ConvertToComment className="border">
<p>This is the first line of the section to be converted.</p>
<p>This is the second line in the converted section.</p>
</ex:ConvertToComment>
<hr />
<p>This should appear after the converted code.</p>
</body>
</html>