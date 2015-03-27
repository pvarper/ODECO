package micrium.odeco.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import micrium.odeco.model.Parametro;
import micrium.user.controler.ControlParametro;
import micrium.user.view.ControlerBitacora;
import micrium.util.DescriptorBitacora;
import micrium.util.Parameter;
import micrium.util.Utils;

import org.apache.log4j.Logger;



@ManagedBean
@ViewScoped
public class ParametrosBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ParametrosBean.class);

	@Inject
	private ControlParametro cparametros;
	@Inject
	private ControlerBitacora controlerBitacora;

	private List<Parametro> listaParametros;
	private List<Parametro> menu;

	//@Inject
	//private Parameter parameters;

	@PostConstruct
	public void init() {
		try {
			listaParametros = cparametros.getParametros();
		//	log.info("Cantidad de datos: "+listaParametros.size());
			/*for (Parametro p : listaParametros) {
				log.debug("Parametro: " + p.getDescripcion());
			}*/
			menu = new ArrayList<Parametro>();
			for (Parametro p : listaParametros) {
				if (p.getTipo()) {
					menu.add(p);
					//log.debug("Menu: " + p.getDescripcion());
				}
			}
		} catch (Exception e) {
			log.error("No se recuper� la lista de parametros",e);
			log.debug(e);
		}
	}

	public List<Parametro> getListaParametros() {
		return listaParametros;
	}

	public void setListaParametros(List<Parametro> listaParametros) {
		this.listaParametros = listaParametros;
	}

	public List<Parametro> getParametrosDeMenu(Parametro menux) {
		List<Parametro> lista = new ArrayList<Parametro>();
		try {
			if (menux == null){
				log.debug("NULL PARAMETRO");
				return lista;
			}
			//log.debug("MENUX: " + menux.getDescripcion());

			
			for (Parametro p : listaParametros) {
			//	log.debug("PARAM: " + p.getDescripcion());
				if (p.getIdformulario()!=null ) {
					if( menux.getId().equals(p.getIdformulario())){
						lista.add(p);
					}
					
				}
			}
			//log.debug("Retornando lista tama�o: " + lista.size());
			return lista;

		} catch (Exception e) {
			//log.debug(e);
			return lista;
		}
	}
																																																																																																																																																																																								
	public void guardarParametro(Parametro p) throws Exception {
		//bitacora.registrarAccion("Se modific� el valor del par�metro '"
		//		+ p.getDescripcion() + "' a '" + p.getValor() + "'");
		p.setValor(p.getValor().trim());
		cparametros.updateParametro(p);
		controlerBitacora.update(
				DescriptorBitacora.PARAMETROS,
				String.valueOf(p.getId()),
				p.getDescripcion());
		Utils.showInfo("Datos guardamos exitosamente...");
		//parameters.init();
	}

	public List<Parametro> getMenu() {
		return menu;
	}

	public void setMenu(List<Parametro> menu) {
		this.menu = menu;
	}

	public List<SelectItem> getOptions(String opt) {
		ArrayList<SelectItem> opciones = new ArrayList<SelectItem>();
		String[] elements = opt.split(";");
		for (String ele : elements) {
			opciones.add(new SelectItem(ele, ele));
		}
		return opciones;
	}
	public String getExpresionRegular(){
		return Parameter.expresionRegular;
	}
	
	public String getExpresionRegularNumero(){
		
		return Parameter.expresionRegularNumero;
	}
}
