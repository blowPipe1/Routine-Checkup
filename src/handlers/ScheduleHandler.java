package handlers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import freemarker.template.TemplateException;
import models.PatientStore;
import utils.TemplateRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class ScheduleHandler implements HttpHandler {
    private final TemplateRenderer renderer;

    public ScheduleHandler(TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGet(exchange);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, -1);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today);

        long totalPatientsInMonth = PatientStore.getTotalPatientsInMonth(currentMonth);

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("currentMonth", currentMonth);
        dataModel.put("today", today);
        dataModel.put("totalPatients", totalPatientsInMonth);


        Map<LocalDate, Integer> dailyCounts = new HashMap<>();
        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            LocalDate date = currentMonth.atDay(day);
            int count = PatientStore.getPatientsByDate(date).size();
            if (count > 0) {
                dailyCounts.put(date, count);
            }
        }
        dataModel.put("dailyCounts", dailyCounts);

        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            renderer.render("patient_schedule.ftlh", dataModel, os);
        } catch (TemplateException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
        }
    }
}
