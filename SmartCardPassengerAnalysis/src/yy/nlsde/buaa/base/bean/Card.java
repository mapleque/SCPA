package yy.nlsde.buaa.base.bean;

public class Card {
	public final static int AFC=1;
	public final static int BUS=2;
	
	protected String line;
	protected String cardID;
	protected String lineID;

	protected String uplineID;
	protected String upSeq;
	protected String upLon;
	protected String upLat;
	protected String upTime;

	protected String downlineID;
	protected String downSeq;
	protected String downLon;
	protected String downLat;
	protected String downTime;
	
	protected boolean avilable=true;
	
	public boolean isAvilable(){
		return this.avilable;
	}

	public String getCardID() {
		return this.cardID;
	}
	
	public String getUpLon() {
		return this.upLon;
	}

	public String getUpLat() {
		return this.upLat;
	}

	public String getDownLon() {
		return this.downLon;
	}

	public String getDownLat() {
		return this.downLat;
	}
}
