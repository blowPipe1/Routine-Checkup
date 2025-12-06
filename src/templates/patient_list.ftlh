<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <style>
        .PRIMARY { color: blue; font-weight: bold; }
        .SECONDARY { color: green; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; }
    </style>
</head>
<body>
<h1>список пациентов на ${selectedDay}</h1>
<p>общее кол-во пациентов: ${patients?size}</p>

<a href="/add?date=${selectedDay}">записать нового пациента</a> |
<a href="/calendar">вернуться в календарь</a>

<table>
    <thead>
    <tr>
        <th>время</th>
        <th>ФИО</th>
        <th>дата рождения</th>
        <th>тип пациента</th>
        <th>симптомы</th>
        <th>действия</th>
    </tr>
    </thead>
    <tbody>
    <#list patients as patient>
        <tr>
            <td>${patient.time}</td>
            <td>${patient.fullName}</td>
            <td>${patient.dateOfBirth}</td>
            <td class="${patient.type}">${patient.type}</td>
            <td>${patient.symptoms}</td>
            <td>
                <form action="/patients" method="POST">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="date" value="${selectedDay}">
                    <input type="hidden" name="patientId" value="${patient.id}">
                    <button type="submit">удалить</button>
                </form>
            </td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>