function getWorkingDays()
{
    var startDate = document.getElementById("id_FormBody:id_FechaIni_input");
    var endDate = document.getElementById("id_FormBody:id_FechaFin_input");
    var result = 0;
    var currentDate = startDate;


    while (currentDate <= endDate)
    {
        var weekDay = currentDate.getDay();
        if(weekDay != 0 && weekDay != 6)
            result++;
        currentDate.setDate(currentDate.getDate()+1);
    }

}

function startpage() {
    if (useAccessibility && isSkipNavigation) {
        skipNavigation();
    }
}

function validarNro(e) {
    var key=0;
    if(window.event) // IE
    {
        key = e.keyCode;
    }
    else if(e.which) // Netscape/Firefox/Opera
    {
        key = e.which;
    }
    if (48>key  || key > 57)
    {
        if( key == 8 ) // Detectar backspace (retroceso)
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
    return true;
}

function validarMonto(e) {
    var key=0;
    var valor;
    var valorcomas;
    valor=document.getElementById("id_FormBody:id_Monto").value;
    valorcomas=contarcomas(valor);
    if(window.event) // IE
    {
        key = e.keyCode;
    }
    else if(e.which) // Netscape/Firefox/Opera
    {
        key = e.which;
    }
    if (48>key  || key > 57)
    {
        if(key == 8)
        {
            return true;
        }
        else 
        {
            if(key == 44 && valorcomas<1) // Detectar . (punto) y backspace (retroceso)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    return true;
}
function contarcomas(valor)
{
    var valor1=0;
    for(var i=0;i<valor.length;i++)
    {         
        if(valor.substring(i,i+1)==",")
        {
            valor1=valor1+1;
        }
    }
    return valor1;
}
