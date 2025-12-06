<!DOCTYPE html>
<html>
<head>
    <title>Расписание врача</title>
    <style>
        .calendar {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            gap: 5px;
            width: 50%;
            margin-top: 20px;
        }
        .day {
            padding: 10px;
            border: 1px solid #ccc;
            text-align: center;
            text-decoration: none;
            color: black;
            display: block;
        }
        .today {
            background-color: #e0f7fa;
            border-color: #00bcd4;
        }
        .has-patients {
            background-color: #c8e6c9;
            font-weight: bold;
        }
        .patient-count {
            display: block;
            font-size: 10px;
            color: #004d40;
            margin-top: 5px;
        }
    </style>
</head>
<body>
<h1>расписание на ${currentMonth}</h1>

<p>общее кол-во пациентов в этом месяце: <strong>${totalPatients}</strong></p>


<div class="calendar">

    <div>пн</div>
    <div>вт</div>
    <div>ср</div>
    <div>чт</div>
    <div>пт</div>
    <div>сб</div>
    <div>вс</div>

    <#assign firstDayOfMonth = currentMonth.atDay(1)>

    <#assign startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue()!7>


    <#list 1..<startDayOfWeek as i>
        <div></div>
    </#list>


    <#list 1..currentMonth.lengthOfMonth() as dayNum>
        <#assign date = currentMonth.atDay(dayNum)>

        <a href="#"
           class="day <#if date == today>today</#if>
                      <#if dailyCounts[date]??>has-patients</#if>">

            ${dayNum}

            <#if dailyCounts[date]??>
                <span class="patient-count">${dailyCounts[date]}</span>
            </#if>
        </a>
    </#list>
</div>

</body>
</html>