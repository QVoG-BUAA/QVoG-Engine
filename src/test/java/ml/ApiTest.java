package ml;

import cn.edu.buaa.qvog.engine.helper.JsonHelper;
import cn.edu.buaa.qvog.engine.ml.ILearningApi;
import cn.edu.buaa.qvog.engine.ml.LearningApiBuilder;
import cn.edu.buaa.qvog.engine.ml.PredictTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ApiTest {
    private static ILearningApi api;

    @BeforeAll
    public static void beforeAll() {
        var config = JsonHelper.getObject(JsonHelper.load("config.json"), "learning");
        api = LearningApiBuilder.connect(config);
    }

    @Test
    public void basicTest() {
        String source = "def _accept(prefix):";
        String sink = "return prefix[:6] == b'SIMPLE'";

        var result = api.predict(List.of(source, sink));
        Assertions.assertSame(result.get(0).getSecond(), PredictTypes.Source);
        Assertions.assertSame(result.get(1).getSecond(), PredictTypes.Sink);

        boolean match = api.match(source, sink);

        Assertions.assertTrue(match);
    }

    @Test
    public void longCodeTest() {
        var code = """
                def download_rc_file(request):
                    file_path = os.path.join(app.config['UPLOAD_FOLDER'], request.args.get('file'))
                    if not os.path.exists(file_path):
                        return 'File not found', 404
                    content_length = os.path.getsize(file_path)
                    return send_file(file_path, as_attachment=True, attachment_filename=request.args.get('file'), cache_timeout=0, add_etags=False, conditional=True, last_modified=None, max_age=0, mimetype=None, response_class=None, cache_timeout=None, etag=None, last_modified=None, max_age=None, response_class=None, conditional=None, mimetype=None)
                """;

        int left = 1;
        int right = code.length();
        int last = 0;
        while (left < right) {
            int mid = (left + right) / 2;
            var sub = code.substring(0, mid);
            try {
                api.predict(List.of(sub));
                System.out.println("Good length: " + mid);
                left = mid + 1;
                last = mid;
            } catch (Exception e) {
                System.out.println(" Bad length: " + mid);
                right = mid;
            }
        }

        System.out.println("Last good length: " + last);
    }
}
