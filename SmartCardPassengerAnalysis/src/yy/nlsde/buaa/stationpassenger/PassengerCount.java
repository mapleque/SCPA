package yy.nlsde.buaa.stationpassenger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yy.nlsde.buaa.base.constant.OutToFile;

public class PassengerCount {
	public static void main(String[] args){
		PassengerCount pc = new PassengerCount();
		pc.count("20120709");
	}
	
	private final static String OUT_PATH="stationPassenger";

	private HashMap<String, PointCountBean> result;

	public PassengerCount() {
		result = new HashMap<String, PointCountBean>();
	}

	public void count(String date) {
		String[] dates = { date };
		CardReadIn in = new CardReadIn();
		in.setCardDate(dates);

		int num = 0;
		CardBean card;
		while ((card = in.getCardInfo()) != null) {
			num++;
			if (num % 100000 == 0)
				System.out.println(num + ":" + System.currentTimeMillis());
			if (card.isAFCUp() || !card.isAvilable()) {
				continue;
			}
			this.add(card);
		}
		List<PointCountBean> list=new ArrayList<PointCountBean>();
		for (String key:result.keySet()){
			list.add(result.get(key));
		}
		outTofile(list,OUT_PATH+File.separator+date+".csv");
	}

	private void add(CardBean card) {
		PointCountBean pcbUp=new PointCountBean(card,PointCountBean.UP);
		String key1 = pcbUp.getKey();
		if (!result.containsKey(key1)){
			result.put(key1, pcbUp);
		}
		result.get(key1).addOne();
		
		PointCountBean pcbDown=new PointCountBean(card,PointCountBean.DOWN);
		String key2 = pcbDown.getKey();
		if (!result.containsKey(key2)){
			result.put(key2, pcbDown);
		}
		result.get(key2).addOne();
	}
	
	public static <T> void outTofile(List<T> list, String filename) {
		// output the result to file
		OutToFile.outToFile(list, filename);
	}
}
