package ynu.util;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class ControllerUtils {

    private ControllerUtils() {}

    /**
     * 获取一个简单的响应状态
     *
     * @param isSuccess 是否操作成功
     *
     * @return 响应JSON字符串
     */
    public static String getResponse(boolean isSuccess) {
        JSONObject jsonObject = new JSONObject();
        if (isSuccess) {
            jsonObject.put("status", "success");
        } else {
            jsonObject.put("status", "error");
            jsonObject.put("message","异常！");
        }
        return jsonObject.toString();
    }

    public static String listToJson(List<?> list) {
        JSONArray array = new JSONArray();
        if (list!=null) {
            array.addAll(list);
        }
        return formatJson(array.toString());
    }
    
    public static String formatJson(String string) {
        String json;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(string);
        json = gson.toJson(je);
        return json;
    }
}

