package br.edu.utfpr.toledo.sensores.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class SensorToledo {

    @JsonProperty("SensorID")
    private String sensorId;

    @JsonProperty("Timestamp")
    private Long timestamp;

    @JsonProperty("Temperatura")
    private Double temperatura;

    @JsonProperty("Umidade")
    private Double umidade;

    @JsonProperty("Localizacao")
    private String localizacao;

    public SensorToledo() {
    }

    public SensorToledo(String sensorId, Long timestamp, Double temperatura, Double umidade, String localizacao) {
        this.sensorId = sensorId;
        this.timestamp = timestamp;
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.localizacao = localizacao;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "TemperaturaIndex")
    @DynamoDbAttribute("SensorID")
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    @DynamoDbSortKey
    @DynamoDbSecondarySortKey(indexNames = "LocalizacaoIndex")
    @DynamoDbAttribute("Timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDbSecondarySortKey(indexNames = "TemperaturaIndex")
    @DynamoDbAttribute("Temperatura")
    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    @DynamoDbAttribute("Umidade")
    public Double getUmidade() {
        return umidade;
    }

    public void setUmidade(Double umidade) {
        this.umidade = umidade;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "LocalizacaoIndex")
    @DynamoDbAttribute("Localizacao")
    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}
