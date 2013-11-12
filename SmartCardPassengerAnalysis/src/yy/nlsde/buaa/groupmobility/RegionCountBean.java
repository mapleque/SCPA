package yy.nlsde.buaa.groupmobility;

import java.util.ArrayList;
import java.util.List;

public class RegionCountBean {
	private List<PointBean> points=null;
	private int count;

	public RegionCountBean(String line){
		String[] data=line.split(",");
		this.count=Integer.parseInt(data[0]);
		int vn=Integer.parseInt(data[1]);
		this.points=new ArrayList<PointBean>();
		for (int i=0;i<vn;i++){
			String[] lonlat=data[i+2].split("-");
			this.points.add(new PointBean(Double.parseDouble(lonlat[0]),Double.parseDouble(lonlat[1])));
		}
	}
	
	public int getCount(){
		return this.count;
	}
}
