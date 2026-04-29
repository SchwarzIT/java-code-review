package schwarz.jobs.interview.coupon.util;

import schwarz.jobs.interview.coupon.configuration.JsonMapperConfig;
import tools.jackson.databind.json.JsonMapper;

public class JsonUtils {

    private static final JsonMapper jsonMapper = new JsonMapperConfig().jsonMapper();

    public static String toJson(final Object obj) {
        return jsonMapper.writeValueAsString(obj);
    }
}
