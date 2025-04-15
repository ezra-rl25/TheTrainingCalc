async function fetchTrainingData() {
  const token = document.getElementById('tokenInput').value;
  const start = document.getElementById('startDate').value;
  const end = document.getElementById('endDate').value;

  const response = await fetch(`https://your-backend-api.com/training-data?token=${token}&start=${start}&end=${end}`);
  const data = await response.json();

  updateCharts(data);
  updateAlerts(data);
}

function updateCharts(data) {
  const acrCtx = document.getElementById('acrChart').getContext('2d');
  const weeklyLoadCtx = document.getElementById('weeklyLoadChart').getContext('2d');

  new Chart(acrCtx, {
    type: 'line',
    data: {
      labels: data.acr.dates,
      datasets: [{
        label: 'ACR',
        data: data.acr.values,
        borderColor: '#3e95cd',
        fill: false
      }]
    },
    options: {
      responsive: true,
      scales: { y: { beginAtZero: true } }
    }
  });

  new Chart(weeklyLoadCtx, {
    type: 'bar',
    data: {
      labels: data.weeklyLoad.dates,
      datasets: [{
        label: 'Weekly Load',
        data: data.weeklyLoad.values,
        backgroundColor: '#8e5ea2'
      }]
    },
    options: {
      responsive: true,
      scales: { y: { beginAtZero: true } }
    }
  });
}

function updateAlerts(data) {
  const alertsDiv = document.getElementById('alerts');
  alertsDiv.style.display = 'block';

  let alerts = [];

  if (data.acr.values.some(val => val > 1.5)) {
    alerts.push("⚠️ ACR exceeds 1.5. High injury risk detected!");
  }

  if (data.restDays < 2) {
    alerts.push("💤 Not enough rest days. Consider adding rest to avoid fatigue.");
  }

  alertsDiv.innerHTML = alerts.length
    ? alerts.map(alert => `<p>${alert}</p>`).join('')
    : "<p>No current alerts. Keep up the good work!</p>";
}
