package demo;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface BadMessageService {

  @UserMessage("""
      Classify the following text.
      Respond ONLY with:
      true -> if the text contains offensive, rude or harmful content
      false -> otherwise
      Text: {text}
      """)
  boolean isBad(String text);

}
