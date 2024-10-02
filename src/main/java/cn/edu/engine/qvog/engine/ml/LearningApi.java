package cn.edu.engine.qvog.engine.ml;

import cn.edu.engine.qvog.engine.helper.RestApi;
import cn.edu.engine.qvog.engine.helper.Tuple;

import java.util.ArrayList;
import java.util.List;

public class LearningApi implements ILearningApi {
    private final RestApi api;

    LearningApi(String host, int port) {
        api = new RestApi(host, port);
    }

    LearningApi(String baseUrl) {
        api = new RestApi(baseUrl);
    }

    @Override
    public List<Tuple<String, PredictTypes>> predict(List<String> values) {
        int maxLength = 150;

        List<RequestPayload> payload = new ArrayList<>();
        for (String value : values) {
            RequestPayload p = new RequestPayload();
            p.setCode(value.length() > maxLength ? value.substring(0, maxLength) : value);
            p.setData(new PlaceHolder());
            payload.add(p);
        }

        PredicateResponse response = api.post("judge_type", payload, PredicateResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed to predict types: " + response.status);
        }
        List<Tuple<String, PredictTypes>> result = new ArrayList<>();
        int i = 0;
        for (Integer data : response.getData()) {
            result.add(new Tuple<>(values.get(i), getPredictType(data)));
            i++;
        }

        return result;
    }

    @Override
    public boolean match(String source, String sink) {
        List<RequestPayload> payload = List.of(
                new RequestPayload(source),
                new RequestPayload(sink));

        MatchResponse response = api.post("judge_pair", payload, MatchResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed to predict types: " + response.status);
        }

        return response.getData() == 1;
    }

    private PredictTypes getPredictType(int type) {
        return switch (type) {
            case 0 -> PredictTypes.Source;
            case 1 -> PredictTypes.Sink;
            default -> PredictTypes.None;
        };
    }

    private static class RequestPayload {
        private String code;
        private PlaceHolder data;

        public RequestPayload() {
        }

        public RequestPayload(String code) {
            this(code, new PlaceHolder());
        }

        public RequestPayload(String code, PlaceHolder data) {
            this.code = code;
            this.data = data;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public PlaceHolder getData() {
            return data;
        }

        public void setData(PlaceHolder data) {
            this.data = data;
        }
    }

    private static class PredicateResponse {
        private int status;
        private List<Integer> data;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<Integer> getData() {
            return data;
        }

        public void setData(List<Integer> data) {
            this.data = data;
        }
    }

    private static class MatchResponse {
        private int status;
        private int data;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }
    }

    private static class PlaceHolder {}
}
