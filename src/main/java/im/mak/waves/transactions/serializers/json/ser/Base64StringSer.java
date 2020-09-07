package im.mak.waves.transactions.serializers.json.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import im.mak.waves.transactions.common.Base64String;

import java.io.IOException;

public class Base64StringSer extends JsonSerializer<Base64String> {
    @Override
    public void serialize(Base64String encodedString, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (encodedString == null || encodedString.bytes().length == 0)
            gen.writeNull();
        else
            gen.writeString(encodedString.encoded());
    }
}
