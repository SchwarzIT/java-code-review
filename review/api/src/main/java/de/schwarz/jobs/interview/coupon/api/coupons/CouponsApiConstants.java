package de.schwarz.jobs.interview.coupon.api.coupons;

public class CouponsApiConstants {

  public static final String PATH_API_V1_ROOT = "/api/v1";
  public static final String PATH_COUPONS = "/coupons";
  public static final String PATH_PARAM_CODE = "code";
  public static final String PATH_PARAM_CODE_DOC = "The code of the coupon.";
  public static final String PATH_PARAM_CODES_DOC = "The codes of the coupons.";
  public static final String PATH_PARAM_BASKET_ID = "basketid";
  public static final String PATH_PARAM_BASKET_ID_DOC = "The ID of the basket.";
  public static final String PATH_CODE = "/{" + PATH_PARAM_CODE + "}";
  public static final String PATH_COUPONS_BY_CODE = PATH_COUPONS + PATH_CODE;
  public static final String PATH_BASKETS = "/baskets";
  public static final String PATH_BASKET_ID = "/{" + PATH_PARAM_BASKET_ID + "}";
  public static final String PATH_BASKETS_BY_ID = PATH_BASKETS + PATH_BASKET_ID;
  public static final String PATH_BASKETS_COUPON_DISCOUNT = PATH_BASKETS_BY_ID + PATH_COUPONS_BY_CODE;
}
