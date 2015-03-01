/*
 * This code creates an object as a property in the
 * window object.  This allows commonTools to be used
 * like a namespace, so that changes are unlikely to affect
 * other packages.  However, it is not actually a namespace,
 * even though it acts like one.
 */
var commonTools = new Object();
/**
 * Obtain a list of the properties of an object and
 * show the types for all of the property values.  The 
 * contents are placed into a table so that the information
 * can be presented easily.
 * 
 * dest - id for element into which information is to be
 *    placed using innerHTML
 * 
 * obj1 - first object to be analyzed
 * 
 * obj2 - second object to be analyzed (optional).  If a second object
 *    is placed in the parameter list, the third column will show the
 *    type of the property value in obj1 and the fourth column will show
 *    the type of the property value in obj2.  If the property doesn't 
 *    exist in one of the objects, there will be a blank.
 */
commonTools.getObjectInfo = function(dest, obj1, obj2) {
	var allKeys = new Array();
	var working = "";
	var counter = -1;
	if (obj1 != null) {
		for (var key in obj1) {
			allKeys.push(key);
		}
	}
	if (obj2 != null) {
		for (var key in obj2) {
			allKeys.push(key);
		}
	}
	allKeys.sort();
	for (var i = 0; i < allKeys.length; i++) {
		if (i > 0 && (allKeys[i] === allKeys[i-1])) {continue;}
		var key = allKeys[i];
		counter++;
		working = working + "<tr><td>" + counter + "</td><td>" + key + "</td>";
		if (obj1 && key in obj1) {
			working = working + "<td>" + typeof obj1[key] + "</td>";
		} else {
			working = working + "<td>&nbsp;</td>";
		}
		if (obj2) {
			if (key in obj2) {
				working = working + "<td>" + typeof obj2[key] + "</td>";
			} else {
				working = working + "<td>&nbsp;</td>";
			}
		}
		working = working + "</tr>";
	}
	var loc = document.getElementById(dest);
	loc.innerHTML = working;	
};
/*
 * This function will place the source code for a JavaScript function on
 * a web page.
 * 
 * dest - id for element into which text will be inserted using innterHTML
 * 
 * funct - the function for which the source is to be displayed
 */
commonTools.getFunctionSource = function(dest, funct) {
	var working = funct.toString();
	working = working.replace(/&/gm, "&amp;");
	working = working.replace(/</g,"&lt;");
	working = working.replace(/>/gm, "&gt;");
    working = working.replace(/(\r\n|\r|\n)/gm, "<br />");
    var loc = document.getElementById(dest);
    loc.innerHTML = working;
}