package micrium.odeco.model;

import java.io.Serializable;
import java.math.BigDecimal;




public class ObjetoFila implements Serializable {
	private static final long serialVersionUID = 1L;

	private String mes;
	private int vector[];
	private BigDecimal col0=new BigDecimal(1);
	private BigDecimal col1=new BigDecimal(1);
	private BigDecimal col2=new BigDecimal(1);
	private BigDecimal col3=new BigDecimal(1);
	private BigDecimal col4=new BigDecimal(1);
	private BigDecimal col5=new BigDecimal(1);
	private BigDecimal col6=new BigDecimal(1);
	private BigDecimal col7=new BigDecimal(1);
	private BigDecimal col8=new BigDecimal(1);
	
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public int[] getVector() {
		return vector;
	}
	public void setVector(int[] vector) {
		this.vector = vector;
	}
	public BigDecimal getCol1() {
		return col1;
	}
	public void setCol1(BigDecimal col1) {
		this.col1 = col1;
	}
	public BigDecimal getCol2() {
		return col2;
	}
	public void setCol2(BigDecimal col2) {
		this.col2 = col2;
	}
	public BigDecimal getCol3() {
		return col3;
	}
	public void setCol3(BigDecimal col3) {
		this.col3 = col3;
	}
	public BigDecimal getCol4() {
		return col4;
	}
	public void setCol4(BigDecimal col4) {
		this.col4 = col4;
	}
	public BigDecimal getCol5() {
		return col5;
	}
	public void setCol5(BigDecimal col5) {
		this.col5 = col5;
	}
	public BigDecimal getCol6() {
		return col6;
	}
	public void setCol6(BigDecimal col6) {
		this.col6 = col6;
	}
	public BigDecimal getCol7() {
		return col7;
	}
	public void setCol7(BigDecimal col7) {
		this.col7 = col7;
	}
	public BigDecimal getCol8() {
		return col8;
	}
	public void setCol8(BigDecimal col8) {
		this.col8 = col8;
	}
	public BigDecimal getCol0() {
		return col0;
	}
	public void setCol0(BigDecimal col0) {
		this.col0 = col0;
	}


	
	

	
	
}