package Walter.entities;

import Walter.Helper;
import net.dv8tion.jda.api.entities.TextChannel;

public enum BlackChannel {
    ADMIN (391278094352121857L),
    CINEMA (305417211508555776L),
    CONFIG (403266783844237323L),
    DROPZONE (391292051712638987L),
    GENERAL (305420693653159938L),
    LOG (722932781243695114L),
    NEWS (405462316470108160L);

    public final long ID;

    BlackChannel(long ID) { this.ID = ID; }

    public TextChannel getInstance() {
        return Helper.instance.getTextChannel(this);
    }
}
