package micrium.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class Ldap {

	public static final String USER_NOMBRE = "givenName";
	public static final String USER_APELLIDO = "sn";
	public static final String USER_EMAIL = "mail";
	public static final String USER_LOGIN = "sAMAccountName";
	public static final String USER_NOMBRE_COMPLETO = "cn";
	public static final String USER_COD_EHUMANO = "initials";
	Hashtable<String, String> env;
	LdapContext ctx;
	SearchControls searchCtls;
	String searchFilter;
	String searchBase;
	private Conexion conexion;

	public Ldap(Conexion conexion) {
		this.conexion = conexion;
		Iniciar();
	}

	public void Iniciar() {
		this.env = new Hashtable<String, String>();
		this.env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");

		this.env.put("java.naming.security.authentication", "simple");
		this.env.put("java.naming.security.principal", this.conexion.getDominio() + "\\" + this.conexion.getUsuario());
		this.env.put("java.naming.security.credentials", this.conexion.getClave());

		this.env.put("java.naming.provider.url", this.conexion.getUrl());
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String> obtenerDatos(String usuarioE, String[] datosReturn) throws NamingException, Exception {
		Map<String, String> mapa = new HashMap<String, String>();
		try {
			this.ctx = new InitialLdapContext(this.env, null);

			this.searchBase = "DC=tigo,DC=net,DC=bo";

			this.searchCtls = new SearchControls();

			this.searchCtls.setSearchScope(2);

			this.searchFilter = ("(&(objectCategory=person)(objectClass=user)(mailNickname=" + usuarioE + "))");

			this.searchCtls.setReturningAttributes(datosReturn);

			NamingEnumeration answer = this.ctx.search(this.searchBase, this.searchFilter, this.searchCtls);
			NamingEnumeration ae;
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				Attributes attrs = sr.getAttributes();
				if (attrs != null) {
					for (ae = attrs.getAll(); ae.hasMore();) {
						Attribute attr = (Attribute) ae.next();
						String llave = attr.getID();
						mapa.put(llave, attr.get().toString());
						System.out.println("Llave: " + llave + " Valor: " + attr.get().toString());
					}

				}

			}

			return mapa;
		} catch (NamingException ne) {
		} catch (Exception e) {
		} finally {
			this.ctx.close();
		}
		return mapa;
	}

}