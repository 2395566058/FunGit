function getXmlHttp(){
	var xmlhttp;
	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlhttp;
}
function addreviewTag(useName,floor,status){
	if(status==0){
		alert("不可以回复被删除的楼层！");
		return;
	}
	var div=document.getElementById("content");
	div.innerHTML="<input disabled='disabled'id='reviewfloor"+floor+"'class='reviewTag'type='text'value='回复 "+useName+" "+floor+"楼:' />";
	div.focus();
}
function addtest2(){
	var div=document.getElementById("content");
	alert(div.innerHTML);
}
function fabiao(status,postid){
	var content=document.getElementById("content").innerHTML;
	if(content==""){
		return;
	}
	if(status=="false"){
		alert("要登录后才能发表想法呢。");
		return;
	}
	var xmlhttp=this.getXmlHttp();
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = function(){
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					var data=xmlhttp.responseText;
					if(data=="发表成功！"){
						alert(data);
						window.open("showPost.action?id="+postid+"&page=final")
					}
				}
			}
		};
		xmlhttp.open("POST", "reviewPost", false);
		xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("postid=" + postid + "&" +
				"content=" + content);
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}
function openFile() {
	document.getElementById("imgfile").click();
}
function addImage(file) {
	var img = document.createElement("img");
	img.className = "img";
	var div=document.getElementById("content");
	var reader = new FileReader();
	reader.onload = function(evt) {
		img.src = evt.target.result;
		div.appendChild(img);
		saveImage(file, img);
	}
	reader.readAsDataURL(file.files[0]);
}
function saveImage(file, img) {
	var xmlhttp=this.getXmlHttp();
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
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}
function addclicknum(postid){
	var xmlhttp=this.getXmlHttp();
	if (xmlhttp != null) {
		xmlhttp.open("POST", "addclicknum", true);
		xmlhttp.setRequestHeader("Content-Type",
		"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("postid="+postid);
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}
function skipto(pagetag,pagenum,postid,topage){
	topage=parseInt(topage)
	if(topage==""){
		topage=document.getElementById("skipPage").value;
	}
	if(topage==pagenum||topage<=0||topage>pagetag){
		return;
	}else{
		window.location.href="showPost.action?id="+postid+"&page="+topage;
	}
}