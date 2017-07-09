package ch.uzh.helper;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Jesus on 09.07.2017.
 */
public class GsonHelper {

    private static final Logger log = LoggerFactory.getLogger(GsonHelper.class);


    public static String createJsonString(Object o){
        Gson gsonFrame = new Gson();
        String jsonFrame = gsonFrame.toJson(o);
        log.info("jsonFrame: " + jsonFrame);
        return jsonFrame;
    }

}
