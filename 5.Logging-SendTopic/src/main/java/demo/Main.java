package demo;

import java.util.Random;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.smallrye.reactive.messaging.mqtt.MqttMessage;
import io.smallrye.reactive.messaging.mqtt.ReceivingMqttMessageMetadata;

public class Main {

  private static final Logger LOG = Logger.getLogger(Main.class);

  Random random = new Random();

  @Incoming("logging")
  @Outgoing("processed")
  public MqttMessage<String> process(MqttMessage<byte[]> message) {
    String s = new String(message.getPayload());
    LOG.infof("Received on Topic \"%s\" QoS %d: %.15s.", message.getTopic(), message.getQosLevel().value(), s);
    return MqttMessage.of("process/ch" + random.nextInt(50), "Process " + message.getTopic().length() + " chars.");
  }

}
