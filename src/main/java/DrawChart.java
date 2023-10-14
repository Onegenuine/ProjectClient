import dto.MeansurementsResponse;
import dto.MeasurementDTO;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrawChart {
    public static void main(String[] args) {
        List<Double> temparatures = getTemperaturesFromServer();
        drawChart(temparatures);
    }

    private static List<Double> getTemperaturesFromServer() {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/measurements";

        MeansurementsResponse jsonResponse = restTemplate.getForObject(url, MeansurementsResponse.class);

        if (jsonResponse == null || jsonResponse.getMeasurements() == null)
            return Collections.emptyList();


        return jsonResponse.getMeasurements().stream().map(MeasurementDTO::getValue)
                .collect(Collectors.toList());
    }


    private static void drawChart(List<Double> temperates) {
        double[] xData = IntStream.range(0, temperates.size()).asDoubleStream().toArray();
        double[] yData = temperates.stream().mapToDouble(x -> x).toArray();

        XYChart chart = QuickChart.getChart("Temperatures", "X", "Y", "temperature",
        xData, yData);

        new SwingWrapper(chart).displayChart();
    }
}
