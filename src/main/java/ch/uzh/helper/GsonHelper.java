package ch.uzh.helper;

import com.google.gson.Gson;

/**
 * Created by Jesus on 09.07.2017.
 */
public class GsonHelper {

    public static String createJsonString(Object o){
        Gson gsonFrame = new Gson();
        String jsonFrame = gsonFrame.toJson(o);
        return jsonFrame;
    }

}
