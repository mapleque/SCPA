package yy.nlsde.buaa.region;

import java.util.ArrayList;
import java.util.List;

public class RegionCountBean extends RegionBean{
	private List<PointBean> stations;

	private int count=0;
	
	public int getCount() {
		return count;
	}
	
	public void addCount(int count){
		this.count+=count;
	}
	
	public String toString(){
		buildEdge();
		//TODO:
		return null;
	}
	
	public void addStation(PointBean p){
		if (stations==null)
			stations=new ArrayList<PointBean>();
		stations.add(p);
	}
	
	private void buildEdge(){
		this.points=RegionUtil.Points2RegionEdge(this.stations);
	}
}
