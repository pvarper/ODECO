package micrium.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import micrium.aes.AlgoritmoAES;
import micrium.user.model.Usuario;

import org.apache.log4j.Logger;

public class ActiveDirectory {
	public static int EXIT_USER = 1;
	public static int NOT_EXIT_USER = 2;
	public static int ERROR = 3;

	private static Logger log = Logger.getLogger(ActiveDirectory.class);
	private static AlgoritmoAES aes = new AlgoritmoAES();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int existUser(String user, String password) {

		if (password == null || password.isEmpty()) {
			return NOT_EXIT_USER;
		}
		Hashtable env = new Hashtable();

		try {
			env.put(Context.INITIAL_CONTEXT_FACTORY, Parameter.activeInitialContext);
			env.put(Context.PROVIDER_URL, Parameter.activeProviderUrl);
			env.put(Context.SECURITY_AUTHENTICATION, Parameter.activeSecurityAuthentication);
			env.put(Context.SECURITY_PRINCIPAL, Parameter.activeSecurityPrincipal + user);
			env.put(Context.SECURITY_CREDENTIALS, password);
		} catch (Exception e) {
			log.error("[existUser] Error al verificar la conectividad con el direcctorio activo" + "Directory: " + e.getMessage());// */
			return ERROR;
		}

		try {
			// Create initial context
			InitialDirContext ctx = new InitialDirContext(env);
			// Close the context when we're done
			ctx.close();
			log.debug("EXISTE USUARIO EXISTE USUARIO EXISTE USUARIO");
			return EXIT_USER;
		} catch (NamingException ex) {
			// *
			log.error("[existUser] Error al autenticar en el servidor de Active " + "Directory: " + ex.getMessage());// */
			return NOT_EXIT_USER;
		} catch (Exception e) {
			log.error("[existUser] Error al autenticar en el servidor de Active " + "Directory: " + e.getMessage());// */
			return NOT_EXIT_USER;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static ArrayList getGroups(String user) {

		ArrayList list = new ArrayList();

		Hashtable env = new Hashtable();

		env.put("java.naming.factory.initial", Parameter.activeInitialContext);
		env.put("java.naming.provider.url", Parameter.activeProviderUrl);
		env.put("java.naming.security.authentication", Parameter.activeSecurityAuthentication);
		env.put("java.naming.security.principal", Parameter.activeSecurityPrincipal + aes.desencriptar(Parameter.activeSecurityUser));
		env.put("java.naming.security.credentials", aes.desencriptar(Parameter.activeSecurityCredentials));

		InitialLdapContext ctx = null;
		try {
			ctx = new InitialLdapContext(env, null);
			String searchBase = "DC=tigo,DC=net,DC=bo";
			SearchControls searchCtls = new SearchControls();
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String returnAtts[] = { "memberOf" };
			String searchFilter = "(&(objectCategory=person)(objectClass=user)(mailNickname=" + user + "))";

			searchCtls.setReturningAttributes(returnAtts);

			NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);
			int totalResults = 0;

			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				Attributes attrs = sr.getAttributes();
				if (attrs != null) {
					for (NamingEnumeration ne = attrs.getAll(); ne.hasMore();) {
						Attribute attr = (Attribute) ne.next();
						String grupo;
						for (NamingEnumeration e = attr.getAll(); e.hasMore(); totalResults++) {
							grupo = e.next().toString().trim();
							grupo = grupo.substring(3, grupo.indexOf(",")).trim();
							list.add(grupo);
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("[getGroups] Error al obtener el listado de grupos: " + e.getMessage());
		} finally {
			try {
				ctx.close();
			} catch (Exception e2) {
				log.warn("[getGroups] Fallo al cerrar el InitialLdapContext. " + e2.getMessage());
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public static Usuario obtenerNombreCompletoActiveDirectory(String usuario) {
		Logger log = Logger.getLogger(ActiveDirectory.class);
		log.info("[obtenerNombreCompletoActiveDirectory]Iniciando recuperacion de Nombre para loginAD=" + usuario);

		Conexion conexion = new Conexion();
		conexion.setDominio(Parameter.activeSecurityDominio);
		conexion.setUrl(Parameter.activeProviderUrl);
		conexion.setUsuario(aes.desencriptar(Parameter.activeSecurityUser));
		conexion.setClave(aes.desencriptar(Parameter.activeSecurityCredentials));
		// System.out.println( "Configurando parametros de conexiï¿½n al LDAP..."
		// );
		Ldap ld = new Ldap(conexion);
		try {
			String[] returnAtts = { "cn", "sAMAccountName", "mail", "sn", "givenName", "initials" };
			// System.out.println( "Consultando los datos al LDAP..." );
			Map mapa = ld.obtenerDatos(usuario, returnAtts);
			// System.out.println( "Respuesta del LDAP: " );
			if (mapa.isEmpty()) {
				// System.out.println(
				// "No se encontro datos en el Active Directory del usuario: " +
				// usuario );
				return null;
			}
			// System.out.println( "Mail : " + (String) mapa.get("mail") );
			// System.out.println( "Nombre : " + (String)
			// mapa.get("givenName"));
			// System.out.println( "Login : " + (String)
			// mapa.get("sAMAccountName"));
			// System.out.println( "Ehumano : " + (String)
			// mapa.get("initials"));
			// System.out.println( "Apellido : " + (String) mapa.get("sn"));
			Usuario u=new Usuario();
			u.setNombre(mapa.get("givenName") + " " + (String) mapa.get("sn"));
			u.setCorreo((String)mapa.get("mail"));
			return u;

		} catch (Exception e) {
			// e.printStackTrace();
			log.error("[obtenerNombreCompletoActiveDirectory]Al intentar recuperar nombres para loginAD" + usuario);
			// System.out.println( e.getMessage() );
		}
		return null;
	}

	// ------------------------------------------------------------------------------
	@SuppressWarnings("rawtypes")
	public static boolean validarGrupo(String grupo) throws NamingException, Exception {
		InitialDirContext dirC = null;
		NamingEnumeration answer = null;
		NamingEnumeration ae = null;

		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put("java.naming.factory.initial", Parameter.activeInitialContext);
			env.put("java.naming.provider.url", Parameter.activeProviderUrl);
			env.put("java.naming.security.authentication", Parameter.activeSecurityAuthentication);
			env.put("java.naming.security.principal", Parameter.activeSecurityPrincipal + aes.desencriptar(Parameter.activeSecurityUser));
			env.put("java.naming.security.credentials", aes.desencriptar(Parameter.activeSecurityCredentials));

			dirC = new InitialDirContext(env);
			if (dirC != null) {
				String searchBase = "DC=tigo,DC=net,DC=bo";
				SearchControls searchCtls = new SearchControls();
				searchCtls.setSearchScope(2);
				String searchFilter = "(objectclass=group)";
				String[] returnAtts = { "cn" };
				searchCtls.setReturningAttributes(returnAtts);
				answer = dirC.search(searchBase, searchFilter, searchCtls);

				while (answer.hasMoreElements()) {
					SearchResult sr = (SearchResult) answer.next();
					Attributes attrs = sr.getAttributes();
					if (attrs != null) {
						for (ae = attrs.getAll(); ae.hasMore();) {
							Attribute attr = (Attribute) ae.next();
							// System.out.println("//"+attr.get().toString());
							if (attr.get().toString().equals(grupo)) {
								return true;
							}
						}

					}

				}

			}

		} catch (NamingException ne) {
		} catch (Exception e) {
		} finally {
			try {
				if (dirC != null)
					dirC.close();
			} catch (Exception e) {
			}

		}

		return false;
	}

}