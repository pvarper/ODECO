/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package micrium.user.controler;

import java.util.Date;

public class NodoIngresoUser {
	private String user;
	private Date date;
	private int count = 0;

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
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public void increment() {
		count++;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	public long getTime() {
		return date.getTime();
	}
}
