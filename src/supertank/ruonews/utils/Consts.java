package supertank.ruonews.utils;


public class Consts {

	public class IntentExtra{
		public static final String POSITION = "position";
		public static final String NEWS_GENERAL_LIST = "news_general_list";
		public static final String CATEGORY = "category";
		public static final String NEWS_ID = "news_id";
	}
	public class net{
		//public static final String BASE = "http://192.168.1.5/ci-rest/index.php/api";
		public static final String BASE = "http://www.southsource.host-ed.me/index.php/api";
		public static final String FORMAT = "/format/json";
		public static final String CATEGORY = BASE + "/category/index" + FORMAT;
		public static final String DIGEST = BASE + "/news/digest" + FORMAT;
		public static final String DETAIL = BASE + "/news/detail" + FORMAT;
		public static final String COMMENT = BASE + "/comment/list" + FORMAT;
		public static final String SUPPORT = BASE + "/comment/support" + FORMAT;
		public static final String OPPOSE = BASE + "/comment/oppose" + FORMAT;
		public static final String POST = BASE + "/comment/post" + FORMAT;
	}
	
	public class retcode{
		public static final int CATEGORY_SUCCESS = 1000;
		public static final int CATEGORY_FAILURE = 1001;
		
		public static final int DIGEST_SUCCESS = 1010;
		public static final int DIGEST_NO_NEW = 1011;
		public static final int DIGEST_PARAM_FAULT = 1012;
		
		public static final int DETAIL_SUCCESS = 1020;
		public static final int DETAIL_ERR = 1021;
		
		public static final int COMMENT_SUCCESS = 1030;
		public static final int NOT_COMMENT = 1031;
		public static final int COMMENT_PARAM_FAULT = 1032;
		
		public static final int SUPPORT_SUCCESS = 1040;
		public static final int NOT_COMMENT_TOSUPPORT = 1041;
		public static final int SUPPORT_PARAM_FAULT = 1042;
		
		public static final int OPPOSE_SUCCESS = 1050;
		public static final int NOT_COMMENT_TO_OPPOSE = 1051;
		public static final int OPPOSE_PARAM_FAULT = 1052;
		
		public static final int POST_SUCCESS = 1060;
		public static final int POST_FAILURE = 1061;
		
	}
}
