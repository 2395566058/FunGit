var xmlhttp=null;
if (window.XMLHttpRequest){
  	xmlhttp=new XMLHttpRequest();
}else if (window.ActiveXObject){
 	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
}
function openFile(){
	document.getElementById("fileimg").click();
}
function changeFile(file){
	var divhead = document.getElementById("divhead");
	var reader = new FileReader();
	reader.onload = function(evt) {
		divhead.src = evt.target.result;
	}
	reader.readAsDataURL(file.files[0]);
}
function updateHead(){
	if(document.getElementById("fileimg").files[0]!=null){
		document.getElementById("updateBotton").innerHTML="修改中。。";
		if (xmlhttp!=null){
  			xmlhttp.onreadystatechange=state_Change;
  			xmlhttp.open("POST","updateImg",true);
			var data=new FormData();
			data.append("imgData",document.getElementById("fileimg").files[0]);
  			xmlhttp.send(data);
		}else{
  			alert("Your browser does not support XMLHTTP.");
		}
	}
}
function state_Change(){
	if (xmlhttp.readyState==4){
  		if (xmlhttp.status==200){
			document.getElementById("updateBotton").innerHTML="我要修改";
			alert(xmlhttp.responseText);
			parent.window.location.href="myInformation.action";
		}else{
    		alert("Problem retrieving XML data");
		}
    } 
}