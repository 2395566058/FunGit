function getXmlHttp() {
	var xmlhttp;
	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlhttp;
}
var getMoreHotPost_status="0";
function getMoreHotPost() {
	var xmlhttp = this.getXmlHttp();
	if(getMoreHotPost_status=="1"){
		return;
	}
	var parentDiv=document.getElementById("addHotPost");
	parentDiv.innerHTML="<span>正在努力加载中。。</span>";
	if (xmlhttp != null) {
		getMoreHotPost_status="1";
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					if (xmlhttp.responseText == "") {
						alert("暂时没有更多热点帖子呢");
					} else {
						var data=JSON.parse(xmlhttp.responseText);
						var data2="";
						for(var i=0;i<data.length;i++){
							data2=data2+"<div onselectstart='return true;' class='hostpost'>"+
								"<div class='hostposttitle' onclick='window.open(\"showPost.action?id="+data[i].id+"\")'>"+
								"<span style='width:100%;display:block;font-size:28px;margin-left:10px;overflow: hidden;white-space:nowrap;text-overflow:ellipsis;'>"+data[i].title+"</span>"+
								"</div>"+
								"<div class='hostpostnum' onclick='window.open(\"showPost.action?id="+data[i].id+"\")'>"+
								"<div style='margin-top:5px;font-size:16px'>点击量："+data[i].clicknum+"</div>"+
								"<div style='margin-top:5px;font-size:16px'>评论量："+data[i].reviewnum+"</div>"+
								"</div>"+
								"<div class='hostposthead' onclick='window.open(\"personalHomeByName.action?name="+data[i].content+"\")'>"+
				        		"<div class='imghead' style='background-image: url(\""+data[i].userid+"\");'></div>"+
				        		"</div>"+
				        		"<div class='hostpostname' onclick='window.open(\"showPost.action?id="+data[i].id+"\")'>"+data[i].content+"</div>"+
				            	"<div class='hostpostother' onclick='window.open(\"showPost.action?id="+data[i].id+"\")'>"+
				            	"<span style='margin-left:30px;'>发表于版块："+data[i].forumid+"</span>"+
				        		"<span style='float:right;margin-right:25px;'>发表时间："+data[i].issuetime+"</span>"+
				            	"</div>"+
				            	"</div>"+
				            	"</div>";
						}
						parentDiv.innerHTML=data2;
						getMoreHotPost_status="0";
					}
				}
			}
		};
		xmlhttp.open("POST", "getMoreHotPost", false);
		xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("num=0");
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}
var getMoreNewPost_status="0";
function getMoreNewPost() {
	var xmlhttp = this.getXmlHttp();
	if(getMoreNewPost_status=="1"){
		return;
	}
	var parentDiv=document.getElementById("addNewPost");
	parentDiv.innerHTML="<span>正在努力加载中。。</span>";
	if (xmlhttp != null) {
		getMoreNewPost_status="1";
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					if (xmlhttp.responseText == "") {
						alert("暂时没有更多新帖子呢");
					} else {
						var data=JSON.parse(xmlhttp.responseText);
						var data2="";
						for(var i=0;i<data.length;i++){
							var div=document.createElement("div");;
							data2=data2+"<div onselectstart='return true;' class='hostpost'>"+
								"<div class='hostposttitle' onclick='window.open(\"showPost.action?id="+data[i].id+"\")'>"+
								"<span style='width:100%;display:block;font-size:28px;margin-left:10px;overflow: hidden;white-space:nowrap;text-overflow:ellipsis;'>"+data[i].title+"</span>"+
								"</div>"+
								"<div class='hostpostnum' onclick='window.open(\"showPost.action?id="+data[i].id+"\")'>"+
								"<div style='margin-top:5px;font-size:16px'>点击量："+data[i].clicknum+"</div>"+
								"<div style='margin-top:5px;font-size:16px'>评论量："+data[i].reviewnum+"</div>"+
								"</div>"+
								"<div class='hostposthead'  onclick='window.open(\"personalHomeByName.action?name="+data[i].content+"\")'>"+
				        		"<div class='imghead' style='background-image: url(\""+data[i].userid+"\");'></div>"+
				        		"</div>"+
				        		"<div class='hostpostname' onclick='window.open(\"showPost.action?id="+data[i].id+"\")'>"+data[i].content+"</div>"+
				            	"<div class='hostpostother' onclick='window.open(\"showPost.action?id="+data[i].id+"\")'>"+
				            	"<span style='margin-left:30px;'>发表于版块："+data[i].forumid+"</span>"+
				            	"<span style='margin-right:15px;position:absolute;left:310px;'>发表时间："+data[i].issuetime+"</span>"+
				            	"</div>"+
				            	"</div>"+
				            	"</div>";
						}
						parentDiv.innerHTML=data2;
						getMoreNewPost_status="0";
					}
				}
			}
		};
		xmlhttp.open("POST", "getMoreNewPost", false);
		xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("num=0");
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}

var blockTitle="";
var tagstatus="0";
function toblock(blocktitle){
	if(blockTitle==blocktitle&tagstatus=="1"){
		closeblock();
		tagstatus="0";
	}else{
		blockTitle=blocktitle;
		closeblock();
		openblock(0);
		tagstatus="1";
	}
}

var blockhr=document.getElementById("blockhr");
var blockhr2=document.getElementById("blockhr2");
var blockdiv=document.getElementById("blockdiv");
function openblock(i){
	if(i<600){
		i=i+25;
		blockhr.style.width=i+"px";
		blockhr2.style.width=i+"px";
		setTimeout(() => {
			openblock(i)
		}, 10);
	}else{
		openblockdiv(0);
	}
}

function openblockdiv(i){
	if(i<555){
		i=i+17.5;
		blockdiv.style.height=i+"px";
		setTimeout(() => {
			openblockdiv(i)
		}, 10);
	}else{
		blockdiv.innerHTML="<span onselectstart='return false;' style='font-size:25px;margin-right:40px;' class='hotTitle'>"+blockTitle+"专区</span>"+
			"<span id='tag1' style='font-size:14px;float:left;margin-top:26px;cursor:pointer;margin-right:20px;color:blue' onclick='changeTag(1)'>热门</span>"+
			"<span id='tag2' style='font-size:14px;float:left;margin-top:26px;cursor:pointer;margin-right:20px' onclick='changeTag(2)'>随便看看</span>"+
			"<span id='tag3' style='font-size:14px;float:left;margin-top:26px;cursor:pointer;margin-right:20px' onclick='changeTag(3)'>最新发布</span><br />"+
			"<object id='objectdiv' style='height:90%;width:98%' data='showBlockPost.action?block="+blockTitle+"&type=hot'></object>";
	}
}

function closeblock(){
	if(blockhr.clientWidth=="600"){
		blockdiv.innerHTML="";
		closeblockdiv("555");
		closeblockhr("600");
	}
}

function closeblockdiv(i){
	if(i!=0){
		i=i-17.5;
		blockdiv.style.height=i+"px";
		setTimeout(() => {
			closeblockdiv(i)
		}, 10);
	}
}

function closeblockhr(i){
	if(i!=0){
		i=i-25;
		blockhr.style.width=i+"px";
		blockhr2.style.width=i+"px";
		setTimeout(() => {
			closeblockhr(i)
		}, 10);
	}
}

function changeTag(num){
	var tag1=document.getElementById("tag1");
	var tag1=document.getElementById("tag1");
	var tag1=document.getElementById("tag1");
	tag1.style.color=tag2.style.color=tag3.style.color="black";
	if(num==1){
		tag1.style.color="blue";
		document.getElementById("objectdiv").data="showBlockPost.action?block="+blockTitle+"&type=hot";
	}else if(num==2){
		tag2.style.color="blue";
		document.getElementById("objectdiv").data="showBlockPost.action?block="+blockTitle+"&type=randow";
	}else{
		tag3.style.color="blue";
		document.getElementById("objectdiv").data="showBlockPost.action?block="+blockTitle+"&type=new";
	}
}

function searchPost(input,select){
	if(input.value==null||input.value==""){
		alert("请输入搜索的关键词！");
		return;
	}
	window.open("showSearchPost.action?input="+input.value+"&select="+select.value);
}