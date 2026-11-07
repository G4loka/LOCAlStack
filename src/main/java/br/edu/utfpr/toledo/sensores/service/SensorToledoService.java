package br.edu.utfpr.toledo.sensores.service;

import br.edu.utfpr.toledo.sensores.model.SensorToledo;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

@Service
public class SensorToledoService {

    private final DynamoDbTable<SensorToledo> table;

    public SensorToledoService(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("SensoresToledo", TableSchema.fromBean(SensorToledo.class));
    }

    public SensorToledo salvar(SensorToledo sensor) {
        table.putItem(sensor);
        return sensor;
    }

    public SensorToledo atualizar(SensorToledo sensor) {
        return table.updateItem(sensor);
    }

    public SensorToledo deletar(String sensorId, Long timestamp) {
        Key key = Key.builder()
                .partitionValue(sensorId)
                .sortValue(timestamp)
                .build();
        return table.deleteItem(key);
    }

    public List<SensorToledo> buscarPorSensorId(String sensorId) {
        QueryConditional conditional = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(sensorId).build()
        );
        return table.query(conditional)
                .items()
                .stream()
                .toList();
    }

    public SensorToledo buscarPorSensorIdETimestamp(String sensorId, Long timestamp) {
        Key key = Key.builder()
                .partitionValue(sensorId)
                .sortValue(timestamp)
                .build();
        return table.getItem(key);
    }

    public List<SensorToledo> buscarPorLocalizacao(String localizacao) {
        DynamoDbIndex<SensorToledo> gsi = table.index("LocalizacaoIndex");
        QueryConditional conditional = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(localizacao).build()
        );
        return gsi.query(conditional)
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<SensorToledo> buscarPorSensorELocalizacao(String sensorId, String localizacao) {
        QueryConditional conditional = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(sensorId).build()
        );
        Expression filterExpression = Expression.builder()
                .expression("Localizacao = :loc")
                .expressionValues(Map.of(":loc", AttributeValue.builder().s(localizacao).build()))
                .build();

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(conditional)
                .filterExpression(filterExpression)
                .build();

        return table.query(request)
                .items()
                .stream()
                .toList();
    }

    public List<SensorToledo> buscarPorSensorETemperaturaMinima(String sensorId, Double temperaturaMinima) {
        DynamoDbIndex<SensorToledo> lsi = table.index("TemperaturaIndex");
        QueryConditional conditional = QueryConditional.sortGreaterThanOrEqualTo(
                Key.builder()
                        .partitionValue(sensorId)
                        .sortValue(temperaturaMinima)
                        .build()
        );
        return lsi.query(conditional)
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<SensorToledo> listarTodos() {
        return table.scan()
                .items()
                .stream()
                .toList();
    }
}
