const metrics = [
  {
    id: "acuteLoad",
    name: "Acute Load",
    chartType: "bar",
    description: "Measures recent training volume (last 7 days)",
    color: "rgba(75, 192, 192, 0.7)"
  },
  {
    id: "chronicLoad",
    name: "Chronic Load",
    chartType: "line",
    description: "Measures long-term fitness base (last 28 days)",
    color: "rgba(153, 102, 255, 0.7)"
  },
  {
    id: "trimp",
    name: "TRIMP",
    chartType: "line",
    description: "Intensity-adjusted training stress",
    color: "rgba(255, 99, 132, 0.7)"
  },
  {
    id: "weeklyElevation",
    name: "Weekly Elevation Gain",
    chartType: "bar",
    description: "Total elevation climbed weekly",
    color: "rgba(255, 206, 86, 0.7)"
  },
  {
    id: "tsb",
    name: "Training Stress Balance (TSB)",
    chartType: "line",
    description: "Indicates fatigue vs readiness",
    color: "rgba(54, 162, 235, 0.7)"
  }
];

const graphsContainer = document.getElementById("graphsContainer");
const metricToggles = document.getElementById("metricToggles");
let chartInstances = {};

fetch("data/metrics.json")
  .then(res => res.json())
  .then(data => {
    metrics.forEach(metric => {
      const toggle = document.createElement("div");
      toggle.classList.add("metric-toggle");
      toggle.innerHTML = `
        <input type="checkbox" id="${metric.id}" />
        <label for="${metric.id}" title="${metric.description}">${metric.name}</label>
      `;
      metricToggles.appendChild(toggle);

      document.getElementById(metric.id).addEventListener("change", (e) => {
        if (e.target.checked) {
          drawChart(metric, data[metric.id]);
        } else {
          destroyChart(metric.id);
        }
      });
    });
  });

function drawChart(metric, dataPoints) {
  const canvas = document.createElement("canvas");
  canvas.id = `${metric.id}-chart`;
  graphsContainer.appendChild(canvas);

  const ctx = canvas.getContext("2d");
  chartInstances[metric.id] = new Chart(ctx, {
    type: metric.chartType,
    data: {
      labels: dataPoints.map(d => d.date),
      datasets: [{
        label: metric.name,
        data: dataPoints.map(d => d.value),
        backgroundColor: metric.color,
        borderColor: metric.color,
        fill: true
      }]
    },
    options: {
      responsive: true,
      plugins: {
        tooltip: {
          mode: 'index',
          intersect: false
        }
      },
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}

function destroyChart(id) {
  chartInstances[id]?.destroy();
  document.getElementById(`${id}-chart`)?.remove();
}
