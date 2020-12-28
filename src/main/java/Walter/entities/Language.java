package Walter.entities;

import Walter.Helper;
import Walter.RoleHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public enum Language {
    ENGLISH(0),
    GERMAN (1);

    public final int index;

    Language(int index) { this.index = index; }

    public static Language getLanguage(User user) {
        Member member = Helper.getMember(user);
        if (member == null) return Language.ENGLISH;
        return getLanguage(member);
    }

    public static Language getLanguage(Member member) {
        if (RoleHandler.hasRole(member, BlackRole.ENGLISH)) return Language.ENGLISH;
        return Language.GERMAN;
    }
}
