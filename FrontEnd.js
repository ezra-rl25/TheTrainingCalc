// FrontEnd.js

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("tokenForm");
    const alertBox = document.getElementById("alertBox");
    const chartsContainer = document.getElementById("charts");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const token = document.getElementById("accessToken").value;
        const startDate = document.getElementById("startDate").value;
        const endDate = document.getElementById("endDate").value;

        if (!token || !startDate || !endDate) {
            alert("Please fill in all fields.");
            return;
        }

        try {
            const response = await fetch("https://your-backend-api.com/metrics", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ token, startDate, endDate })
            });

            if (!response.ok) throw new Error("Failed to fetch metrics.");

            const data = await response.json();
            renderCharts(data);
            showAlerts(data);
        } catch (error) {
            console.error(error);
            alert("Error fetching data. Check the console for details.");
        }
    });
});

function renderCharts(data) {
    const { acrHistory, weeklyLoad, monotony, strain } = data;

    chartsContainer.innerHTML = `
        <canvas id="acrChart"></canvas>
        <canvas id="weeklyLoadChart"></canvas>
    `;

    new Chart(document.getElementById("acrChart"), {
        type: "line",
        data: {
            labels: acrHistory.map(entry => entry.date),
            datasets: [{
                label: "ACR",
                data: acrHistory.map(entry => entry.value),
                borderColor: "red",
                backgroundColor: "rgba(255, 99, 132, 0.2)",
                fill: true,
                tension: 0.3
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    new Chart(document.getElementById("weeklyLoadChart"), {
        type: "bar",
        data: {
            labels: weeklyLoad.map(entry => entry.week),
            datasets: [{
                label: "Weekly Load",
                data: weeklyLoad.map(entry => entry.load),
                backgroundColor: "rgba(54, 162, 235, 0.6)"
            }]
        },
        options: {
            responsive: true
        }
    });
}

function showAlerts(data) {
    const latestACR = data.acrHistory[data.acrHistory.length - 1]?.value || 0;

    let messages = [];
    if (latestACR > 1.5) {
        messages.push("⚠️ ACR is above 1.5. Risk of injury is increased. Consider a rest day.");
    }
    if (data.monotony > 2) {
        messages.push("⚠️ High monotony detected. Vary your training to reduce injury risk.");
    }
    if (data.strain > 6000) {
        messages.push("⚠️ High training strain. Consider adjusting workload.");
    }

    if (messages.length > 0) {
        alertBox.innerHTML = messages.map(msg => `<p>${msg}</p>`).join("");
        alertBox.style.display = "block";
    } else {
        alertBox.innerHTML = "<p>✅ No immediate injury risks detected.</p>";
        alertBox.style.display = "block";
    }
}