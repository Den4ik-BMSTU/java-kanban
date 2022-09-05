package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import gson.*;
import history_manager.*;
import manager.Managers;
import task.*;
import task_manager.TaskManager;


import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager manager = Managers.getDefault("http://localhost:8080", "key");
    public static  final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(File.class, new FileAdapter())
            .registerTypeAdapter(HistoryManager.class, new HistoryManagerAdapter())
            .serializeNulls().create();

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", this::handle);
    }

    public void handle(HttpExchange exchange) throws IOException {
        String response;
        String path = exchange.getRequestURI().getPath();
        String param = exchange.getRequestURI().getQuery();
        switch (path) {
            case "/src/task/task" -> handleTask(exchange);
            case "/src/task/subtask" -> handleSubTask(exchange);
            case "/src/task/epic" -> handleEpic(exchange);
            case "/src/task/subtask/epic" -> {
                int id = Integer.parseInt(param.split("=")[1]);
                List<SubTask> subTasks = manager.getSubTasksFromEpic(id);
                if (subTasks == null) {
                    exchange.sendResponseHeaders(404, 0);
                    response = "Эпик не найден!";
                } else {
                    response = GSON.toJson(subTasks);
                    exchange.sendResponseHeaders(200, 0);
                }
                sendText(exchange, response);
                exchange.close();
            }
            case "/tasks/history" -> {
                response = GSON.toJson(manager.getHistory());
                exchange.sendResponseHeaders(200, 0);
                sendText(exchange, response);
                exchange.close();
            }
            case "/tasks" -> {
                response = GSON.toJson(manager.getPrioritizedTasks());
                exchange.sendResponseHeaders(200, 0);
                sendText(exchange, response);
                exchange.close();
            }
        }
    }

    private void handleTask(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response;
        switch (method) {
            case "GET" ->
                response = handleGet(exchange, "Task");
            case "POST" ->
                response = handlePost(exchange, "Task");
            case "DELETE" ->
                response = handleDelete(exchange, "Task");
            default -> response="";
        }
        sendText(exchange, response);
        exchange.close();
    }

    private void handleSubTask(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response;
        switch (method) {
            case "GET" ->
                response = handleGet(exchange, "SubTask");
            case "POST" ->
                response = handlePost(exchange, "SubTask");
            case "DELETE" ->
                response = handleDelete(exchange, "SubTask");
            default -> response="";
        }
        sendText(exchange, response);
        exchange.close();
    }

    private void handleEpic(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response;
        switch (method) {
            case "GET" ->
                    response = handleGet(exchange, "Epic");
            case "POST" ->
                response = handlePost(exchange, "Epic");
            case "DELETE" ->
                response = handleDelete(exchange, "Epic");
            default -> response="";
        }
        sendText(exchange, response);
        exchange.close();
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        System.out.println("Сервер остановлен");
        server.stop(0);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseBody().write(resp);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private String handleDelete(HttpExchange h, String tasks) throws IOException {
        String param = h.getRequestURI().getQuery();
        String response;
        int id = 0;
        if (param != null) {
            id = Integer.parseInt(param.split("=")[1]);
        }
        if (param == null) {
            switch (tasks){
                case "Task"->manager.deleteAllTasks();
                case "SubTask"->manager.deleteAllSubTasks();
                case "Epic"->manager.deleteAllEpics();
            }
            h.sendResponseHeaders(200, 0);
            response = "Все "+tasks+" удалены!";
        } else {
            Task removedTask=null;
            switch (tasks){
                case "Task"-> removedTask = manager.removeTask(id);
                case "SubTask"->removedTask = manager.removeSubTask(id);
                case "Epic"->removedTask = manager.removeEpic(id);
            }

            if (removedTask == null) {
                h.sendResponseHeaders(404, 0);
                response = tasks+" не найдена";
            } else {
                h.sendResponseHeaders(200, 0);
                response = tasks + id + " удалена!";
            }
        }
        return response;
    }

    private String handlePost(HttpExchange h, String tasks) throws IOException {
        String param = h.getRequestURI().getQuery();
        String response;
        int id = 0;
        int result = -1;
        int result1 = -1;
        if (param != null) {
            id = Integer.parseInt(param.split("=")[1]);
        }
        String body = readText(h);
        if (body.isBlank()) {
            h.sendResponseHeaders(404, 0);
            response = "В теле запроса необходимо передать "+tasks+" в формате JSON";
        } else {
            Task task=null;
            SubTask subTask=null;
            Epic epic = null;
            switch (tasks){
                case "Task"->{task = GSON.fromJson(body, Task.class);}
                case "SubTask"->{subTask = GSON.fromJson(body, SubTask.class);}
                case "Epic"->{epic = GSON.fromJson(body, Epic.class);}
            }

            if (param == null) {
                switch (tasks){
                    case "Task"-> result = manager.addTask(task);
                    case "SubTask"->result = manager.addSubTask(subTask);
                    case "Epic"->result = manager.addEpic(epic);
                }
                if (result < 0) {
                    h.sendResponseHeaders(400, 0);
                    response = "Не удалось добавить "+tasks+"!";
                } else {
                    h.sendResponseHeaders(201, 0);
                    response = tasks+" успешно добавлена!";
                }
            } else {
                switch (tasks){
                    case "Task"-> {result1 = manager.updateTask(task);
                        task.setId(id);}
                    case "SubTask"->{result1 = manager.updateSubTask(subTask);
                        subTask.setId(id);}
                    case "Epic"->{result1 = manager.updateEpic(epic);
                        epic.setId(id);}
                }
                if (result1 < 0) {
                    h.sendResponseHeaders(400, 0);
                    response = "Не удалось обновить "+tasks+"!";
                } else {
                    h.sendResponseHeaders(201, 0);
                    response = tasks + id + " успешно обновлена!";
                }
            }
        }
        return response;
    }

    private String handleGet(HttpExchange h, String tasks) throws IOException {
        String param = h.getRequestURI().getQuery();
        String response=null;
        int id = 0;
        if (param != null) {
            id = Integer.parseInt(param.split("=")[1]);
        }
        if (param == null) {
            switch (tasks){
                case "Task"-> response = GSON.toJson(manager.getAllTasks());
                case "SubTask"->response = GSON.toJson(manager.getAllSubTasks());
                case "Epic"->response = GSON.toJson(manager.getAllEpics());
            }
            h.sendResponseHeaders(200, 0);
        } else {
            Task task = null;
            switch (tasks){
                case "Task"-> task = manager.getTask(id);
                case "SubTask"->task = manager.getSubTask(id);
                case "Epic"->task = manager.getEpic(id);
            }
            if (task == null) {
                h.sendResponseHeaders(404, 0);
                response = tasks+" не найдена";
            } else {
                response = GSON.toJson(task);
                h.sendResponseHeaders(200, 0);
            }
        }
        return response;
    }
}