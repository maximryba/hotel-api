package test.task.hotel_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistogramResponse {

    private Map<String, Long> histogram = new LinkedHashMap<>();

    public static HistogramResponse fromList(List<Object[]> results) {
        HistogramResponse response = new HistogramResponse();
        for (Object[] result : results) {
            String key = (String) result[0];
            Long count = (Long) result[1];
            response.getHistogram().put(key, count);
        }
        return response;
    }
}
