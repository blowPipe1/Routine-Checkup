package handlers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.Patient;
import models.PatientStore;
import utils.FormParser;
import utils.TemplateRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalTime;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class AddPatientHandler implements HttpHandler {
    private final TemplateRenderer renderer;

    public AddPatientHandler(TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGet(exchange, new HashMap<>(), new HashMap<>());
        } else if ("POST".equals(exchange.getRequestMethod())) {
            handlePost(exchange);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, -1);
        }
    }

    private void handleGet(HttpExchange exchange, Map<String, Object> errors, Map<String, String> oldFormData) throws IOException {
        Map<String, Object> dataModel = new HashMap<>(errors);
        dataModel.put("formData", oldFormData);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            renderer.render("add_patient.ftlh", dataModel, os);
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        Map<String, String> formData = FormParser.parseForm(exchange.getRequestBody());
        Map<String, Object> errors = validateForm(formData);

        if (!errors.isEmpty()) {
            handleGet(exchange, errors, formData);
            return;
        }

        LocalDate date = LocalDate.parse(formData.get("date"));
        Patient newPatient = new Patient(
                LocalTime.parse(formData.get("time")),
                formData.get("fullName"),
                LocalDate.parse(formData.get("dateOfBirth")),
                formData.get("type").toUpperCase(),
                formData.get("symptoms")
        );

        PatientStore.addPatient(date, newPatient);

        String redirectUrl = "/patients?date=" + date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        exchange.getResponseHeaders().set("Location", redirectUrl);
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_SEE_OTHER, -1);
    }


    private Map<String, Object> validateForm(Map<String, String> data) {
        Map<String, Object> errors = new HashMap<>();
        if (data.get("fullName") == null || data.get("fullName").isEmpty()) errors.put("fullNameError", "ФИО обязательно.");
        try {
            LocalDate date = LocalDate.parse(data.get("date"));
            if (date.isBefore(LocalDate.now())) {
                errors.put("dateError", "Дата записи не может быть в прошлом.");
            }
        } catch (DateTimeParseException e) {
            errors.put("dateError", "Неверный формат даты.");
        }
        return errors;
    }
}
