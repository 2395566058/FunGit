var div = document.getElementById("divcontent");
var xmlhttp;
function openFile() {
	document.getElementById("fileimg").click();
}
function selectForum(value) {
	var forumshow = document.getElementById("forumName");
	forumshow.innerText = value;
}
function addImage(file) {
	var img = document.createElement("img");
	img.className = "img";
	var reader = new FileReader();
	reader.onload = function(evt) {
		img.src = evt.target.result;
		div.appendChild(img);
		saveImage(file, img);
	}
	reader.readAsDataURL(file.files[0]);
}
function saveImage(file, img) {
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					if (xmlhttp.responseText != "false") {
						img.src = xmlhttp.responseText;
						return;
					}
					alert("出现未知错误！图片可能无法正常加载！");
				} else {
					alert("Problem retrieving XML data");
				}
			}
		};
		xmlhttp.open("POST", "addImagefromIssuePost", true);
		var data = new FormData();
		data.append("imgData", file.files[0]);
		xmlhttp.send(data);
	}
}
function issue() {
	var forumshow = document.getElementById("forumName").innerText;
	var title = document.getElementById("title").value;
	if (forumshow == "版块选择") {
		alert("请先从左方选择您帖子所属版块");
		return;
	}
	if (title == "") {
		alert("标题不能为空！");
		return;
	}
	if (div.innerHTML == "") {
		alert("帖子内容不能为空！");
		return;
	}
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = state_Change;
		xmlhttp.open("POST", "issuePost", true);
		xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("title=" + title + "&" + "forumid=" + forumshow + "&"
				+ "content=" +encodeURIComponent(div.innerHTML));
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}
function state_Change() {
	if (xmlhttp.readyState == 4) {
		if (xmlhttp.status == 200) {
			if (xmlhttp.responseText == "false") {
				alert("发布失败！请稍后重试！");
			} else {
				alert("发布成功，跳转至页面！");
				window.location.href="showPost.action?id="+xmlhttp.responseText;
			}
		} else {
			alert("Problem retrieving XML data");
		}
	}
}
if (window.XMLHttpRequest) {
	xmlhttp = new XMLHttpRequest();
} else if (window.ActiveXObject) {
	xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
}