package demo;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.mqtt.MqttMessage;

public class Main {

  private static final Logger LOG = Logger.getLogger(Main.class);

  //   On eventloop
  //  @Incoming("logging")
  public CompletionStage<Void> receive1(MqttMessage<byte[]> message) {
    String s = new String(message.getPayload());
    LOG.infof("Received on Topic \"%s\" QoS %d: %.15s.", message.getTopic(), message.getQosLevel().value(), s);
    return message.ack();
  }

  // on Worker thread, parallel
  @Blocking(ordered = false)
  @Incoming("logging")
  public CompletionStage<Void> receive2(MqttMessage<byte[]> message) {
    String s = new String(message.getPayload());
    LOG.infof("Received on Topic \"%s\" QoS %d: %.15s.", message.getTopic(), message.getQosLevel().value(), s);
    return message.ack();
  }

}
