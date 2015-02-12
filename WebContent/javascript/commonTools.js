var commonTools = new Object();
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
			for (var key in obj2) {
				allKeys.push(key);
			}
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
		var loc = document.getElementById(dest);
		loc.innerHTML = working;
	}
};