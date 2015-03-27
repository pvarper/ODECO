/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package micrium.user.controler;

public class NodoMenu {

	private String pather;
	private String name;
	private String url;
	private boolean swRendered;
	private int tipo;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the swRendered
	 */
	public boolean isSwRendered() {
		return swRendered;
	}

	/**
	 * @param swRendered
	 *            the swRendered to set
	 */
	public void setSwRendered(boolean swRendered) {
		this.swRendered = swRendered;
	}

	/**
	 * @return the pather
	 */
	public String getPather() {
		return pather;
	}

	/**
	 * @param pather
	 *            the pather to set
	 */
	public void setPather(String pather) {
		this.pather = pather;
	}

}
