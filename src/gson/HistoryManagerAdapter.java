package gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import history_manager.*;
import task.Task;
import task.SubTask;
import task.Epic;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class HistoryManagerAdapter extends TypeAdapter<HistoryManager> {
    public Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(File.class, new FileAdapter()).serializeNulls().create();

    @Override
    public void write(final JsonWriter jsonWriter, final HistoryManager manager) throws IOException {
        if (manager == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.beginObject();
            for (Task task : manager.getHistory()) {
                if (task instanceof Epic) {
                    jsonWriter.name("Epic" + task.getId());
                    jsonWriter.value(gson.toJson(task));
                } else if (task instanceof SubTask) {
                    jsonWriter.name("SubTask" + task.getId());
                    jsonWriter.value(gson.toJson(task));
                } else {
                    jsonWriter.name("Task" + task.getId());
                    jsonWriter.value(gson.toJson(task));
                }
            }
            jsonWriter.endObject();
        }
    }

    @Override
    public HistoryManager read(final JsonReader jsonReader) throws IOException {
        HistoryManager manager;
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        } else {
            manager = new InMemoryHistoryManager();
            jsonReader.beginObject();
            String field = null;
            while (jsonReader.hasNext()) {
                JsonToken token = jsonReader.peek();
                if (token.equals(JsonToken.NAME)) {
                    field = jsonReader.nextName();
                }
                if (field == null) {
                    jsonReader.peek();
                } else if (field.contains("Epic")) {
                    jsonReader.peek();
                    manager.add(gson.fromJson(jsonReader.nextString(), (Type) Epic.class));
                } else if (field.contains("SubTask")) {
                    jsonReader.peek();
                    manager.add(gson.fromJson(jsonReader.nextString(), (Type) SubTask.class));
                } else {
                    jsonReader.peek();
                    manager.add(gson.fromJson(jsonReader.nextString(), Task.class));
                }
            }
        }
        jsonReader.endObject();
        return manager;
    }
}