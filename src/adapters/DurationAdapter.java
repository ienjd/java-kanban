package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter out, Duration value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        long hours = value.toHours();
        long minutes = value.toMinutes() % 60;
        long seconds = value.getSeconds() % 60;

        String formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        out.value(formattedDuration);
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        String durationString = in.nextString();

        try {
            String[] parts = durationString.split(":");
            if (parts.length != 3) {
                throw new IOException("Неверный формат длительности: " + durationString);
            }

            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);

            return Duration.ofHours(hours)
                    .plusMinutes(minutes)
                    .plusSeconds(seconds);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IOException("Ошибка при парсинге длительности: " + durationString, e);
        }
    }
}
