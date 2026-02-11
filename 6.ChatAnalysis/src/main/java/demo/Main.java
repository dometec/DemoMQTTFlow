package demo;

import java.util.Objects;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.mqtt.MqttMessage;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;

public class Main {

  private static final Logger LOG = Logger.getLogger(Main.class);

  private static final String CHAT_OPERATOR_NAME = "GRANDE FRATELLO";
  private static final String STANDARD_WARNING_MESSAGE = "Ricorda di mantenere un linguaggio cordiale ed educato!";

  @Inject
  BadMessageService badMessageService;

  @ActivateRequestContext
  @Incoming("logging")
  @Outgoing("warning")
  public Multi<MqttMessage<String>> analize(Multi<MqttMessage<byte[]>> messages) {

    return messages

        .filter(m -> !m.getTopic().contains(CHAT_OPERATOR_NAME))

        .onItem().transformToUniAndMerge(m -> Uni.createFrom().item(() -> {

          boolean result = badMessageService.isBad(new String(m.getPayload()));
          if (result) {
            LOG.warnf("Bad message detected. Topic=%s Payload=%s",
                m.getTopic(),
                new String(m.getPayload()));
          }
          return result;
        }).runSubscriptionOn(io.smallrye.mutiny.infrastructure.Infrastructure.getDefaultWorkerPool())

            .onItem().transform(isBad -> {
              if (isBad) {
                String user = m.getTopic().split("/")[2];
                String topic = "chat/private/" + CHAT_OPERATOR_NAME + "/" + user + "/messages";
                return MqttMessage.of(topic, STANDARD_WARNING_MESSAGE);
              }
              return null;
            }))

        .select().where(Objects::nonNull);

  }

}
