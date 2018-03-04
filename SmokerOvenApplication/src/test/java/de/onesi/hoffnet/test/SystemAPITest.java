package de.onesi.hoffnet.test;


import de.onesi.hoffnet.events.OvenEvent;
import de.onesi.hoffnet.states.OvenState;
import de.onesi.hoffnet.tinkerforge.sensor.ObjectTemperatureSensor;
import de.onesi.hoffnet.tinkerforge.sensor.RoomTemperatureSensor;
import de.onesi.hoffnet.web.SystemApi;
import de.onesi.hoffnet.web.data.Configuration;
import de.onesi.hoffnet.web.data.State;
import de.onesi.hoffnet.web.data.Temperature;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SystemAPITest extends BasicTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RoomTemperatureSensor roomTemperatureSensor;
    @Autowired
    private ObjectTemperatureSensor objectTemperatureSensor;
    @Autowired
    private SystemApi systemApi;

    @Override
    public void before() throws Exception {
        super.before();
        systemApi.setLog(LoggerFactory.getLogger(SystemApi.class));
    }

    public <T> T call(String path, Class<T> clazz) {
        return this.restTemplate.getForObject(path, clazz);
    }

    public <T> T call(String path, Object data, Class<T> clazz) {
        return this.restTemplate.postForObject(path, data, clazz);
    }

    public <T> ResponseEntity<T> callEntity(String path, Object data, Class<T> clazz) {
        return this.restTemplate.postForEntity(path, data, clazz);
    }

    @Test
    public void getState() {
        State state = call("/state", State.class);
        Assert.assertNotNull(state);
        Assert.assertEquals(OvenState.READY, state.getOvenState());
    }

    @Test
    public void getTemperature() {
        roomTemperatureSensor.temperature(9325);
        objectTemperatureSensor.temperature(6712);
        Temperature temperature = call("/temperature", Temperature.class);
        Assert.assertNotNull(temperature);
        Assert.assertNotNull(temperature.getObjectTemperature());
        Assert.assertNotNull(temperature.getRoomTemperature());
        Assert.assertEquals(93.25, temperature.getRoomTemperature(), 0d);
        Assert.assertEquals(67.12, temperature.getObjectTemperature(), 0d);
    }

    @Test
    public void sendEvent() {
        ResponseEntity response = callEntity("/state", OvenEvent.SMOKER_REFILLED, Object.class);
        Assert.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void configuration() {
        Configuration configuration = call("/configure", Configuration.class);
        Assert.assertNotNull(configuration);
        configuration.setObjectTemperature(67.12);
        configuration.setRoomTemperature(93.25);
        configuration.setTemperatureTolerance(2.0);
        Configuration newConfiguration = call("/configure", configuration, Configuration.class);
        Assert.assertEquals(configuration.getObjectTemperature(), newConfiguration.getObjectTemperature(), 0d);
        Assert.assertEquals(configuration.getRoomTemperature(), newConfiguration.getRoomTemperature(), 0d);
        Assert.assertEquals(configuration.getTemperatureTolerance(), newConfiguration.getTemperatureTolerance(), 0d);

    }
}
