package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonUtils {

    private static final String os = System.getProperty("os.name");

    public static <E> List<E> getVertices(
            List<JsonElement> jsonElements, Class<E> clazz, Object typeAdapter) {
        List<E> vertices = new ArrayList<>();
        for (JsonElement element : jsonElements) {
            Gson gson = new GsonBuilder().registerTypeAdapter(clazz, typeAdapter).create();
            vertices.add(gson.fromJson(element.getAsJsonObject().toString(), clazz));
        }
        return vertices;
    }

    public static Set<JsonElement> setOfElements(JsonArray arr) {
        Set<JsonElement> set = new HashSet<>();
        arr.iterator().forEachRemaining(set::add);
        return set;
    }

    public static Set<JsonElement> setOfElements2(JsonArray arr) {
        Set<JsonElement> set = new HashSet<>();
        for (JsonElement j : arr) {
            String path = j.getAsJsonObject().get("path").toString();
            if (!os.equals("Linux") && !path.isEmpty()) {
                j.getAsJsonObject().addProperty("path", path.substring(0, path.length() - 1));
            }
            set.add(j);
        }
        return set;
    }
}
