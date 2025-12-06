package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class PatientStore {
    private static final Map<LocalDate, List<Patient>>store = new HashMap<>();

    static {
        LocalDate today = LocalDate.now();
        store.put(today, new ArrayList<>(Arrays.asList(
                new Patient(LocalTime.of(9, 0), "Иванов Ван Даркхолм", LocalDate.of(1985, 10, 20), "PRIMARY", "температура"),
                new Patient(LocalTime.of(10, 30), "Billy Herrington", LocalDate.of(1992, 4, 15), "SECONDARY", "Повторный прием")

        )));

        LocalDate tomorrow = today.plusDays(1);
        store.put(tomorrow, new ArrayList<>(Arrays.asList(
                new Patient(LocalTime.of(14, 0), "Цаль Виталий", LocalDate.of(2001, 1, 1), "PRIMARY", "дотер головного мозга"),
                new Patient(LocalTime.of(11, 0), "Александр Костылёв", LocalDate.of(1976, 7, 7), "SECONDARY", "играет в тир 3 помойке")
        )));
    }

    public static List<Patient> getPatientsByDate(LocalDate date) {
        return store.getOrDefault(date, Collections.emptyList()).stream()
                .sorted(Comparator.comparing(Patient::getTime))
                .collect(Collectors.toList());
    }

    public static long getTotalPatientsInMonth(YearMonth yearMonth) {
        return store.entrySet().stream()
                .filter(entry -> YearMonth.from(entry.getKey()).equals(yearMonth))
                .flatMap(entry -> entry.getValue().stream())
                .count();
    }

    public static boolean deletePatient(LocalDate date, String patientId) {
        List<Patient> patients = store.get(date);
        if (patients == null) {
            return false;
        }
        return patients.removeIf(p -> p.getId().equals(patientId));
    }
}
