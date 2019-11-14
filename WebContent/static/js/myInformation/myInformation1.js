var xmlhttp=null;
if (window.XMLHttpRequest){
  	xmlhttp=new XMLHttpRequest();
}else if (window.ActiveXObject){
 	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
}
function updateInformation(){
	document.getElementById("updateButton").innerHTML="修改中。。";
	if (xmlhttp!=null){
  		xmlhttp.onreadystatechange=state_Change;
  		xmlhttp.open("POST","updateInformation",true);
		xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
  		xmlhttp.send("name="+document.getElementById("name").value+"&"+
		"birthday="+document.getElementById("birthday").value+"&"+
		"qq="+document.getElementById("qq").value+"&"+
		"city="+document.getElementById("city").value+"&"+
		"introduce="+document.getElementById("introduce").value
		);
	}else{
  		alert("Your browser does not support XMLHTTP.");
	}
}
function state_Change(){
	if (xmlhttp.readyState==4){
  		if (xmlhttp.status==200){
  			document.getElementById("updateButton").innerHTML="我要修改";
			alert(xmlhttp.responseText);
			if(xmlhttp.responseText=="修改成功！"){
				parent.window.location.href="myInformation.action";
			}
		}else{
    		alert("Problem retrieving XML data");
		}
    } 
}