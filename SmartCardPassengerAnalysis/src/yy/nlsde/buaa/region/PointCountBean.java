package yy.nlsde.buaa.region;

public class PointCountBean {
	
	public final static int UP=1;
	public final static int DOWN=2;

	private double lon;
	private double lat;
	private String time;
	private int ud;//up or down
	private int count;
	
	public PointCountBean(String line) {
		String[] sub=line.split(",");
		this.lon=Double.parseDouble(sub[0]);
		this.lat=Double.parseDouble(sub[1]);
		this.time=sub[2];
		this.ud=Integer.parseInt(sub[3]);
		this.count=Integer.parseInt(sub[4]);
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getUd() {
		return ud;
	}

	public void setUd(int ud) {
		this.ud = ud;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
