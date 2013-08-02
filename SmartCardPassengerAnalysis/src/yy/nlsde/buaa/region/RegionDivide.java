package yy.nlsde.buaa.region;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yy.nlsde.buaa.base.constant.OutToFile;

public class RegionDivide {
	
	private final static String OUT_PATH="regionData";
	private static String sub_path="tmp";

	public void generalTheRegion(String date){
		sub_path=date;
		PointCountReadIn in=new PointCountReadIn();
		in.setDate(date);
		HashMap<String,List<PointCountBean>> map=new HashMap<String,List<PointCountBean>>();
		PointCountBean pcb=null;
		while ((pcb=in.getPointCountBean())!=null){
			String key=pcb.getTime();
			if (!map.containsKey(key)){
				map.put(key, new ArrayList<PointCountBean>());
			}
			map.get(key).add(pcb);
		}
		for (String time:map.keySet()){
			this.generalTheRegion(time,map.get(time));
		}
	}
	
	private void generalTheRegion(String time,List<PointCountBean> list){
		List<RegionBean> result=new ArrayList<RegionBean>();
		for (PointCountBean pcb:list){
			//TODO:DBSCAN
			this.merge(pcb);
		}
		this.outToFile(result, time);
	}
	
	private void merge(PointCountBean pcb){
		//TODO:
	}
	
	private void outToFile(List<RegionBean> list,String filename){
		OutToFile.outToFile(list, OUT_PATH+File.separator+sub_path+File.separator+filename+".csv");
	}
	
	
}
