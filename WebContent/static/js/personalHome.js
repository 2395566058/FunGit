var birthdaySelect = document.getElementById("birthdaySelect");
var qqSelect = document.getElementById("qqSelect");
var emailSelect = document.getElementById("emailSelect");
var citySelect = document.getElementById("citySelect");
var registerSelect = document.getElementById("registerSelect");
var postSelect = document.getElementById("postSelect");
var birthday;
var qq;
var email;
var city;
var register;
var post;
birthday = qq = email = city = register = post = "0";
if (birthdaySelect.checked == false) {
	birthday = "1";
}
if (qqSelect.checked == false) {
	qq = "1";
}
if (emailSelect.checked == false) {
	email = "1";
}
if (citySelect.checked == false) {
	city = "1";
}
if (registerSelect.checked == false) {
	register = "1";
}
if (postSelect.checked == false) {
	post = "1";
}
function toupdate(column, columnValue, num) {
	if (num == 1) {
		if (birthday != columnValue) {
			sendUpdate(column, columnValue, "1");
		}
	} else if (num == 2) {
		if (qq != columnValue) {
			sendUpdate(column, columnValue, "2");
		}
	} else if (num == 3) {
		if (email != columnValue) {
			sendUpdate(column, columnValue, "3");
		}
	} else if (num == 4) {
		if (city != columnValue) {
			sendUpdate(column, columnValue, "4");
		}
	} else if (num == 5) {
		if (register != columnValue) {
			sendUpdate(column, columnValue, "5");
		}
	} else if (num == 6) {
		if (post != columnValue) {
			sendUpdate(column, columnValue, "6");
		}
	}
}
function sendUpdate(column, columnValue, num) {
	var xmlhttp = null;
	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					alert(xmlhttp.responseText);
					if (xmlhttp.responseText == "修改成功") {
						if (num == 1) {
							birthday = columnValue;
						} else if (num == 2) {
							qq = columnValue;
						} else if (num == 3) {
							email = columnValue;
						} else if (num == 4) {
							city = columnValue;
						} else if (num == 5) {
							register = columnValue;
						} else if (num == 6) {
							post = columnValue;
						}
					}
				}
			}
		}
		xmlhttp.open("POST", "updateErmission", true);
		xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("column=" + column + "&columnValue=" + columnValue);
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}