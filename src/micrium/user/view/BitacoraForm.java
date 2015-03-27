package micrium.user.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.business.BitacoraBL;
import micrium.user.model.Bitacora;

@Named
@javax.enterprise.context.RequestScoped
public class BitacoraForm {

	@Inject
	BitacoraBL logBl;

	private List<Bitacora> listBitacora;

	@PostConstruct
	public void init() {

		try {
			listBitacora = logBl.listBitacora();
		} catch (Exception e) {
		}

	}

	public List<Bitacora> getListBitacora() {
		return listBitacora;
	}

	public void setListBitacora(List<Bitacora> listBitacora) {
		this.listBitacora = listBitacora;
	}


	
	
}
