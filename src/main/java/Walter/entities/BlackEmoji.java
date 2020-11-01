package Walter.entities;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.internal.requests.Route;

public enum  BlackEmoji {
    POPCORN ("\uD83C\uDF7F");

    BlackEmoji(String identifier) {
        this.identifier = identifier;
    }

    public final String identifier;


}
