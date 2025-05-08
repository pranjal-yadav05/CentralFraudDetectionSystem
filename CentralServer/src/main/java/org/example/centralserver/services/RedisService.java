package org.example.centralserver.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.centralserver.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Save an object in Redis
    public void saveObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
        System.out.println("Saving object: " + key);
    }

    public void incrementFreq(String redisKey) {
        redisTemplate.opsForValue().increment(redisKey + "_freq", 1);
    }

    // Retrieve an object from Redis
    public <T> T getObject(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            // Convert the LinkedHashMap to the target type
            return objectMapper.convertValue(value, type);
        }
        return null;
    }


    // Delete a key from Redis
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key, 1L);
    }


    // Add a value to a Redis set
    public void addToSet(String setName, String value) {
        redisTemplate.opsForSet().add(setName, value);
    }

    // Retrieve all members of a Redis set
    public Set<Object> getSetMembers(String setName) {
        return redisTemplate.opsForSet().members(setName);
    }

    // Remove a value from a Redis set
    public void removeFromSet(String setName, String value) {
        redisTemplate.opsForSet().remove(setName, value);
    }
}
