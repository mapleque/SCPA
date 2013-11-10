package yy.nlsde.buaa.region;

import java.util.ArrayList;
import java.util.List;

public class RegionBean {
	public List<PointBean> points=null;

	public void addPoint(double lon, double lat) {
		if (points==null)
			points=new ArrayList<PointBean>();
		points.add(new PointBean(lon,lat));
	}

}
