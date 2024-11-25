package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.List;

@Service
public class SensorService {
    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public void addSensor(Sensor sensor) {
        if(sensorRepository.existsByIdInAndHubId(List.of(sensor.getId()), sensor.getHubId())){
            return;
        }
        sensorRepository.save(sensor);
    }

    public void deleteSensor(String hubId, String id) {
        sensorRepository.deleteByHubIdAndId(hubId, id);
    }
}
