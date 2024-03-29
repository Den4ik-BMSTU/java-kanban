package task_manager;

import gson.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import history_manager.HistoryManager;
import server.KVTaskClient;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class HTTPTaskManager extends FileBackedTaskManager {

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(File.class, new FileAdapter())
            .registerTypeAdapter(HistoryManager.class, new HistoryManagerAdapter())
            .serializeNulls().create();
    private final String key;
    private final String url;

    public HTTPTaskManager(String url, String key) {
        this.url = url;
        this.key = key;
    }

    @Override
    protected void save() {
        String manager = gson.toJson(this);
        try {
            new KVTaskClient(url).put(key, manager);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static HTTPTaskManager loadFromServer(String url, String key) throws IOException, InterruptedException {
        String json = new KVTaskClient(url).load(key);
        if (json.isEmpty()) {
            return new HTTPTaskManager(url, key);
        }
        return gson.fromJson(json, HTTPTaskManager.class);
    }
}