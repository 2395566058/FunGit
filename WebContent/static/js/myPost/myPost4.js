function getXmlHttp() {
	var xmlhttp;
	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlhttp;
}

function showreviewdivs(title, ids) {
	var bigshowreviewdiv = document.getElementById("bigshowreviewdiv");
	var showreviewdiv = document.getElementById("showreviewdiv");
	if (title == "他人评论") {
		var dataArray = ids.split("|");
		var size = dataArray.length;
		for (var i = 0; i < size; i++) {
			var xmlhttp = this.getXmlHttp();
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4) {
					if (xmlhttp.status == 200) {
						var data = xmlhttp.responseText;
						var result = JSON.parse(data);
						var div = document.createElement("div");
						var divid = "id" + dataArray[i] + "floor" + result.floor;
						div.id = divid;
						div.className = "newPostDiv";
						var param = "\'" + dataArray[i] + "\',\'" + result.floor + "\'";
						div.innerHTML = " "
							+ result.floor
							+ "楼      "
							+ "<div style='cursor:pointer;margin-right:20px;float:right' onclick=\"deletereview("
							+ param + ")\">删除</div>"
							+"<span style='margin-right:30px;font-size:15px;float:right'>"
							+"发表时间："
							+ "<span style='font-size:16px;color:#1b1b1b'>"
							+ result.issuetime
							+"</span></span>"
							+"<br>" + result.content;
						showreviewdiv.appendChild(div);
					}
				}
			}
			xmlhttp.open("POST", "getCommentFromPeopleForNotice", false);
			xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
			xmlhttp.send("id=" + dataArray[i]);
		}
		bigshowreviewdiv.style.display = "block";
	}
	if (title == "他人回复") {

	}
	if (title == "系统通知") {

	}
}

function getCommentFromPeople(id) {
	var xmlhttp = this.getXmlHttp();
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					var result = xmlhttp.responseText;
					this.result = result;
				}
			}
		}
		xmlhttp.open("POST", "getCommentFromPeopleForNotice", false);
		xmlhttp.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("id=" + id);
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
	return result;
}

function getReviewFromPeople() {
	var xmlhttp = this.getXmlHttp();
	if (xmlhttp != null) {
		var result;
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					var result = xmlhttp.responseText;
					return result;
				}
			}
		}
		xmlhttp.open("POST", "getReviewFromPeopleForNotice", false);
		xmlhttp.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("id=" + id);
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}

function getSystemInfo() {
	var xmlhttp = this.getXmlHttp();
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					var result = xmlhttp.responseText;
					return result;
				}
			}
		}
		xmlhttp.open("POST", "getSystemInfoForNotice", true);
		xmlhttp.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("id=" + id);
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}