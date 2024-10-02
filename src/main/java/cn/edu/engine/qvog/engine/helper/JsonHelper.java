package cn.edu.engine.qvog.engine.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides helper methods for JSON objects.
 */
public class JsonHelper {
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    private JsonHelper() {}

    public static JSONObject tryGetObject(JSONObject json, String key) {
        Object value = json.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        System.out.println("something error in extracting: " + value);
        return null;
    }

    public static JSONObject getObject(JSONObject json, String key) {
        Object value = json.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing object at " + key + ": " + json);
        }
        return (JSONObject) value;
    }

    public static JSONObject tryGetObject(JSONArray json, int index) {
        Object value = json.get(index);
        if (value == null) {
            return null;
        }
        return (JSONObject) value;
    }

    public static JSONObject getObject(JSONArray json, int index) {
        Object value = json.get(index);
        if (value == null) {
            throw new IllegalArgumentException("Missing object at " + index + ": " + json);
        }
        return (JSONObject) value;
    }

    public static JSONArray tryGetArray(JSONObject json, String key) {
        Object value = json.get(key);
        if (value == null) {
            return null;
        }
        return (JSONArray) value;
    }

    public static JSONArray getArray(JSONObject json, String key) {
        Object value = json.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing array at " + key + ": " + json);
        }
        return (JSONArray) value;
    }

    public static JSONArray tryGetArray(JSONArray json, int index) {
        Object value = json.get(index);
        if (value == null) {
            return null;
        }
        return (JSONArray) value;
    }

    public static JSONArray getArray(JSONArray json, int index) {
        Object value = json.get(index);
        if (value == null) {
            throw new IllegalArgumentException("Missing array at " + index + ": " + json);
        }
        return (JSONArray) value;
    }

    public static List<JSONObject> getObjectElements(JSONArray array) {
        List<JSONObject> elements = new ArrayList<>();
        for (Object element : array) {
            if (!(element instanceof JSONObject)) {
                throw new IllegalArgumentException("Not an array of JSONObject: " + array);
            }
            elements.add((JSONObject) element);
        }
        return elements;
    }

    public static List<JSONArray> getArrayElements(JSONArray array) {
        List<JSONArray> elements = new ArrayList<>();
        for (Object element : array) {
            if (!(element instanceof JSONArray)) {
                throw new IllegalArgumentException("Not an array of JSONArray: " + array);
            }
            elements.add((JSONArray) element);
        }
        return elements;
    }

    public static List<String> getStringElements(JSONArray array) {
        List<String> elements = new ArrayList<>();
        for (Object element : array) {
            if (!(element instanceof String)) {
                throw new IllegalArgumentException("Not an array of String: " + array);
            }
            elements.add((String) element);
        }
        return elements;
    }

    public static Object tryGet(JSONObject json, String key) {
        return json.get(key);
    }

    public static Object get(JSONObject json, String key) {
        Object value = json.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing value at " + key + ": " + json);
        }
        return value;
    }

    public static String tryGetValue(JSONObject json, String key) {
        Object value = json.get(key);
        if (value == null) {
            return null;
        }
        return (String) value;
    }

    public static String getValue(JSONObject json, String key) {
        Object value = json.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing value at " + key + ": " + json);
        }
        return (String) value;
    }

    public static int tryGetIntValue(JSONObject json, String key, int defaultValue) {
        Object value = json.get(key);
        if (value == null) {
            return defaultValue;
        }
        return ((Long) value).intValue();
    }

    public static int getIntValue(JSONObject json, String key) {
        Object value = json.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing value at " + key + ": " + json);
        }
        return ((Long) value).intValue();
    }

    public static boolean tryGetBoolValue(JSONObject json, String key, boolean defaultValue) {
        Object value = json.get(key);
        if (value == null) {
            return defaultValue;
        }
        return (boolean) value;
    }

    public static boolean getBoolValue(JSONObject json, String key) {
        Object value = json.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing value at " + key + ": " + json);
        }
        return (boolean) value;
    }

    public static String tryGetValue(JSONArray json, int index) {
        Object value = json.get(index);
        if (value == null) {
            return null;
        }
        return (String) value;
    }

    public static String getValue(JSONArray json, int index) {
        Object value = json.get(index);
        if (value == null) {
            throw new IllegalArgumentException("Missing value at " + index + ": " + json);
        }
        return (String) value;
    }

    public static String dumps(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize object: " + object, e);
        }
    }

    public static <T> T loads(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to deserialize JSON: " + json, e);
        }
    }

    public static JSONObject load(String path) {
        try {
            String json = Files.readString(Paths.get(path));
            return (JSONObject) new JSONParser().parse(json);
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Failed to read JSON file: " + path, e);
        }
    }
}
