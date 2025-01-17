package model.dto;

public class ShopMenuDto {
	private int meno;
	private String mename;
	private int meprice;
	private int cno;
	private int eno;
	private String ename;

	public ShopMenuDto(int meno, String mename, int meprice, int eno, String ename) {
		this.meno = meno;
		this.mename = mename;
		this.meprice = meprice;
		this.eno = eno;
		this.ename = ename;
	}

	public int getMeno() {
		return meno;
	}

	public void setMeno(int meno) {
		this.meno = meno;
	}	
	
	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getMename() {
		return mename;
	}

	public void setMename(String mename) {
		this.mename = mename;
	}

	public int getMeprice() {
		return meprice;
	}

	public void setMeprice(int meprice) {
		this.meprice = meprice;
	}

	public int getCno() {
		return cno;
	}

	public void setCno(int cno) {
		this.cno = cno;
	}

	public int getEno() {
		return eno;
	}

	public void setEno(int eno) {
		this.eno = eno;
	}

	@Override
	public String toString() {
		return "ShopMenuDto [meno=" + meno + ", mename=" + mename + ", meprice=" + meprice + ", cno=" + cno + ", eno="
				+ eno + "]";
	}
}
