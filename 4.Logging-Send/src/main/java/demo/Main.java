package demo;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

public class Main {

  private static final Logger LOG = Logger.getLogger(Main.class);

  @Incoming("logging")
  @Outgoing("processed")
  public String receiveState(String message) {
    LOG.infof("Received: %.15s.", message);
    return message.replace("\n", "").toLowerCase();
  }

}
