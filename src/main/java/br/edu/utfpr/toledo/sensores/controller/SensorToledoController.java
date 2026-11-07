package br.edu.utfpr.toledo.sensores.controller;

import br.edu.utfpr.toledo.sensores.model.SensorToledo;
import br.edu.utfpr.toledo.sensores.service.SensorToledoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sensores")
public class SensorToledoController {

    private final SensorToledoService service;

    public SensorToledoController(SensorToledoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SensorToledo> criar(@RequestBody SensorToledo sensor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(sensor));
    }

    @PutMapping("/{sensorId}/{timestamp}")
    public ResponseEntity<SensorToledo> atualizar(
            @PathVariable String sensorId,
            @PathVariable Long timestamp,
            @RequestBody SensorToledo sensor) {
        sensor.setSensorId(sensorId);
        sensor.setTimestamp(timestamp);
        return ResponseEntity.ok(service.atualizar(sensor));
    }

    @DeleteMapping("/{sensorId}/{timestamp}")
    public ResponseEntity<SensorToledo> deletar(
            @PathVariable String sensorId,
            @PathVariable Long timestamp) {
        SensorToledo deletado = service.deletar(sensorId, timestamp);
        if (deletado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deletado);
    }

    @GetMapping
    public ResponseEntity<List<SensorToledo>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{sensorId}")
    public ResponseEntity<List<SensorToledo>> buscarPorSensorId(@PathVariable String sensorId) {
        return ResponseEntity.ok(service.buscarPorSensorId(sensorId));
    }

    @GetMapping("/{sensorId}/{timestamp}")
    public ResponseEntity<SensorToledo> buscarPorSensorIdETimestamp(
            @PathVariable String sensorId,
            @PathVariable Long timestamp) {
        SensorToledo item = service.buscarPorSensorIdETimestamp(sensorId, timestamp);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @GetMapping("/localizacao/{localizacao}")
    public ResponseEntity<List<SensorToledo>> buscarPorLocalizacao(@PathVariable String localizacao) {
        return ResponseEntity.ok(service.buscarPorLocalizacao(localizacao));
    }

    @GetMapping("/localizacao/{localizacao}/sensor/{sensorId}")
    public ResponseEntity<List<SensorToledo>> buscarPorSensorELocalizacao(
            @PathVariable String localizacao,
            @PathVariable String sensorId) {
        return ResponseEntity.ok(service.buscarPorSensorELocalizacao(sensorId, localizacao));
    }

    @GetMapping("/{sensorId}/temperatura")
    public ResponseEntity<List<SensorToledo>> buscarPorSensorETemperatura(
            @PathVariable String sensorId,
            @RequestParam(defaultValue = "0.0") Double min) {
        return ResponseEntity.ok(service.buscarPorSensorETemperaturaMinima(sensorId, min));
    }
}
