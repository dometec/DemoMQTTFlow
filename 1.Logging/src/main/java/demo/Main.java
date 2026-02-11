package demo;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import io.smallrye.reactive.messaging.annotations.Blocking;

public class Main {

  private static final Logger LOG = Logger.getLogger(Main.class);

  // On worker thread, parallel
  //  @Blocking(ordered = false)
  //  @Incoming("logging")
  public void receive1(String message) throws InterruptedException {
    Thread.sleep(3_000);
    LOG.infof("Received: %.15s.", message.replace("\n", ""));
  }

  // On worker thread
  @Incoming("logging")
  public void receive2(String message) throws InterruptedException {
    Thread.sleep(3_000);
    LOG.infof("Received: %.15s.", message.replace("\n", ""));
  }

  // On event loop thread
  //  @Incoming("logging")
  public CompletionStage<Void> receive3(Message<String> message) throws InterruptedException {
    Thread.sleep(3_000);
    LOG.infof("Received: %.15s.", message.getPayload().replace("\n", ""));
    return message.ack();
  }

}
