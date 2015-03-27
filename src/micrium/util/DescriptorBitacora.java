package micrium.util;

public enum DescriptorBitacora {

	USUARIO("Gestor Usuario"),
	GRUPO("Gestor Grupo"),
	PARAMETROS("Gestor Parametros"),
	ROL("Rol"),
	
	MENSAJES("Administracion de Mensajes"),
	MOTIVO_RECLAMO("Motivo de Reclamo"),
	CONFIGURACION_CALENDARIO("Configuracion de Calendario"),
	EXPORTARREPORTE1("Exportar Reporte 1"),
	EXPORTARREPORTE2("Exportar Reporte 2"),
	EXPORTARREPORTE3("Exportar Reporte 3"),
	EXPORTARREPORTE4("Exportar Reporte 4"),
	EXPORTARREPORTE5("Exportar Reporte 5"),
	EXPORTARREPORTE6("Exportar Reporte 6"),
	OBJETO_RECLAMO("Objeto de Reclamo"),
	RESPUESTA_ODECO("Respuesta Odeco"),
	FLUJO("Flujo"),
	
	ODECO_PRIMERA_INSTANCIA("Odeco de primera Instancia"),
	ODECO_PRIMERA_INSTANCIA_RECARGA("Odeco de primera Instancia Recarga"),
	ODECO_SEGUNDA_INSTANCIA("Odeco de segunda Instancia"),
	ODECO_REVISION_AGENTE("Odeco Revision de Agente"),
	ODECO_REVISION_STAFF("Odeco Revision de Staff"),
	ODECO_REVISION_COORDINADOR("Odeco Revision de Coordinador"),
	DERIVACION_TODOS_COORDINADOR("Derivacion todos los Coordinadores"),
	DERIVACION_ESPECIFICO_COORDINADOR("Derivacion Coordinador Especifico"),
	ODECO_REVISION_REGULACION("Odeco Revision de Regulacion"),
	DEVUELTO("Devuelto a Agente para revision");
	

	private final String formulario;

	DescriptorBitacora(String formulario) {
		this.formulario = formulario;
	}

	public String getFormulario() {
		return formulario;
	}
}
