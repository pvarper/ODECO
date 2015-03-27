package micrium.user.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.business.RoleBL;
import micrium.user.model.Rol;
import micrium.user.model.RolFormulario;
import micrium.util.Utils;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@Named
@javax.enterprise.context.RequestScoped
public class RolFormularioForm {

	@Inject
	private RoleBL roleBL;

	private String idRol;
	private String nameRole;

	private TreeNode root;
	private TreeNode[] selectedNodes;

	public static Logger log = Logger.getLogger(RolFormularioForm.class);

	@PostConstruct
	public void init() {

		String Idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("roleId");

		try {
			idRol = Idstr;
			int id = Integer.parseInt(Idstr);

			Rol role = roleBL.getRole(id);
			nameRole = role.getNombre();

			cargar(id);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void cargar(int id) {

		Map<String, TreeNode> mapTreeNode = new HashMap<String, TreeNode>();
		root = new DefaultTreeNode("Root", null);
		List<RolFormulario> lista = roleBL.getRolFormulario(id);
		for (RolFormulario rolFor : lista) {

			String str = rolFor.getFormulario().getNivel();
			if (str != null)
				if (!str.isEmpty()) {
					int k = str.lastIndexOf(".");
					if (k == -1) {
						TreeNode node0 = new DefaultTreeNode(rolFor.getFormulario().getNombre(), root);
						node0.setSelected(rolFor.getEstado());
						mapTreeNode.put(str, node0);
					} else {
						String path = str.substring(0, k);
						TreeNode nodeF = mapTreeNode.get(path);
						TreeNode node0 = new DefaultTreeNode(rolFor.getFormulario().getNombre(), nodeF);
						mapTreeNode.put(str, node0);
						node0.setSelected(rolFor.getEstado());
						nodeF.setSelected(false);
						nodeF.setExpanded(true);
					}
				}
		}
	}

	public String saveRolFormulario() {

		int id = Integer.parseInt(idRol);

		if (id == 1) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// "No puede modificarse es rol Administrativo"));
			Utils.showError("No puede modificarse es Rol Administrativo.");
			return "";
		}
		List<String> lis = new ArrayList<String>();
		for (TreeNode treeNode : selectedNodes) {
			lis.add(treeNode.getData().toString());
		}
		log.info("[saveRolFormulario] lista de selecciones:" + lis + " " + "id Role:" + id);
		roleBL.updateRoleFormularioList(lis, id);
		return "/view/Role.xhtml";
	}

	public String getIdRol() {

		return idRol;
	}

	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}

	public String getNameRole() {
		return nameRole;
	}

	public void setNameRole(String nameRole) {
		this.nameRole = nameRole;
	}

	public TreeNode getRoot() {
		return root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}
}
