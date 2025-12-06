<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>запись нового пациента</title>
    <style>
        .error { color: red; font-weight: bold; }
        .form-group { margin-bottom: 15px; }
        label { display: block; }
    </style>
</head>
<body>
<h1>запись нового пациента</h1>

<#if dateError??><p class="error">${dateError}</p></#if>
<#if fullNameError??><p class="error">${fullNameError}</p></#if>

<form action="/add" method="POST">
    <div class="form-group">
        <label for="date">дата приема:</label>
        <input type="date" id="date" name="date" value="${formData.date!""}" required>
    </div>
    <div class="form-group">
        <label for="time">время приема:</label>
        <input type="time" id="time" name="time" required>
    </div>
    <div class="form-group">
        <label for="fullName">ФИО:</label>
        <input type="text" id="fullName" name="fullName" required>
    </div>
    <div class="form-group">
        <label for="dateOfBirth">дата рождения:</label>
        <input type="date" id="dateOfBirth" name="dateOfBirth" required>
    </div>
    <div class="form-group">
        <label for="type">тип пациента:</label>
        <select id="type" name="type" required>
            <option value="PRIMARY">первичный</option>
            <option value="SECONDARY">вторичный</option>
        </select>
    </div>
    <div class="form-group">
        <label for="symptoms">симптомы:</label>
        <textarea id="symptoms" name="symptoms" required></textarea>
    </div>

    <button type="submit" title="в дурку">записать</button>
</form>
<a href="/calendar">отмена</a>
</body>
</html>