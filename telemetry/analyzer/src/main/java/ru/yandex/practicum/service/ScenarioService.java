package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.repository.ScenarioRepository;

@Service
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;

    public ScenarioService(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    public void addScenario(Scenario scenario) {
        scenarioRepository.save(scenario);
    }

    public void deleteScenario(String hubId, String name) {
        scenarioRepository.deleteByHubIdAndName(hubId, name);
    }
}
