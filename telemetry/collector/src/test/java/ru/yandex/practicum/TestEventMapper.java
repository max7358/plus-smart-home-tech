package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.mapper.*;
import ru.yandex.practicum.model.*;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestEventMapper {
    @Test
    void testClimateSensorToAvro() {
        ClimateSensorEvent event = mock(ClimateSensorEvent.class);
        when(event.getTemperatureC()).thenReturn(25);
        when(event.getHumidity()).thenReturn(50);
        when(event.getCo2Level()).thenReturn(400);
        ClimateSensorAvro avro = ClimateSensorEventMapper.toAvro(event);
        assertEquals(25, avro.getTemperatureC());
        assertEquals(50, avro.getHumidity());
        assertEquals(400, avro.getCo2Level());
    }

    @Test
    void testDeviceAddedToAvro() {
        DeviceAddedEvent event = mock(DeviceAddedEvent.class);
        when(event.getId()).thenReturn("12345");
        when(event.getDeviceType()).thenReturn(DeviceType.MOTION_SENSOR);
        DeviceAddedEventAvro avro = DeviceAddedEventMapper.toAvro(event);
        assertEquals("12345", avro.getId());
        assertEquals(DeviceTypeAvro.MOTION_SENSOR, avro.getType());
    }

    @Test
    void testDeviceRemovedToAvro() {
        DeviceRemovedEvent event = mock(DeviceRemovedEvent.class);
        when(event.getId()).thenReturn("12345");
        DeviceRemovedEventAvro avro = DeviceRemovedEventMapper.toAvro(event);
        assertEquals("12345", avro.getId());
    }

    @Test
    void testLightSensorToAvro() {
        LightSensorEvent event = mock(LightSensorEvent.class);
        when(event.getLinkQuality()).thenReturn(25);
        when(event.getLuminosity()).thenReturn(50);
        LightSensorAvro avro = LightSensorEventMapper.toAvro(event);
        assertEquals(25, avro.getLinkQuality());
        assertEquals(50, avro.getLuminosity());
    }

    @Test
    void testMotionSensorToAvro() {
        MotionSensorEvent event = mock(MotionSensorEvent.class);
        when(event.getLinkQuality()).thenReturn(25);
        when(event.getVoltage()).thenReturn(50);
        when(event.isMotion()).thenReturn(true);
        MotionSensorAvro avro = MotionSensorEventMapper.toAvro(event);
        assertEquals(25, avro.getLinkQuality());
        assertEquals(50, avro.getVoltage());
        assertTrue(avro.getMotion());
    }

    @Test
    void testScenarioAddedToAvro() {
        ScenarioCondition condition = mock(ScenarioCondition.class);
        when(condition.getSensorId()).thenReturn("sensor123");
        when(condition.getType()).thenReturn(ConditionType.TEMPERATURE);
        when(condition.getOperation()).thenReturn(ConditionOperation.GREATER_THAN);
        when(condition.getValue()).thenReturn(25);
        DeviceAction action = mock(DeviceAction.class);
        when(action.getSensorId()).thenReturn("sensor123");
        when(action.getType()).thenReturn(ActionType.ACTIVATE);
        when(action.getValue()).thenReturn(1);
        List<ScenarioCondition> conditions = List.of(condition);
        List<DeviceAction> actions = List.of(action);
        ScenarioAddedEvent event = mock(ScenarioAddedEvent.class);
        when(event.getName()).thenReturn("Test Scenario");
        when(event.getConditions()).thenReturn(conditions);
        when(event.getActions()).thenReturn(actions);
        ScenarioAddedEventAvro avro = ScenarioAddedEventMapper.toAvro(event);
        assertEquals("Test Scenario", avro.getName());
        assertEquals(1, avro.getConditions().size());
        assertEquals("sensor123", avro.getConditions().getFirst().getSensorId());
        assertEquals(ConditionTypeAvro.TEMPERATURE, avro.getConditions().getFirst().getType());
        assertEquals(ConditionOperationAvro.GREATER_THAN, avro.getConditions().getFirst().getOperation());
        assertEquals(25, avro.getConditions().getFirst().getValue());
        assertEquals(1, avro.getActions().size());
        assertEquals("sensor123", avro.getActions().getFirst().getSensorId());
        assertEquals(ActionTypeAvro.ACTIVATE, avro.getActions().getFirst().getType());
        assertEquals(1, avro.getActions().getFirst().getValue());
    }

    @Test
    void testScenarioRemovedToAvro() {
        ScenarioRemovedEvent event = mock(ScenarioRemovedEvent.class);
        when(event.getName()).thenReturn("name1");
        ScenarioRemovedEventAvro avro = ScenarioRemovedEventMapper.toAvro(event);
        assertEquals("name1", avro.getName());
    }

    @Test
    void testSwitchSensorToAvro() {
        SwitchSensorEvent event = mock(SwitchSensorEvent.class);
        when(event.isState()).thenReturn(true);
        SwitchSensorAvro avro = SwitchSensorEventMapper.toAvro(event);
        assertTrue(avro.getState());
    }

    @Test
    void testTemperatureSensorToAvro() {
        TemperatureSensorEvent event = mock(TemperatureSensorEvent.class);
        when(event.getId()).thenReturn("sensor123");
        when(event.getHubId()).thenReturn("hub456");
        when(event.getTimestamp()).thenReturn(Instant.ofEpochMilli(1627840080000L));
        when(event.getTemperatureC()).thenReturn(22);
        when(event.getTemperatureF()).thenReturn(72);
        TemperatureSensorAvro avro = TemperatureSensorEventMapper.toAvro(event);
        assertEquals("sensor123", avro.getId());
        assertEquals("hub456", avro.getHubId());
        assertEquals(1627840080000L, avro.getTimestamp());
        assertEquals(22, avro.getTemperatureC());
        assertEquals(72, avro.getTemperatureF());
    }
}
