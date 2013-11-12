package yy.nlsde.buaa.region;

import java.util.ArrayList;
import java.util.List;

public class RegionCountBean extends RegionBean{
	public List<PointBean> stations;

	private int count=0;
	
	public int getCount() {
		return count;
	}
	
	public void addCount(int count){
		this.count+=count;
	}
	
	public String toString(){
		if (this.points==null)
			buildEdge();
		int nv=this.points.size();
		String re=this.count+","+nv;
		for (PointBean p:this.points){
			re+=","+p.toString();
		}
		return re;
	}
	
	public String getRegionEdgeString(){
		String re="";
		boolean first=true;
		for (PointBean p:this.points){
			if (first){
				first=false;
			}else{
				re+=",";
			}
			re+=p.toAreaString();
		}
		return re;
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
