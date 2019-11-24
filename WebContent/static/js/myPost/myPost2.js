function skipto(targetpage,countpage){
	if(targetpage>0&targetpage<=countpage){
		window.location.href="myPost2.action?page="+targetpage;
	}
}
function xiaoshi(div){
	div.style.display="none";
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
function showreviewdiv(id){
	var xmlhttp=this.getXmlHttp();
	var bigshowreviewdiv=document.getElementById("bigshowreviewdiv");
	var showreviewdiv=document.getElementById("showreviewdiv");
	if(xmlhttp != null) {
		xmlhttp.onreadystatechange = function(){
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					showreviewdiv.innerHTML="";
					var obj = JSON.parse(xmlhttp.responseText);
					for(var i=0;i<obj.length;i++){
						var div=document.createElement("div");
						var divid="id"+id+"floor"+obj[i].floor;
						div.id=divid;
						div.className = "newPostDiv";
						var param="\'"+id+"\',\'"+obj[i].floor+"\',\'"+divid+"\'";
						var existReviewName;
						if(obj[i].reviewName==""){
							existReviewName="<br> 帖子发言";
						}else{
							existReviewName="<br> 回复  ";
						}
						div.innerHTML=" "+obj[i].floor+"楼      发表时间："+obj[i].issuetime+"<div style='cursor:pointer;margin-right:20px;float:right' onclick=\"deletereview("+param+")\">删除</div>"+existReviewName+obj[i].reviewName+":<br>"+obj[i].content;
						showreviewdiv.appendChild(div);
					}
					bigshowreviewdiv.style.display="block";
				}
			}
		};
		xmlhttp.open("POST", "showPostReviewByPostId", true);
		xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		xmlhttp.send("postid=" + id );
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}
function deletereview(postid,floor,div){
	if(confirm("确定删除你在该层的发言吗？操作不可逆")){
		var xmlhttp=this.getXmlHttp();
		if(xmlhttp != null) {
			xmlhttp.onreadystatechange = function(){
				if (xmlhttp.readyState == 4) {
					if (xmlhttp.status == 200) {
						if(xmlhttp.responseText=="true"){
							alert("删除成功");
							var childdiv=document.getElementById(div);
							childdiv.parentNode.removeChild(childdiv);
						}
					}
				}
			};
			xmlhttp.open("POST", "deleteReviewByFloor", true);
			xmlhttp.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=utf-8");
			xmlhttp.send("postid=" + postid+"&"+
					"floor="+floor);
		} else {
			alert("Your browser does not support XMLHTTP.");
		}
	}
}
function deletePost(postid){
	if(confirm("确定删除你在该帖子的所有发言吗？操作不可逆")){
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
			xmlhttp.open("POST", "deleteReviewAll", false);
			xmlhttp.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=utf-8");
			xmlhttp.send("postid=" + postid );
		} else {
			alert("Your browser does not support XMLHTTP.");
		}
	}
}