package yy.nlsde.buaa.estimate.down;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import yy.nlsde.buaa.base.constant.OutToFile;

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
		Evaluation.outTofile(
				rl,
				"downEvaluateResult" + File.separator
						+ System.currentTimeMillis() + ".csv");
		System.out.println("start time:" + System.currentTimeMillis());
		System.out.println("end time:" + System.currentTimeMillis());
	}

	public static <T> void outTofile(List<T> list, String filename) {
		// output the result to file
		OutToFile.outToFile(list, filename);
	}

	private final int NO_PATTERN = 1;
	private final int MISS_PATTERN = 2;
	private final int WRONG_PATTERN = 3;
	private final int CORRECT_PATTERN = 4;
	private final int CORRECT_DOWN = 5;
	private final int WRONG_DOWN = 6;
	private final int TOTAL_NUM = 0;

	private int[] result;

	private IPatternService ips;

	private static final int DST = 1000;// 相同空间判定阈值，m
	private static final int TST = 90;// 乘车时间范围阈值，m

	public Evaluation() {
		ips = ServiceFatory.getPatternService();
		result = new int[7];
		result[NO_PATTERN] = 0;
		result[MISS_PATTERN] = 0;
		result[WRONG_PATTERN] = 0;
		result[CORRECT_PATTERN] = 0;
		result[CORRECT_DOWN] = 0;
		result[WRONG_DOWN] = 0;
		result[TOTAL_NUM] = 0;
	}

	public String getResult() {
		return result[TOTAL_NUM] + "," + result[CORRECT_DOWN] + ","
				+ result[WRONG_DOWN] + "," + result[CORRECT_PATTERN] + ","
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
			if (isCorrectPattern(card, cp)) {
				result[CORRECT_PATTERN]++;
			} else {
				result[WRONG_PATTERN]++;
			}
			if (isCorrectDown(card, cp, pl)) {
				result[CORRECT_DOWN]++;
			} else {
				result[WRONG_DOWN]++;
			}
		}
	}

	private PatternBean getCorrectPattern(List<PatternBean> pl, CardBean card) {
		if (pl.size() <= 0) {
			result[NO_PATTERN]++;
			return null;
		}
		// get the most probably pattern
		List<PatternBean> tl = new ArrayList<PatternBean>();
		PatternBean cpb = new PatternBean(card);
		for (PatternBean pb : pl) {
			if (inTimeScale(pb, cpb)) {
				// add to the property list
				tl.add(pb);
			}
		}
		if (tl.size() <= 0) {
			result[MISS_PATTERN]++;
			return null;
		}
		PatternBean rs = null;
		for (PatternBean pb : tl) {
			// TODO:bus card in line filter
			if (rs == null) {
				rs = pb;
			} else {
				if (rs.weight < pb.weight) {
					rs = pb;
				}
			}
		}
		return rs;
	}

	private boolean isCorrectPattern(CardBean card, PatternBean cp) {
		if (inSpaceScale(card, cp))
			return true;
		return false;
	}

	private boolean isCorrectDown(CardBean card, PatternBean cp,
			List<PatternBean> pl) {
		Collections.sort(pl, new Comparator<PatternBean>() {
			@Override
			public int compare(PatternBean o1, PatternBean o2) {
				return (int) (o1.getTime() - o2.getTime());
			}
		});
		PatternBean cpb = new PatternBean(card);
		boolean flag = false;
		int stweight = 0;
		List<PatternBean> tpl = new ArrayList<PatternBean>();
		for (PatternBean tp : pl) {
			if (inTimeScale(cpb, tp)) {
				flag = true;
			}
			if (flag) {
				if (inSpaceScale(cpb, tp)) {
					stweight = tp.getWeight();
				} else {
					tpl.add(tp);
				}
			}
		}
		// get the correct down pattern
		PatternBean corPb = null;
		for (PatternBean tp : tpl) {
			if (tp.getWeight() > stweight / 2) {
				corPb = tp;
			}
		}
		// the last down need to find the morning up 
		if (corPb == null) {
			for (PatternBean tp : pl) {
				if (tp.getWeight() > stweight / 2) {
					corPb = tp;
				}
			}
		}
		// low down the filter
		if (corPb == null && tpl.size() > 0) {
			corPb = tpl.get(0);
		}
		if (corPb == null && pl.size() > 0) {
			corPb = pl.get(0);
		}
		// no pattern can find
		if (corPb == null) {
			return false;
		}
		return inSpaceScale(card, corPb);
	}

	private boolean inTimeScale(PatternBean pb1, PatternBean pb2) {
		if (time(pb1.getTime(), pb2.getTime()) < TST) {
			return true;
		}
		return false;
	}

	private boolean inSpaceScale(PatternBean pb1, PatternBean pb2) {
		if (distence(pb1.getLon(), pb1.getLat(), pb2.getLon(), pb2.getLat()) < DST) {
			return true;
		}
		return false;
	}

	// down space
	private boolean inSpaceScale(CardBean card, PatternBean pb) {
		if (distence(Double.valueOf(card.getDownLon()),
				Double.valueOf(card.getDownLat()), pb.getLon(), pb.getLat()) < DST) {
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
