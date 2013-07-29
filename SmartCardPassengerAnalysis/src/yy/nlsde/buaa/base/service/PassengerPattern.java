package yy.nlsde.buaa.base.service;

import java.util.ArrayList;
import java.util.List;

import yy.nlsde.buaa.base.bean.Pattern;

public class PassengerPattern {

	private final static String SPLIT_CHAR = "#";

	private String pid;
	private String pattern;
	
	private boolean isNew;
	
	public PassengerPattern(String id){
		this.pid=id;
		this.pattern="";
		this.isNew=true;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.isNew=false;
		this.pattern = pattern;
	}

	public List<Pattern> getPatternList() {
		String pbsstr = this.pattern;
		if (pbsstr == null || pbsstr.length() <= 0) {
			return null;
		}
		String[] pbl = pbsstr.split(PassengerPattern.SPLIT_CHAR);
		if (pbl.length <= 0) {
			return null;
		}
		List<Pattern> rl = new ArrayList<Pattern>();
		for (String pbs : pbl) {
			Pattern pb = Pattern.getPattern(pbs);
			if (pb.isAvilable()) {
				rl.add(pb);
			}
		}
		return rl;
	}

	public void updatePattern(List<Pattern> pbs) {
		if (pbs == null || pbs.size() <= 0) {
			return;
		}
		this.pattern = "";
		for (Pattern pb : pbs) {
			this.pattern += PassengerPattern.SPLIT_CHAR;
			this.pattern += pb;
		}
		if (this.pattern.length() >= PassengerPattern.SPLIT_CHAR.length()) {
			this.pattern = this.pattern.substring(PassengerPattern.SPLIT_CHAR
					.length());
		}
	}

	public boolean isNew() {
		return isNew;
	}

}
