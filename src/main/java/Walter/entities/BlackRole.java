package Walter.entities;

import Walter.RoleHandler;
import net.dv8tion.jda.api.entities.Role;

public enum BlackRole {
    ADMIN       (254264388209475584L),
    BOT         (0L),
    ENGLISH     (431236126443831308L),
    EVERYONE    (254263827237961729L),
    GUEST       (401876564154777620L),
    MEMBER      (254264987294498817L),
    NETFLIX     (397462377521610762L),
    OVERLORD    (391257227975196673L),
    WALTER      (391285765893783554L);

    public final long ID;

    BlackRole(long ID) { this.ID = ID; }

    //role instances have an expiring life span, therefore we must fetch freshly everytime
    public Role getInstance() {
        return RoleHandler.getRole(ID);
    }

    public String getName() { return getInstance().getName(); }

    public String getAsMention() { return getInstance().getAsMention(); }
}
