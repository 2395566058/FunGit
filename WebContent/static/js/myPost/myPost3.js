function skipto(targetpage, countpage) {
	targetpage = parseInt(targetpage)
	if (targetpage > 0 & targetpage <= countpage) {
		window.location.href = "myPost3.action?page=" + targetpage;
	}
}
function xiaoshi(div) {
	div.style.display = "none";
	var showreviewdiv = document.getElementById("showreviewdiv");
	showreviewdiv.innerHTML = "";
}
function getXmlHttp() {
	var xmlhttp;
	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlhttp;
}
function isread(ids) {
	var id = ids.split("|");
	if(id.length==1){
		this.sendisread(id);
	}else{
		for(var i=0;i<id.length;i++){
			this.sendisread(id[i]);
		}
	}
}
function sendisread(id){
	var xmlhttp = this.getXmlHttp();
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					var result = xmlhttp.responseText;
					if (result == "true") {
						window.location.reload();
					} else {
						alert("发生未知错误");
					}
				}
			}
		}
		xmlhttp.open("POST", "isreadById", true);
		xmlhttp.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("id=" + id);
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}
function showreviewdiv(id) {
	var xmlhttp = this.getXmlHttp();
	var bigshowreviewdiv = document.getElementById("bigshowreviewdiv");
	var showreviewdiv = document.getElementById("showreviewdiv");
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					showreviewdiv.innerHTML = "";
					var obj = JSON.parse(xmlhttp.responseText);
					for (var i = 0; i < obj.length; i++) {
						var div = document.createElement("div");
						var divid = "id" + id + "floor" + obj[i].floor;
						div.id = divid;
						div.className = "newPostDiv";
						var param = "\'" + id + "\',\'" + obj[i].floor + "\'";
						div.innerHTML = " " +
							"<div>"+
							obj[i].floor +
							"楼      " +
							"<div style='cursor:pointer;margin-right:20px;float:right' onclick=\"deletereview(" +
							param + ")\">删除</div>"+
							"<span style='margin-right:30px;font-size:15px;float:right'>" +
							"发表时间：" +
							"<span style='font-size:16px;color:#1b1b1b'>" +
							obj[i].issuetime +
							"</span></span>" +
							"</div><br>" + obj[i].content;
						showreviewdiv.appendChild(div);
						var review = obj[i].review;
						for (var j = 0; j < review.length; j++) {
							var div2 = document.createElement("div");
							div2.className = "newPostDiv2";
							div2.innerHTML = " " + review[j].floor
								+ "楼      发表时间：" + review[j].issuetime
								+ "<br><div >" + review[j].userName
								+ ":</div><br>" + review[j].content;
							showreviewdiv.appendChild(div2);
						}
					}
					bigshowreviewdiv.style.display = "block";
				}
			}
		};
		xmlhttp.open("POST", "showReviewMeByPostId", true);
		xmlhttp.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("postid=" + id);
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}
function deletereview(postid, floor) {
	if (confirm("确定删除你在该层的发言吗？操作不可逆")) {
		var xmlhttp = this.getXmlHttp();
		if (xmlhttp != null) {
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4) {
					if (xmlhttp.status == 200) {
						if (xmlhttp.responseText == "true") {
							alert("删除成功");
							window.location.reload();
						}
					}
				}
			};
			xmlhttp.open("POST", "deleteReviewByFloor", true);
			xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
			xmlhttp.send("postid=" + postid + "&floor=" + floor);
		} else {
			alert("Your browser does not support XMLHTTP.");
		}
	}
}
function gotoPost(id) {
	parent.window.open("showPost.action?id=" + id);
}