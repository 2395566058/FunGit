function toHome() {
	parent.window.location.href = "home.action";
}
function toMyInformation() {
	parent.window.location.href = "myInformation.action";
}
function toMyPost() {
	parent.window.location.href = "myPost.action";
}
function toMyPost2() {
	parent.window.location.href = "myPost.action?part=2";
}
function toMyPost4(){
	parent.window.location.href = "myPost.action?part=4";
}
function toIssuePost() {
	parent.window.location.href = "issuePost.action";
}
this.ttag(num);
var xmlhttp = null;
if (window.XMLHttpRequest) {
	xmlhttp = new XMLHttpRequest();
} else if (window.ActiveXObject) {
	xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
}
this.getOnlineStatus();
this.existNotRead();
function showTags() {
	document.getElementById("shortimg").style.display = "block";
	document.getElementById("dropDown").style.display = "block";
}
function closeTags() {
	document.getElementById("shortimg").style.display = "none";
	document.getElementById("dropDown").style.display = "none";
}
function tagColor(button) {
	button.style.backgroundColor = "#999";
}
function tagColor2(button) {
	button.style.backgroundColor = "#FFF";
}
var xiaoxiICON=document.getElementById("xiaoxiICON");
function existNotRead(id) {
	var xmlhttp2 = null;
	if (window.XMLHttpRequest) {
		xmlhttp2 = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		xmlhttp2 = new ActiveXObject("Microsoft.XMLHTTP");
	}
	if (xmlhttp2 != null) {
		xmlhttp2.onreadystatechange = function() {
			if (xmlhttp2.readyState == 4) {
				if (xmlhttp2.status == 200) {
					var result = xmlhttp2.responseText;
					if (result == "true") {
						xiaoxiICON.className="imginfo";
					} else {
						xiaoxiICON.className="imginfo2";
					}
					setTimeout(existNotRead, 2000)
				}
			}
		}
		xmlhttp2.open("POST", "existNotRead", true);
		xmlhttp2.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp2.send();
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}
function exitStatus() {
	if (confirm("还可以再看一会哦，确定要退出吗？")) {
		if (xmlhttp != null) {
			xmlhttp.onreadystatechange = state_Change;
			xmlhttp.open("POST", "exit", true);
			xmlhttp.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=utf-8");
			xmlhttp.send();
		} else {
			alert("Your browser does not support XMLHTTP.");
		}
	}
}
function state_Change() {
	if (xmlhttp.readyState == 4) {
		if (xmlhttp.status == 200) {
			alert(xmlhttp.responseText);
			if (xmlhttp.responseText == "已退出登录") {
				parent.window.location.href = "login.action";
			}
		} else {
			alert("Problem retrieving XML data");
		}
	}
}
function ttag(num) {
	if (num == 1) {
		document.getElementById("tag1").style.backgroundColor = "#FFF";
		document.getElementById("tag1").style.borderColor = "inherit";
		return;
	}
	if (num == 2) {
		document.getElementById("tag2").style.backgroundColor = "#FFF";
		document.getElementById("tag2").style.borderColor = "inherit";
		return;
	}
	if (num == 3) {
		document.getElementById("tag3").style.backgroundColor = "#FFF";
		document.getElementById("tag3").style.borderColor = "inherit";
		return;
	}
	if (num == 4) {
		document.getElementById("tag4").style.backgroundColor = "#FFF";
		document.getElementById("tag4").style.borderColor = "inherit";
		return;
	}
	if (num == 5) {
		document.getElementById("tag5").style.backgroundColor = "#FFF";
		document.getElementById("tag5").style.borderColor = "inherit";
		return;
	}
}
function getOnlineStatus() {
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					if (xmlhttp.responseText != "false") {
						alert("你的账号已在其他地方登录！");
						parent.window.location.href = "login.action";
					}
					getOnlineStatus();
				}
			}
		};
		xmlhttp.open("POST", "getOnlineStatus", true);
		xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send();
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}