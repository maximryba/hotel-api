package test.task.hotel_api.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {

    private String name;
    private String brand;
    private String city;
    private String country;
    private String amenities;

    public boolean hasAnyCriteria() {
        return StringUtils.hasText(name) ||
                StringUtils.hasText(brand) ||
                StringUtils.hasText(city) ||
                StringUtils.hasText(country) ||
                StringUtils.hasText(amenities);
    }
}
