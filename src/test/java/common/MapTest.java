package common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MapTest {
    @Test
    public void modifyTest() {
        Map<String, Data> map = new HashMap<>();
        var data = new Data(1);
        map.put("key", data);
        var newData = map.values().iterator().next();
        newData.setValue(2);

        Assertions.assertSame(data, newData);
        Assertions.assertEquals(2, map.get("key").getValue());
    }

    private static class Data {
        public int value;

        public Data(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
