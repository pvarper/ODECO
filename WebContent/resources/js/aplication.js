function setaFoco(elemento) {
	var keyCode = event.keyCode ? event.keyCode : event.which ? event.which
			: event.charCode;
	if (keyCode == 13) {
		var i;
		for (i = 0; i < elemento.form.elements.length; i++)
			if (elemento == elemento.form.elements[i])
				break;
		i = (i + 1) % elemento.form.elements.length;
		elemento.form.elements[i].focus();
		event.preventDefault();
		return false;
	}
	return false;
}

function isTeclaF2(e) {
	tecla = document.all ? e.keyCode : e.which;
	return (tecla == 113);
}

function start() {
	startButton1.disable();
	window['progress'] = setInterval(function() {
		var oldValue = pbClient.getValue(), newValue = oldValue + 10;

		pbClient.setValue(pbClient.getValue() + 10);

		if (newValue == 100) {
			clearInterval(window['progress']);
		}

	}, 1000);
}

function cancel() {
	clearInterval(window['progress']);
	pbClient.setValue(0);
	startButton1.enable();
}

function maximaLongitud(texto, maxlong) {
	var in_value, out_value;

	alert(texto);

	if (texto.value.length > maxlong) {
		in_value = texto.value;
		out_value = in_value.substring(0, maxlong);
		texto.value = out_value;
		return false;
	}
	return true;
}

function ValidarCaracteres(textareaControl, maxlength) {

	if (textareaControl.value.length > maxlength) {
		textareaControl.value = textareaControl.value.substring(0, maxlength);
		alert("Debe ingresar hasta un maximo de " + maxlength + " caracteres");
	}
}

function onlyNumbers(evt) {
	var keyPressed = (evt.which) ? evt.which : event.keyCode;
	return !(keyPressed > 31 && (keyPressed < 48 || keyPressed > 57));
}

function encryptPassword() {
	var texto = document.getElementById("formLogin:password").value;
	document.getElementById("formLogin:password").value = Base64.encode(texto);
}

function FinalizarBO() {
	var ok = document.getElementById("formPanel:completar");
	if (ok != null) {
		if (ok.value == 0) {
			return false;

		} else {
			return true;
		}

	}
	return false;

}
