var hotnum = 5;
var newnum = 5;
function getXmlHttp() {
	var xmlhttp;
	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlhttp;
}
function getMoreHotPost() {
	var xmlhttp = this.getXmlHttp();
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					if (xmlhttp.responseText == "") {
						alert("暂时没有更多热点帖子呢");
					} else {
						var data=JSON.parse(xmlhttp.responseText);
						var parentDiv=document.getElementById("addHotPost");
						var parentDivDiv=parentDiv.parentNode;
						for(var i=0;i<data.length;i++){
							var div=document.createElement("div");;
							div.innerHTML="<div onselectstart='return true;' class='hostpost' onclick='window.open(\"showPost.action?id="+data[i].id+"\")'>"+
								"<div class='hostposttitle'>"+
								"<span style='font-size:28px;margin-left:10px;'>"+data[i].title+"</span>"+
								"</div>"+
								"<div class='hostpostnum'>"+
								"<div style='margin-top:5px;font-size:16px'>点击量："+data[i].clicknum+"</div>"+
								"<div style='margin-top:5px;font-size:16px'>评论量："+data[i].reviewnum+"</div>"+
								"</div>"+
								"<div class='hostposthead'>"+
				        		"<div class='imghead' style='background-image: url(\""+data[i].userid+"\");'></div>"+
				        		"</div>"+
				        		"<div class='hostpostname'>"+data[i].content+"</div>"+
				            	"<div class='hostpostother'>"+
				            	"<span style='margin-left:30px;'>发表于版块："+data[i].forumid+"</span>"+
				        		"<span style='float:right;margin-right:25px;'>发表时间："+data[i].issuetime+"</span>"+
				            	"</div>"+
				            	"</div>"+
				            	"</div>";
							parentDivDiv.insertBefore(div,parentDiv);
							parentDivDiv.scrollTop = parentDivDiv.scrollHeight;
							hotnum++;
						}
					}
				}
			}
		};
		xmlhttp.open("POST", "getMoreHotPost", false);
		xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("num=" + hotnum);
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}

function getMoreNewPost() {
	var xmlhttp = this.getXmlHttp();
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					if (xmlhttp.responseText == "") {
						alert("暂时没有更多新帖子呢");
					} else {
						var data=JSON.parse(xmlhttp.responseText);
						var parentDiv=document.getElementById("addNewPost");
						var parentDivDiv=parentDiv.parentNode;
						for(var i=0;i<data.length;i++){
							var div=document.createElement("div");;
							div.innerHTML="<div onselectstart='return true;' class='hostpost' onclick='window.open(\"showPost.action?id="+data[i].id+"\")'>"+
								"<div class='hostposttitle'>"+
								"<span style='font-size:28px;margin-left:10px;'>"+data[i].title+"</span>"+
								"</div>"+
								"<div class='hostpostnum'>"+
								"<div style='margin-top:5px;font-size:16px'>点击量："+data[i].clicknum+"</div>"+
								"<div style='margin-top:5px;font-size:16px'>评论量："+data[i].reviewnum+"</div>"+
								"</div>"+
								"<div class='hostposthead'>"+
				        		"<div class='imghead' style='background-image: url(\""+data[i].userid+"\");'></div>"+
				        		"</div>"+
				        		"<div class='hostpostname'>"+data[i].content+"</div>"+
				            	"<div class='hostpostother'>"+
				            	"<span style='margin-left:30px;'>发表于版块："+data[i].forumid+"</span>"+
				            	"<span style='margin-right:15px;position:absolute;left:310px;'>发表时间："+data[i].issuetime+"</span>"+
				            	"</div>"+
				            	"</div>"+
				            	"</div>";
							parentDivDiv.insertBefore(div,parentDiv);
							parentDivDiv.scrollTop = parentDivDiv.scrollHeight;
							newnum++;
						}
					}
				}
			}
		};
		xmlhttp.open("POST", "getMoreNewPost", false);
		xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("num=" + newnum);
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}
function searchPost(input,select){
	if(input.value==null||input.value==""){
		alert("请输入搜索的关键词！");
		return;
	}
	window.open("showSearchPost.action?input="+input.value+"&select="+select.value);
}