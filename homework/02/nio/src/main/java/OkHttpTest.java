import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;public class OkHttpTest {
    public static void main(String[] args){

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8801")
                .method("GET", null)
                .build();
    }
}
