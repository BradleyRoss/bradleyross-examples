<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Using jsp:plugin</title>
<style type="text/css">
.border { border: 1px solid black;}
</style>
</head>
<body>
<p>This page uses the JSP plugin to create a page using
   an applet.  It selects the correct code for inserting
   the applet based on the type of browser being used.
   If you look at the code when it arrives at the browser,
   only the appropriate tags using applet, object, or embed
   will appeqr on the page.</p>
<p>The jsp:plugin action can also contain jsp:params
   and jsp:fallback actions to define properties used
   when running the applet.</p>
<!-- 
This was the old code used to run some applets.
<applet code="local.scrollbarSample.class" archive="combined.jar"
height="300" width="500" ></applet>
<br />
<applet code="local.colorsSample.class" archive="combined.jar"
height="300" width="500" ></applet>
<br />
<applet code="local.oldJavaButtons.class" archive="combined.jar"
height="150" width="500" ></applet>
 -->
 <div class="border">
<jsp:plugin type="applet" code="bradleyross.library.applets.ScrollbarSample.class" 
     codebase="../applets" 
     jreversion="5"
     archive="bradleyross_examples.jar" height="400" width="400" >
     <jsp:fallback>
     <p>Problem running applet</p>
     </jsp:fallback>
</jsp:plugin>
</div>
<br />
<div class="border">
<jsp:plugin type="applet" code="bradleyross.library.applets.ColorsSample.class" 
     codebase="../applets" 
     jreversion="5"
     archive="bradleyross_examples.jar" height="400" width="400" >
     <jsp:fallback>
     <p>Problem running applet</p>
     </jsp:fallback>
</jsp:plugin>
</div>
</body>
</html>