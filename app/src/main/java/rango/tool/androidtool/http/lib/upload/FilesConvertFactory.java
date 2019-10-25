package rango.tool.androidtool.http.lib.upload;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class FilesConvertFactory extends Converter.Factory {

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        for (Annotation annotation : methodAnnotations) {
            if (annotation instanceof UploadMoreFiles) {
                return new FilesRequestBodyConverter();
            }
        }
        return null;
    }
}
