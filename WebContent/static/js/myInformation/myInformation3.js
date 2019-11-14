var xmlhttp=null;
if (window.XMLHttpRequest){
  	xmlhttp=new XMLHttpRequest();
}else if (window.ActiveXObject){
 	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
}
function updatePassword(){
	var oldPassword=document.getElementById("oldpassword");
	var newPassword=document.getElementById("newpassword");
	var newPassword2=document.getElementById("newpassword2");
	if(oldPassword.value!=''&&newPassword.value!=''){
		if(newPassword.value!=newPassword2.value){
			alert("二次密码不一致!");
			return;
		}
		if (xmlhttp!=null){
			document.getElementById("updateButtom").innerHTML="修改中。。";
  			xmlhttp.onreadystatechange=state_Change;
  			xmlhttp.open("POST","updatePassword",true);
			xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
  			xmlhttp.send("oldPassword="+oldPassword.value+"&"+
				"newPassword="+newPassword.value
			);
		}else{
  			alert("Your browser does not support XMLHTTP.");
		}
	}
}
function state_Change(){
	if (xmlhttp.readyState==4){
  		if (xmlhttp.status==200){
  			document.getElementById("updateButtom").innerHTML="我要修改";
			alert(xmlhttp.responseText);
			if(xmlhttp.responseText=="修改成功！"){
				parent.window.location.href="myInformation.action";
			}
		}else{
    		alert("Problem retrieving XML data");
		}
    } 
}