package models.jsonplaceholder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Company {
    public String name;
    public String catchPhrase;
    public String bs;
}
