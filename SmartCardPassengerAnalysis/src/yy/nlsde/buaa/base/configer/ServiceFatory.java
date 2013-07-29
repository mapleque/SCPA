package yy.nlsde.buaa.base.configer;

import yy.nlsde.buaa.base.service.IPatternService;
import yy.nlsde.buaa.base.service.Pattern2DB;

public class ServiceFatory {

	public static IPatternService getPatternService(){
		return new Pattern2DB();
	}
}
