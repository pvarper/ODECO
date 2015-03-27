/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package micrium.user.controler;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author marciano
 */
public class NodoClient {
	private String user;
	private String addressIp;
	private long time;
	private Map<String, Boolean> mapListaUrl = new HashMap<String, Boolean>();

	public void addUrl(String url, boolean sw) {
		mapListaUrl.put(url, sw);
	}

	public boolean existeUrl(String url) {
		return mapListaUrl.get(url);
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the addressIp
	 */
	public String getAddressIp() {
		return addressIp;
	}

	/**
	 * @param addressIp
	 *            the addressIp to set
	 */
	public void setAddressIp(String addressIp) {
		this.addressIp = addressIp;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

}
