package micrium.user.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;









import micrium.odeco.business.CiudadBL;
import micrium.odeco.business.ColaCoordinadorBL;
import micrium.odeco.business.FlujoBL;
import micrium.odeco.business.MotivoReclamoBL;
import micrium.odeco.model.Ciudad;
import micrium.odeco.model.ColaCoordinador;
import micrium.odeco.model.Flujo;
import micrium.odeco.model.MotivoReclamo;
import micrium.user.business.RoleBL;
import micrium.user.business.UsuarioBL;
import micrium.user.controler.ControlLoginImpl;
import micrium.user.model.Rol;
import micrium.user.model.Usuario;
import micrium.util.ActiveDirectory;
import micrium.util.DescriptorBitacora;
import micrium.util.Parameter;
import micrium.util.Utils;

import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class RoleUserForm {

	
	@Inject
	private UsuarioBL userBL;
	
	@Inject
	private FlujoBL flujoBL;

	@Inject
	private RoleBL rolBL;

	@Inject
	private UsuarioBL usuarioBL;
	
	@Inject
	private CiudadBL ciudadBL;
	
	@Inject
	private ControlerBitacora controlerBitacora;
	
	@Inject
	private ColaCoordinadorBL colaCoordinadorBL;
	
	@Inject
	private MotivoReclamoBL motivoReclamoBL;

	@Inject
	private ControlLoginImpl controler;

	private List<Usuario> listUser;

	
	private Usuario user = new Usuario();
	private String userId;
	private Boolean edit;

	private List<SelectItem> selectItems;
	private List<SelectItem> selectItemsResponsables;
	private List<SelectItem> selectItemsCiudad;
	
	private Date fecha;
	private String select;
	private String selectResponsable;
	private String selectCiudad;
	private Boolean booleanRender;
	private Boolean booleanRenderEspecial;
	private Boolean especial;

	private static Logger log = Logger.getLogger(RoleUserForm.class);

	@PostConstruct
	public void init() {

		try {
			user = new Usuario();
			listUser = userBL.getUsers();
			this.setFecha(new Date());
			fillSelectItems();
			newUser();
		} catch (Exception e) {
			log.error("init|Fallo al inicializar la clase. " + e.getMessage());
		}
	}

	private void fillSelectItems() throws Exception {

	
		selectItems = new ArrayList<SelectItem>();
		selectItems.add(new SelectItem("-1", "Grupos_Rol"));
		List<Rol> listaRol = rolBL.getRoles();
		for (Rol role : listaRol) {
			SelectItem sel = new SelectItem(role.getRolId(), role.getNombre());
			selectItems.add(sel);
		}
		selectItemsResponsables = new ArrayList<SelectItem>();
		selectItemsResponsables.add(new SelectItem("-1", "Responsables"));
		List<Usuario> listaUsuario = usuarioBL.getUsersNoadmin();
		for (Usuario usuario : listaUsuario) {
			SelectItem sel = new SelectItem(usuario.getUsuarioId(), usuario.getNombre());
			selectItemsResponsables.add(sel);
		}
		
		selectItemsCiudad = new ArrayList<SelectItem>();
		selectItemsCiudad.add(new SelectItem("-1", "Ciudades"));
		List<Ciudad> listaCiudad = ciudadBL.getList();
		for (Ciudad ciudad : listaCiudad) {
			SelectItem sel = new SelectItem(ciudad.getCiudadId(), ciudad.getNombre());
			selectItemsCiudad.add(sel);
		}		
	}

	public void RenderResponsable(String s) throws Exception{
		if(s.equalsIgnoreCase("1")){
			booleanRender=false;
			booleanRenderEspecial=true;
			selectItemsResponsables = new ArrayList<SelectItem>();
			selectItemsResponsables.add(new SelectItem("-1", "Responsables"));
			return;
		}
		String a=flujoBL.getFlujo(1).getAgente();
		if(a!=null){
			String b=rolBL.getname(a).getRolId().toString();
			if(s.equalsIgnoreCase(b)){
				booleanRender=true;
				booleanRenderEspecial=true;
				selectItemsResponsables = new ArrayList<SelectItem>();
				selectItemsResponsables.add(new SelectItem("-1", "Responsables"));
				List<Usuario> listaUsuario = usuarioBL.getUsersRol(rolBL.getname(flujoBL.getFlujo(1).getStaff()).getRolId().toString());
				for (Usuario usuario : listaUsuario) {
					SelectItem sel = new SelectItem(usuario.getUsuarioId(), usuario.getNombre());
					selectItemsResponsables.add(sel);
				}
				return;
			}
			
		}
		
		a=flujoBL.getFlujo(1).getStaff();
		if(a!=null){
			String b=rolBL.getname(a).getRolId().toString();
			if(s.equalsIgnoreCase(b)){
				booleanRender=true;
				booleanRenderEspecial=true;
				selectItemsResponsables = new ArrayList<SelectItem>();
				selectItemsResponsables.add(new SelectItem("-1", "Responsables"));
				List<Usuario> listaUsuario = usuarioBL.getUsersRol(rolBL.getname(flujoBL.getFlujo(1).getCoordinador()).getRolId().toString());
				for (Usuario usuario : listaUsuario) {
					if(edit){
						if(!usuario.getUsuarioId().equals(Integer.valueOf(userId))){
							SelectItem sel = new SelectItem(usuario.getUsuarioId(), usuario.getNombre());
							selectItemsResponsables.add(sel);
						}
					}else{
						SelectItem sel = new SelectItem(usuario.getUsuarioId(), usuario.getNombre());
						selectItemsResponsables.add(sel);
					}														
				}
				return;
			}
			
		}
		
		a=flujoBL.getFlujo(1).getCoordinador();
		if(a!=null){
			String c=rolBL.getname(a).getRolId().toString();
			if(s.equalsIgnoreCase(c)){
				booleanRender=false;
				booleanRenderEspecial=false;
				selectItemsResponsables = new ArrayList<SelectItem>();
				selectItemsResponsables.add(new SelectItem("-1", "Responsables"));
			/*	List<Usuario> listaUsuario = usuarioBL.getUsersRol("5");
				for (Usuario usuario : listaUsuario) {
					SelectItem sel = new SelectItem(usuario.getUsuarioId(), usuario.getNombre());
					selectItemsResponsables.add(sel);
				}*/
				return;
			}
			
		}
		a=flujoBL.getFlujo(1).getRegulacion();
		if(a!=null){
			String r=rolBL.getname(a).getRolId().toString();
			if(s.equalsIgnoreCase(r)){
				booleanRender=false;
				booleanRenderEspecial=true;
				selectItemsResponsables = new ArrayList<SelectItem>();
				selectItemsResponsables.add(new SelectItem("-1", "Responsables"));
				return;
			}
			
			
		}
	}
	

	
	public String saveUser() {
		
		Usuario name = ActiveDirectory.obtenerNombreCompletoActiveDirectory(user.getLogin());

		log.debug(" nameAD=" + name);
		if (name!=null){
			user.setNombre(name.getNombre());
			user.setCorreo(name.getCorreo());
			user.setEspecial(especial);
		}
		
		String str = "";
		try {
			str = userBL.validate(user, userId);
		} catch (Exception e) {
			str = "error con la conexion a la BD o otro problema";
		}

		if (!str.isEmpty()) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// str));
			Utils.showError(str);
			return "";
		}
		
		int idCiudad = Integer.parseInt(selectCiudad);
		if (idCiudad == -1) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// "seleccione un role"));
			Utils.showError("Seleccione una Ciudad");
			select="-1";
			booleanRenderEspecial=true;
			return "";
		}
		int idRole = Integer.parseInt(select);
		Rol rol=rolBL.getRole(idRole);
		int idResponsable=-2;
		if(rol==null){
			Utils.showError("Seleccione un Rol.");
			select="-1";
			return "";
		}
		if(!rol.getNombre().equalsIgnoreCase(rolBL.getname(flujoBL.getFlujo(1).getRegulacion()).getNombre())&&!rol.getNombre().equalsIgnoreCase("Administracion")&&!rol.getNombre().equalsIgnoreCase(rolBL.getname(flujoBL.getFlujo(1).getCoordinador()).getNombre())){			
			if(selectResponsable!=null){
				 idResponsable = Integer.parseInt(selectResponsable);
			}
		}
		if (idRole == -1) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// "seleccione un role"));
			Utils.showError("Seleccione un Rol.");
			select="-1";
			return "";
		}
		if (idResponsable == -1) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// "seleccione un role"));
			Utils.showError("Seleccione un Responsable");
			select="-1";
			return "";
		}
	
		/*if (user.getTelefono().equals("")) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// "seleccione un role"));
			Utils.showError("Inserte un Telefono valido");
			return "";
		}*/

	
		// System.out.println("sw: " + Parameters.swActive.equals("true"));

		// List groups = Parameters.swActive.equals("true") ?
		// ActiveDirectory.getGroups(user.getLogin()) :
		// controler.getListaGrupo();

	
		try {
			if (!edit) {
				List<?> groups = ActiveDirectory.getGroups(user.getLogin());

				if (groups == null) {
					// FacesContext.getCurrentInstance().addMessage(null, new
					// FacesMessage(FacesMessage.SEVERITY_ERROR,
					// "Mensaje de error", "este usuario no existe en AD."));
					Utils.showError("Este usuario no existe en Active Directory.");
					return "";
				}

				if (groups.size() <= 0) {
					// FacesContext.getCurrentInstance().addMessage(null, new
					// FacesMessage(FacesMessage.SEVERITY_ERROR,
					// "Mensaje de error",
					// "este usuario no tiene grupo en AD."));
					Utils.showError("Este usuario no tiene grupo en Active Directory.");
					return "";
				}
				
				userBL.saveUserRole(user, idRole,idCiudad,idResponsable);
				Flujo flujo = flujoBL.getFlujoNombre("flujo");
				if(idRole==rolBL.getname(flujo.getCoordinador()).getRolId()&& !user.getEspecial()){
					ColaCoordinador co= new ColaCoordinador();
					co.setColaCoordinadorId(null);
					co.setNombreCoordinador(user.getLogin());
					co.setCantidad(0);
					colaCoordinadorBL.save(co);					
				}
				fillSelectItems();
				controlerBitacora.insert(DescriptorBitacora.USUARIO, user.getUsuarioId() + "", user.getLogin());
			} else {
				int id = Integer.parseInt(userId);

				List<?> groups = null;
				if (id == 1) {
					groups = controler.getListaGrupo();
				} else {
					groups = ActiveDirectory.getGroups(user.getLogin());
				}

				if (groups == null) {
					// FacesContext.getCurrentInstance().addMessage(null, new
					// FacesMessage(FacesMessage.SEVERITY_ERROR,
					// "Mensaje de error", "este usuario no existe en AD."));
					Utils.showError("Este usuario no existe en Active Directory.");
					return "";
				}

				if (groups.size() <= 0) {
					// FacesContext.getCurrentInstance().addMessage(null, new
					// FacesMessage(FacesMessage.SEVERITY_ERROR,
					// "Mensaje de error",
					// "este usuario no tiene grupo en AD."));
					Utils.showError("Este usuario no tiene grupo en Active Directory.");
					return "";
				}

				System.out.println("groups size: " + groups.size());

				if (groups.get(0).toString().equals("Administracion") && idRole != 1) {
					// FacesContext.getCurrentInstance().addMessage(null, new
					// FacesMessage(FacesMessage.SEVERITY_ERROR,
					// "Mensaje de error",
					// "este usuario no puede cambiar de Rol"));
					Utils.showError("Este usuario no puede cambiar de Rol.");
					return "";
				}
				
				List<Usuario> listaUs=userBL.getListResponsables(userId);
				if(!listaUs.isEmpty()){
					Utils.showError("Este Usuario no puede editarse por que tiene usuarios asociados");
					return "";
				}
				Flujo flujo = flujoBL.getFlujoNombre("flujo");
				if(idRole!=rolBL.getname(flujo.getCoordinador()).getRolId()){
					List<MotivoReclamo> lista=motivoReclamoBL.getListCoordinador(user.getLogin());
					if(!lista.isEmpty()){
						Utils.showError("Este Coordinador tiene Motivos de Reclamos Asociados, por favor revisar");
						return "";
					}
				}
				
				
				if(user.getRol().getRolId().equals(rolBL.getname(flujo.getCoordinador()).getRolId())){
					if(idRole!=rolBL.getname(flujo.getCoordinador()).getRolId()||idRole==rolBL.getname(flujo.getCoordinador()).getRolId()&&user.getEspecial()){					
							ColaCoordinador coo=colaCoordinadorBL.find(user.getLogin());
							if(coo!=null){
								colaCoordinadorBL.remove(coo);	
							}											
						
					}
					
				}
				if(idRole==rolBL.getname(flujo.getCoordinador()).getRolId()){
					if(idRole==rolBL.getname(flujo.getCoordinador()).getRolId()&& !user.getEspecial()){
						ColaCoordinador co= new ColaCoordinador();
						co.setColaCoordinadorId(null);
						co.setNombreCoordinador(user.getLogin());
						co.setCantidad(0);
						if(colaCoordinadorBL.find(user.getLogin())==null){
							colaCoordinadorBL.save(co);	
						}
						
					}
					
				}
				user.setUsuarioId(id);
				userBL.updateUser(user, idRole,idCiudad,idResponsable,flujo);				
				fillSelectItems();
				controlerBitacora.update(DescriptorBitacora.USUARIO, user.getUsuarioId() + "", user.getLogin());
			}
			listUser = userBL.getUsers();
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_INFO, "Información",
			// "Se guardo correctamente"));
			Utils.showInfo("Se guardo correctamente.");
			newUser();
		} catch (Exception e) {
			log.error("[saveUser] error al momento de modificar o guardar: " + user.getNombre() + " " + e);
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
			// "error al guardar en la BD"));
			Utils.showError("Fallo al guardar en la Base de Datos.");
			e.printStackTrace();
		}
		return "";
	}

	// public String saveUser() {
	// int idRole = Integer.parseInt(select);
	// if (idRole == -1) {
	// FacesContext.getCurrentInstance().addMessage(null, new
	// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
	// "seleccione un role"));
	// return "";
	// }
	//
	// String str = "";
	// try {
	// str = userBL.validate(user, userId);
	// } catch (Exception e) {
	// str = "error con la conexion a la BD o otro problema";
	//
	// }
	//
	// if (!str.isEmpty()) {
	// FacesContext.getCurrentInstance().addMessage(null, new
	// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error", str));
	// return "";
	// }
	// try {
	// if (!edit) {
	// userBL.saveUserRole(user, idRole);
	// controlerBitacora.insert(DescriptorBitacora.USUARIO, user.getUsuarioId()
	// + "", user.getLogin());
	// } else {
	// int id = Integer.parseInt(userId);
	//
	// if (id == 1) {
	// FacesContext.getCurrentInstance().addMessage(null, new
	// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
	// "Este Usuario no se puede modificar es usuario Interno"));
	// return "";
	// }
	//
	// user.setUsuarioId(id);
	// userBL.updateUser(user, idRole);
	// controlerBitacora.update(DescriptorBitacora.USUARIO, user.getUsuarioId()
	// + "", user.getLogin());
	// }
	// listUser = userBL.getUsers();
	// FacesContext.getCurrentInstance().addMessage(null, new
	// FacesMessage(FacesMessage.SEVERITY_INFO, "Información",
	// "Se guardo correctamente"));
	// newUser();
	// } catch (Exception e) {
	// log.error("[saveUser] error al momento de modificar o guardar: " +
	// user.getNombre() + " " + e);
	// FacesContext.getCurrentInstance().addMessage(null, new
	// FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
	// "error al guardar en la BD"));
	//
	// }
	// return "";
	// }

	public void editRoleUser() {
		String Idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("user_id");
		int id = Integer.parseInt(Idstr);
		userId = Idstr;
		edit = true;
		
		try {
			user = userBL.getUser(id);
			select = user.getRol().getRolId() + "";
			especial=user.getEspecial();
			selectItemsResponsables = new ArrayList<SelectItem>();
			RenderResponsable(user.getRol().getRolId().toString());
			selectResponsable = user.getUsuario().getUsuarioId() + "";
			selectCiudad = user.getCiudad().getCiudadId() + "";			
	
		} catch (Exception e) {
			user = new Usuario();
			log.error("[editRoleUser] error al obtener el usuario:" + e);
		}
	}

	public String deleteRoleUser() {
		String Idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("user_id");
		int id = Integer.parseInt(Idstr);

		if (id == 1) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// "Este Usuario no se puede elimnar es usuario Interno"));
			Utils.showError("Este Usuario no se puede elimnar es usuario Interno.");
			return "";
		}

		try {
			user = userBL.getUser(id);
			List<Usuario> listaUs=userBL.getListResponsables(Idstr);
			if(!listaUs.isEmpty()){
				Utils.showError("Este Usuario no puede eliminarse por que tiene usuarios asociados");
				return "";
			}
			List<MotivoReclamo> lista=motivoReclamoBL.getListCoordinador(user.getLogin());
			if(!lista.isEmpty()){
				Utils.showError("Este Coordinador no puede eliminarse tiene Motivos de Reclamos Asociados, por favor revisar");
				return "";
			}
			userBL.deleteUser(id);
			Flujo flujo = flujoBL.getFlujoNombre("flujo");
			if(user.getRol().getRolId().equals(rolBL.getname(flujo.getCoordinador()).getRolId())){
				ColaCoordinador coo=colaCoordinadorBL.find(user.getLogin());
				if(coo==null){
					log.error("[delete] no se pudo obtener el usuario :"+user.getLogin());
					return "";
					
				}
				colaCoordinadorBL.remove(coo);
			}
			controlerBitacora.delete(DescriptorBitacora.USUARIO, user.getUsuarioId() + "", user.getLogin());
			listUser = userBL.getUsers();
			newUser();
			fillSelectItems();
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_INFO, "Información",
			// "Se elimino correctamente"));
			Utils.showInfo("Se elimino correctamente.");
		} catch (Exception e) {
			log.error("[deleteRoleUser]  error al eliminar el menu id:" + Idstr + "  " + e);
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Notificación",
			// "Error al eliminar"));
			Utils.showError("Fallo al eliminar.");
		}
		return "";
	}

	public void newUser() {
		edit = false;
		booleanRender=true;
		booleanRenderEspecial=true;
		especial=false;
		user = new Usuario();
		select = "-1";
		selectResponsable="-1";
		selectCiudad="-1";
	}
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public List<SelectItem> getSelectItems() {
		return selectItems;
	}

	public void setSelectItems(List<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public List<Usuario> getListUser() {
		return listUser;
	}

	public void setListUser(List<Usuario> listUser) {
		this.listUser = listUser;
	}

	public String getSelectResponsable() {
		return selectResponsable;
	}

	public void setSelectResponsable(String selectResponsable) {
		this.selectResponsable = selectResponsable;
	}

	public List<SelectItem> getSelectItemsResponsables() {
		return selectItemsResponsables;
	}

	public void setSelectItemsResponsables(List<SelectItem> selectItemsResponsables) {
		this.selectItemsResponsables = selectItemsResponsables;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<SelectItem> getSelectItemsCiudad() {
		return selectItemsCiudad;
	}

	public void setSelectItemsCiudad(List<SelectItem> selectItemsCiudad) {
		this.selectItemsCiudad = selectItemsCiudad;
	}

	public String getSelectCiudad() {
		return selectCiudad;
	}

	public void setSelectCiudad(String selectCiudad) {
		this.selectCiudad = selectCiudad;
	}

	public Boolean getBooleanRender() {
		return booleanRender;
	}

	public void setBooleanRender(Boolean booleanRender) {
		this.booleanRender = booleanRender;
	}

	public Boolean getBooleanRenderEspecial() {
		return booleanRenderEspecial;
	}

	public void setBooleanRenderEspecial(Boolean booleanRenderEspecial) {
		this.booleanRenderEspecial = booleanRenderEspecial;
	}

	public Boolean getEspecial() {
		return especial;
	}

	public void setEspecial(Boolean especial) {
		this.especial = especial;
	}

	public String getExpresionRegular() {
		return Parameter.expresionRegular;
	}

	public String getExpresionRegularNumero() {
		return Parameter.expresionRegularNumero;
	}


	
	
}
