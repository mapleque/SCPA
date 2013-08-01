package yy.nlsde.buaa.stationpassenger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import yy.nlsde.buaa.base.constant.OutToFile;

public class PassengerCount {
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
			if (num % 10000 == 0)
				System.out.println(num + ":" + System.currentTimeMillis());
			if (card.isAFCUp() || !card.isAvilable()) {
				continue;
			}
			this.add(card);
		}
		outTofile(result,OUT_PATH+File.separator+date+".csv");
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
	
	public static <K, V> void outTofile(Map<K, V> list, String filename) {
		// output the result to file
		OutToFile.outToFile(list, filename);
	}
}
