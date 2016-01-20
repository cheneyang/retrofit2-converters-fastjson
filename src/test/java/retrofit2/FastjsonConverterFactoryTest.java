package retrofit2;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dayaa on 16/1/20.
 */
public class FastjsonConverterFactoryTest {

    public static class AnName {
        private String theName;

        public AnName() {
        }

        public AnName(String theName) {
            this.theName = theName;
        }

        public void setTheName(String theName) {
            this.theName = theName;
        }

        public String getTheName() {
            return theName;
        }
    }

    interface Service {
        @POST("/")
        Call<AnName> anName(@Body AnName name);

    }

    @Rule
    public final MockWebServer server = new MockWebServer();

    private Service service;

    @Before
    public void setUp() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(FastjsonConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);
    }

    @Test
    public void anImplementation() throws IOException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"theName\":\"value\"}"));

        Call<AnName> call = service.anName(new AnName("value"));
        Response<AnName> response = call.execute();
        AnName body = response.body();
        assertThat(body.theName).isEqualTo("value");

        RecordedRequest request = server.takeRequest();
        assertThat(request.getBody().readUtf8()).isEqualTo("{\"theName\":\"value\"}");
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/json; charset=UTF-8");
    }

    @Test
    public void serializeUsesConfiguration() throws IOException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{}"));

        service.anName(new AnName(null)).execute();

        RecordedRequest request = server.takeRequest();
        assertThat(request.getBody().readUtf8()).isEqualTo("{}"); // Null value was not serialized.
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/json; charset=UTF-8");
    }
}
