package yy.nlsde.buaa.estimate.down;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Evaluation {
	public static void main(String[] args) {
		System.out.println("start time:" + System.currentTimeMillis());
		String[] dates = { "20120709", "20120710", "20120711", "20120712",
				"20120713", "20120716", "20120717", "20120718", "20120719",
				"20120723", "20120724", "20120725", "20120726", "20120727" };
		List<String> rl = new ArrayList<String>();
		for (String date : dates) {
			Evaluation e = new Evaluation();
			e.evaluate(date);
			rl.add(e.getResult());
		}
		Evaluation.outTofile(rl,"downEvaluateResult"+File.separator+System.currentTimeMillis()+".csv");
		System.out.println("start time:" + System.currentTimeMillis());
		System.out.println("end time:" + System.currentTimeMillis());
	}
	
	public static <T> void outTofile(List<T> list,String filename){
		//output the result to file
		OutToFile.outToFile(list, filename);
	}

	private final int NO_PATTERN = 1;
	private final int MISS_PATTERN = 2;
	private final int WRONG_PATTERN = 3;
	private final int CORRECT_PATTERN = 4;
	private final int TOTAL_NUM = 0;

	private int[] result;

	private IPatternService ips;

	private static final int DST = 1000;// 相同空间判定阈值，m
	private static final int TST = 120;// 乘车时间范围阈值，m

	public Evaluation() {
		ips = ServiceFatory.getPatternService();
		result = new int[5];
		result[NO_PATTERN] = 0;
		result[MISS_PATTERN] = 0;
		result[WRONG_PATTERN] = 0;
		result[CORRECT_PATTERN] = 0;
		result[TOTAL_NUM] = 0;
	}

	public String getResult() {
		return result[TOTAL_NUM] + "," + result[CORRECT_PATTERN] + ","
				+ result[WRONG_PATTERN] + "," + result[MISS_PATTERN] + ","
				+ result[NO_PATTERN];
	}

	public void evaluate(String date) {
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
			this.evaluate(card);
		}
	}

	public void evaluate(CardBean card) {
		result[TOTAL_NUM]++;
		PassengerPattern pp = ips.getPattern(card.getCardID());
		List<PatternBean> pl = pp.getPatternList();
		PatternBean cp = getCorrectPattern(pl, card);
		if (cp != null) {
			if(isCorrect(card, cp)){
				result[CORRECT_PATTERN]++;
			}else{
				result[WRONG_PATTERN]++;
			}
		}
	}

	private PatternBean getCorrectPattern(List<PatternBean> pl,CardBean card){
		if (pl.size()<=0){
			result[NO_PATTERN]++;
			return null;
		}
		//get the most probably pattern
		List<PatternBean> tl=new ArrayList<PatternBean>();
		PatternBean cpb=new PatternBean(card);
		for (PatternBean pb:pl){
			if (inTimeScale(pb,cpb)){
				//add to the property list
				tl.add(pb);
			}
		}
		if (tl.size()<=0){
			result[MISS_PATTERN]++;
			return null;
		}
		PatternBean rs=null;
		for (PatternBean pb:tl){
			//TODO:bus card in line filter
			if (rs==null){
				rs=pb;
			}else{
				if (rs.weight<pb.weight){
					rs=pb;
				}
			}
		}
		return rs;
	}

	private boolean isCorrect(CardBean card, PatternBean cp) {
		if (inSpaceScale(card,cp))
			return true;
		return false;
	}

	private boolean inTimeScale(PatternBean pb1, PatternBean pb2) {
		if (time(pb1.getTime(), pb2.getTime()) < TST) {
			return true;
		}
		return false;
	}

	private boolean inSpaceScale(CardBean card, PatternBean pb) {
		if (distence(Double.valueOf(card.getDownLon()), Double.valueOf(card.getDownLat()), pb.getLon(), pb.getLat()) < DST) {
			return true;
		}
		return false;
	}

	private double distence(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(
				111000 * Math.abs(x1 - x2)
						* Math.cos((y1 + y2) / 2 * Math.PI / 180), 2)
				+ Math.pow(111000 * Math.abs(y1 - y2), 2));
	}

	private double time(double t1, double t2) {
		return Math.abs(t1 - t2);
	}
}
