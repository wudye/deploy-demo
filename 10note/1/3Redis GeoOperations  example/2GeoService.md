package com.mwu.geodistance.service;

import com.mwu.geodistance.model.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoService {

    private final StringRedisTemplate redisTemplate;

    // 方式 1: 使用注入的 GeoOperations（如果配置了）
    // private final GeoOperations<String, String> geoOperations;

    /**
     * 添加地理位置
     */
    public void addLocation(String key, Location location) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        Point point = new Point(location.getLongitude(), location.getLatitude());
        geoOps.add(key, point, location.getName());
        
        log.info("添加地点: {} at ({}, {})", location.getName(), 
                 location.getLongitude(), location.getLatitude());
    }

    /**
     * 批量添加地理位置
     */
    public void addLocations(String key, List<Location> locations) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        Map<Object, Point> memberToPoint = locations.stream()
            .collect(
                java.util.stream.Collectors.toMap(
                    Location::getName,
                    loc -> new Point(loc.getLongitude(), loc.getLatitude())
                )
            );
        
        geoOps.add(key, memberToPoint);
        log.info("批量添加 {} 个地点到 {}", locations.size(), key);
    }

    /**
     * 获取地理位置
     */
    public Point getLocation(String key, String member) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        Point point = geoOps.position(key, member);
        
        if (point != null) {
            log.info("地点 {} 的坐标: ({}, {})", member, point.getX(), point.getY());
        } else {
            log.warn("地点 {} 不存在", member);
        }
        
        return point;
    }

    /**
     * 计算两点之间的距离
     */
    public Double calculateDistance(String key, String member1, String member2) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        // 默认单位: 米
        Distance distance = geoOps.distance(key, member1, member2);
        
        log.info("{} 和 {} 之间的距离: {} 米", member1, member2, distance.getValue());
        
        return distance.getValue();
    }

    /**
     * 计算两点之间的距离（指定单位）
     */
    public Double calculateDistance(String key, String member1, String member2, 
                                   DistanceUnit unit) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        Distance distance = geoOps.distance(key, member1, member2, unit);
        
        log.info("{} 和 {} 之间的距离: {} {}", 
                 member1, member2, distance.getValue(), unit);
        
        return distance.getValue();
    }

    /**
     * 计算与指定坐标的距离
     */
    public Double calculateDistanceToCoordinate(String key, String member, 
                                               Double longitude, Double latitude) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        Point referencePoint = new Point(longitude, latitude);
        Distance distance = geoOps.distance(key, referencePoint, member);
        
        log.info("地点 {} 到坐标 ({}, {}) 的距离: {} 米", 
                 member, longitude, latitude, distance.getValue());
        
        return distance.getValue();
    }

    /**
     * 查找指定范围内的地点（圆形范围）
     */
    public List<String> findLocationsInRadius(String key, Double longitude, 
                                              Double latitude, Double radius) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        Circle circle = new Circle(new Point(longitude, latitude), 
                                  new Distance(radius, Metrics.KILOMETERS));
        
        GeoResults<GeoLocation<String>> results = geoOps.radius(key, circle);
        
        List<String> locations = results.getContent().stream()
            .map(result -> result.getContent().getName())
            .toList();
        
        log.info("在 {} 公里范围内找到 {} 个地点", radius, locations.size());
        
        return locations;
    }

    /**
     * 查找指定范围内的地点（返回详细信息）
     */
    public List<GeoResult<GeoLocation<String>>> findLocationsInRadiusWithDetails(
            String key, Double longitude, Double latitude, Double radius) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        Circle circle = new Circle(new Point(longitude, latitude), 
                                  new Distance(radius, Metrics.KILOMETERS));
        
        // 返回距离和坐标信息
        GeoResults<GeoLocation<String>> results = geoOps.radius(
            key, 
            circle,
            RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance()    // 包含距离
                .includeCoordinates()  // 包含坐标
                .sortAscending()      // 按距离升序排序
        );
        
        results.getContent().forEach(result -> {
            GeoLocation<String> location = result.getContent();
            Distance distance = result.getDistance();
            Point point = location.getPoint();
            
            log.info("地点: {}, 距离: {} km, 坐标: ({}, {})",
                     location.getName(),
                     distance.getValue(),
                     point.getX(),
                     point.getY());
        });
        
        return results.getContent();
    }

    /**
     * 查找指定范围内的地点（按距离排序，限制数量）
     */
    public List<GeoResult<GeoLocation<String>>> findNearbyLocations(
            String key, Double longitude, Double latitude, 
            Double radius, int limit) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        Circle circle = new Circle(new Point(longitude, latitude), 
                                  new Distance(radius, Metrics.KILOMETERS));
        
        GeoResults<GeoLocation<String>> results = geoOps.radius(
            key, 
            circle,
            RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance()
                .includeCoordinates()
                .sortAscending()
                .limit(limit)  // 限制返回数量
        );
        
        return results.getContent();
    }

    /**
     * 查找指定范围内的地点（以某个地点为中心）
     */
    public List<String> findLocationsNearMember(String key, String centerMember, 
                                                 Double radius) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        Distance distance = new Distance(radius, Metrics.KILOMETERS);
        GeoResults<GeoLocation<String>> results = geoOps.radius(key, centerMember, distance);
        
        List<String> locations = results.getContent().stream()
            .map(result -> result.getContent().getName())
            .filter(name -> !name.equals(centerMember))  // 排除中心点
            .toList();
        
        log.info("在 {} {} 公里范围内找到 {} 个地点", 
                 centerMember, radius, locations.size());
        
        return locations;
    }

    /**
     * 计算地理哈希
     */
    public String getGeoHash(String key, String member) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        List<String> hashes = geoOps.hash(key, member);
        String geoHash = hashes.isEmpty() ? null : hashes.get(0);
        
        log.info("地点 {} 的地理哈希: {}", member, geoHash);
        
        return geoHash;
    }

    /**
     * 批量获取地理哈希
     */
    public Map<String, String> getGeoHashes(String key, List<String> members) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        
        List<String> hashes = geoOps.hash(key, members.toArray(new String[0]));
        
        Map<String, String> result = new java.util.HashMap<>();
        for (int i = 0; i < members.size(); i++) {
            result.put(members.get(i), hashes.get(i));
        }
        
        return result;
    }

    /**
     * 删除地理位置
     */
    public Long removeLocation(String key, String... members) {
        Long removed = redisTemplate.opsForZSet().remove(key, members);
        log.info("删除 {} 个地点", removed);
        return removed;
    }

    /**
     * 获取所有地点数量
     */
    public Long getLocationCount(String key) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        Long count = geoOps.size(key);
        log.info("地点总数: {}", count);
        return count;
    }
}
