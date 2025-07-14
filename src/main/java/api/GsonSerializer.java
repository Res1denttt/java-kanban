package api;

import api.handlers.adapters.DurationAdapter;
import api.handlers.adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public class GsonSerializer {
    private GsonSerializer() {
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}

