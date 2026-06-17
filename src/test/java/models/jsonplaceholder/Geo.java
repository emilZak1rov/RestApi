package models.jsonplaceholder;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class Geo {
    public String lat;
    public String lng;
}
