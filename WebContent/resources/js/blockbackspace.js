//Function: Common javascript file to prevent backspace key from going back to the previous page.
//Author: Taeho Lee
//Creation date: December 3, 2009
//Suported Browser: Internet Explorer, Firefox, Chrome
//Usage: <bf:script src="/includes/blockbackspace.js"/>

//Every single key press action will call this function
//Return true: disable backspace
function checkDisableBackspace(e)
{
	var key;

	 //return when the key is not backspace key
	if(e) {
		key = e.which? e.which : e.keyCode;
		if(key == null || ( key != 8 && key != 13)){
			return false;
		}
	}
	else
	{
		return false;
	}

	if (e.srcElement)
	{
		//ie
		tag = e.srcElement.tagName.toUpperCase();
		type = e.srcElement.type;
		readOnly =e.srcElement.readOnly;
		if( type == null){ //Type is null means the mouse focus on a non-form field. Disable backspace button
			return true;
		}
		else
		{
			type = e.srcElement.type.toUpperCase();
		}
	} else {
		//firefox
		tag = e.target.nodeName.toUpperCase(); 
		type = (e.target.type) ? e.target.type.toUpperCase() : "";
	}

	//DO NOT disable backspace ever if focus is not in an input control
	if (tag == 'INPUT' || type == 'TEXT' || type == 'TEXTAREA')
	{
		if(readOnly == true)
		{
			return true;  //if the field is disabled, disable the back space button
		}

		if(((tag == 'INPUT' && type == 'RADIO') || (tag == 'INPUT' && type == 'CHECKBOX')) && (key == 8 || key == 13))
		{
			return true; //disable the backspace button when radio or checkbox
		}

		return false;
	}

	//if none of above cases, disable the backspace
	return (key == 8 || key == 13);
}

//check browser type
function checkCurrentWebBrowser() {
	var agt=navigator.userAgent.toLowerCase();
	if (agt.indexOf("msie") != -1) { return 'msie';
	} else if (agt.indexOf("firefox") != -1) { return 'firefox';
	} else if (agt.indexOf("chrome") != -1) { return 'chrome';
	} else {return navigator.userAgent;}
}

//Global events every key press
var browser = checkCurrentWebBrowser();
if(browser == 'msie'){
	document.onkeydown = function() { return !checkDisableBackspace(event); }
}else if(browser == 'firefox'){
	document.onkeypress = function(e) { return !checkDisableBackspace(e); }
}else if(browser == 'chrome'){
	document.onkeydown = function(e) { return !checkDisableBackspace(e); }
}
