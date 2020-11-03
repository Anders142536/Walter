package Walter.Quoter;

import javax.annotation.Nonnull;

public class QuoteMessage {

    String author;
    String timestamp;
    String message;

    public QuoteMessage(@Nonnull String author,@Nonnull String timestamp,@Nonnull String message) {
        this.author = author;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("**").append(author).append("** *").append(timestamp).append("*");

        message.lines().forEach(line -> {
            builder.append("\n> ").append(line);
        });

        return builder.toString();
    }
}
