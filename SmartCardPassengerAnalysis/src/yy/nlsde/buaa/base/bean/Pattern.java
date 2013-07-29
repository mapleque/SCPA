package yy.nlsde.buaa.base.bean;

import yy.nlsde.buaa.patternlibrary.CardBean;

public class Pattern {
	public final static String SPLIT_CHAR = ",";
	
	protected static final int TIME = 0;
	protected static final int LON = 1;
	protected static final int LAT = 2;
	protected static final int W = 3;
	protected static final int[] PATTERN = { TIME, LON, LAT, W };

	protected double time;// 转化为分
	protected double lon;
	protected double lat;
	protected int weight;

	protected boolean avilable = true;
	

	public Pattern(double time, double lon, double lat, int weight) {
		this.time = time;
		this.lon = lon;
		this.lat = lat;
		this.weight = weight;
	}

	protected Pattern(CardBean card) {
		try {
			String timestr = card.getUpTime(CardBean.MIN);
			if (timestr == null || timestr.length() != 4) {
				this.avilable = false;
			}
			this.time = Double.valueOf(timestr.substring(0, 2)) * 60
					+ Double.valueOf(timestr.substring(2, 4));
			this.lon = Double.valueOf(card.getUpLon());
			this.lat = Double.valueOf(card.getUpLat());
			this.weight = 1;
		} catch (Exception e) {
			this.avilable = false;
		}
	}

	protected Pattern(String pbstr) {
		try {
			if (pbstr == null || pbstr.length() <= 0) {
				this.avilable = false;
			}
			String[] pbl = pbstr.split(Pattern.SPLIT_CHAR);
			if (pbl.length != Pattern.PATTERN.length ) {
				this.avilable = false;
			}
			this.time=Double.valueOf(pbl[Pattern.TIME]);
			this.lon=Double.valueOf(pbl[Pattern.LON]);
			this.lat=Double.valueOf(pbl[Pattern.LAT]);
			this.weight=Integer.valueOf(pbl[Pattern.W]);
		} catch (Exception e) {
			this.avilable = false;
		}
	}

	public boolean isAvilable() {
		return this.avilable;
	}

	public String toString() {
		return this.time+Pattern.SPLIT_CHAR+
				this.lon+Pattern.SPLIT_CHAR+
				this.lat+Pattern.SPLIT_CHAR+
				this.weight;
	}

	public static Pattern getPattern(CardBean card) {
		return new Pattern(card);
	}

	public static Pattern getPattern(String pbstr) {
		return new Pattern(pbstr);
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
