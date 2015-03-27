package micrium.odeco.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ListaInputStream {

	
	private List<InputStream> listaInputStream ;
		
	
	public ListaInputStream() {
		this.listaInputStream = new ArrayList<InputStream>();
	}

	public void add(InputStream a){
		this.listaInputStream.add(a);		
	}
	
	public List<InputStream> get(){
		return listaInputStream;
	}
	
	
}
