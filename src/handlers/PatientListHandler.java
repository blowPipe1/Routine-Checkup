package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import freemarker.template.TemplateException;
import models.Patient;
import models.PatientStore;
import utils.FormParser;
import utils.TemplateRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class PatientListHandler implements HttpHandler {
    private final TemplateRenderer renderer;

    public PatientListHandler(TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGet(exchange);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            handlePostDelete(exchange);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, -1);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        LocalDate selectedDate = extractDateFromQuery(exchange.getRequestURI())
                .orElse(LocalDate.now());

        List<Patient> patientsForDay = PatientStore.getPatientsByDate(selectedDate);

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("patients", patientsForDay);
        dataModel.put("selectedDay", selectedDate);

        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            renderer.render("patient_list.ftlh", dataModel, os);
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    private void handlePostDelete(HttpExchange exchange) throws IOException {
        Map<String, String> dataModel = FormParser.parseForm(exchange.getRequestBody());

        String action = dataModel.get("action");
        String patientId = dataModel.get("patientId");
        String dateString = dataModel.get("date");

        if ("delete".equals(action) && patientId != null && dateString != null) {
            try {
                LocalDate date = LocalDate.parse(dateString);
                boolean deleted = PatientStore.deletePatient(date, patientId);
                String redirectUrl = "/patients?date=" + date.format(DateTimeFormatter.ISO_LOCAL_DATE);
                exchange.getResponseHeaders().set("Location", redirectUrl);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_SEE_OTHER, -1);
                return;

            } catch (DateTimeParseException e) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
                return;
            }
        }
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
    }

    private Optional<LocalDate> extractDateFromQuery(URI uri) {
        String query = uri.getQuery();
        if (query != null && query.startsWith("date=")) {
            try {
                return Optional.of(LocalDate.parse(query.substring(5)));
            } catch (DateTimeParseException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

}
