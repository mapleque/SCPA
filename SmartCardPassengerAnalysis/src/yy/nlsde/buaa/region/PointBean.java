package yy.nlsde.buaa.region;

public class PointBean {
	protected double lon;
	protected double lat;
	
	public PointBean(){}
	
	public PointBean(double lon,double lat){
		this.lon=lon;
		this.lat=lat;
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

}
