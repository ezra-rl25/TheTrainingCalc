import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MetricExporter {

    public static void main(String[] args) {
        Map<String, List<MetricDataPoint>> allMetrics = new HashMap<>();

        allMetrics.put("acuteLoad", Arrays.asList(
                new MetricDataPoint("2025-04-01", 320),
                new MetricDataPoint("2025-04-08", 380)
        ));

        allMetrics.put("chronicLoad", Arrays.asList(
                new MetricDataPoint("2025-04-01", 950),
                new MetricDataPoint("2025-04-08", 1020)
        ));

        allMetrics.put("trimp", Arrays.asList(
                new MetricDataPoint("2025-04-01", 180),
                new MetricDataPoint("2025-04-08", 210)
        ));

        allMetrics.put("weeklyElevation", Arrays.asList(
                new MetricDataPoint("2025-04-01", 1300),
                new MetricDataPoint("2025-04-08", 1600)
        ));

        allMetrics.put("tsb", Arrays.asList(
                new MetricDataPoint("2025-04-01", 550),
                new MetricDataPoint("2025-04-08", 620)
        ));

        exportToJson(allMetrics, "src/main/resources/metrics.json");
    }

    public static void exportToJson(Map<String, List<MetricDataPoint>> metrics, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(metrics, writer);
            System.out.println("Exported JSON to " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to write JSON: " + e.getMessage());
        }
    }
}
