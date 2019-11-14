function skipto(targetpage,countpage){
	if(targetpage>0&targetpage<=countpage){
		window.location.href="myPost1.action?page="+targetpage;
	}
}
function getXmlHttp(){
	var xmlhttp;
	if (window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlhttp;
}
function deletePost(id){
	if(confirm("确定删除该帖子吗？操作不可逆")){
		var xmlhttp=this.getXmlHttp();
		if(xmlhttp != null) {
			xmlhttp.onreadystatechange = function(){
				if (xmlhttp.readyState == 4) {
					if (xmlhttp.status == 200) {
						if(xmlhttp.responseText=="true"){
							alert("删除成功");
							window.location.reload();
						}
					}
				}
			};
			xmlhttp.open("POST", "deletePost", false);
			xmlhttp.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=utf-8");
			xmlhttp.send("id=" + id );
		} else {
			alert("Your browser does not support XMLHTTP.");
		}
	}
}