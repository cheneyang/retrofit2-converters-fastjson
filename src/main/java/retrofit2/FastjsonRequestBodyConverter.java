package retrofit2;

import com.alibaba.fastjson.JSON;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Created by dayaa on 16/1/20.
 */
public class FastjsonRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private Type type;
    private Charset charset;
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    public FastjsonRequestBodyConverter(Type type, Charset charset) {
        this.type = type;
        this.charset = charset;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONString(value).getBytes(charset));
    }
}
