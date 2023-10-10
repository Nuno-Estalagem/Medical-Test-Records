package Quarkus.Group25;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DecodeRequest {

    public DecodeRequest() {

    }

    public List<String> decode(String request) {
        String decoded = URLDecoder.decode(request, StandardCharsets.UTF_8);
        String[] elements = decoded.split("&");
        List<String> values = new ArrayList<>();
        for (String element : elements) {
            String[] minielem = element.split("=");
            for (int i = 0; i < minielem.length; i++) {
                if (i % 2 != 0)
                    values.add(minielem[i]);
            }

        }
        return values;
    }
}