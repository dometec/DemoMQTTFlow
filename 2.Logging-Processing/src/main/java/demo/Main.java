package demo;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Blocking;

public class Main {

  private static final Logger LOG = Logger.getLogger(Main.class);

  @Blocking(ordered = false)
  @Incoming("logging")
  @Outgoing("processed-a")
  public String receive(String message) {
    LOG.infof("Get: %.15s.", message);
    return message.toUpperCase();
  }

  @Incoming("processed-a")
  @Outgoing("processed-b")
  public Multi<String> filter(Multi<String> input) {
    LOG.infof("Process...");
    return input.map(i -> i.replace("\n", ""));
  }

  @Blocking(ordered = false)
  @Incoming("processed-b")
  public void endProcess(String word) {
    LOG.infof("Out: %.15s.", word);
  }

}
