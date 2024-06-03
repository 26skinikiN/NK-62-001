google.charts.load("current", {packages: ["corechart"]});
google.charts.setOnLoadCallback(drawAppStatuses);

function drawAppStatuses() {
    let res = [['Статус', 'Количество']];

    for (let i = 0; i < appStatusesNames.length; i++) {
        res.push([appStatusesNames[i], appStatusesValues[i]]);
    }

    var data = google.visualization.arrayToDataTable(res);

    var options = {
        title: 'Заявки по статусам'
    };

    var chart = new google.visualization.PieChart(document.getElementById('drawAppStatuses'));

    chart.draw(data, options);
}
