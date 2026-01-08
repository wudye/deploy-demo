import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Location {
    private String name;
    private Double longitude;
    private Double latitude;
}

public class Location {
    private String name;
    private Double longitude;
    private Double latitude;
    
    // Lombok 自动生成
    public static class LocationBuilder {
        private String name;
        private Double longitude;
        private Double latitude;
        
        public LocationBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public LocationBuilder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }
        
        public LocationBuilder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }
        
        public Location build() {
            return new Location(name, longitude, latitude);
        }
    }
}
Location location = Location.builder()
    .name("天安门")
    .longitude(116.397128)
    .latitude(39.916527)
    .build();
