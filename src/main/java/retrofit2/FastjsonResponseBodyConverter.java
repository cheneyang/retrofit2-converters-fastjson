package retrofit2;

import com.alibaba.fastjson.JSON;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Created by dayaa on 16/1/20.
 */
final class FastjsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Type type;
    private Charset charset;

    public FastjsonResponseBodyConverter() {
    }

    public FastjsonResponseBodyConverter(Type type, Charset charset) {
        this.type = type;
        this.charset = charset;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return JSON.parseObject(value.string(), type);
        } finally {
            value.close();
        }
    }
}
