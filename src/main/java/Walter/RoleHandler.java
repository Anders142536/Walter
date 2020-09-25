package Walter;

import Walter.entities.BlackRole;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class RoleHandler {
    public static RoleHandler instance;

    public void assignRole(Member member, BlackRole blackRoleToAssign) {
        Helper.instance.getGuild().addRoleToMember(member, blackRoleToAssign.getInstance()).queue();
    }

    public void removeRole(Member member, BlackRole blackRoleToRemove) {
        Helper.instance.getGuild().removeRoleFromMember(member, blackRoleToRemove.getInstance()).queue();
    }

    public void toggleRole(Member member, BlackRole blackRoleToToggle) {

    }

    public boolean hasRole(Member member, BlackRole blackRoleToCheck) {
        return member.getRoles().contains(blackRoleToCheck.getInstance());
    }

    public boolean hasRole(User user, BlackRole blackRoleToCheck) {
        Member member = Helper.instance.getMember(user);
        if (member == null) return false;
        return hasRole(member, blackRoleToCheck);
    }

    public Language getLanguage(User user) {
        Member member = Helper.instance.getMember(user);
        if (member == null) return Language.ENGLISH;
        return getLanguage(member);
    }

    public Language getLanguage(Member member) {
        if (hasRole(member, BlackRole.ENGLISH)) return Language.ENGLISH;
        return Language.GERMAN;
    }

    public Role getRole(long roleID) {
        return Helper.instance.getGuild().getRoleById(roleID);
    }

    public boolean hasMinimumRequiredRole(Member member, BlackRole blackRoleToCheck) {
        boolean hasMinimumRequiredRole = false;
        switch (blackRoleToCheck) {
            case GUEST:
                if (hasRole(member, BlackRole.GUEST)) hasMinimumRequiredRole = true;
            case MEMBER:
                if (hasRole(member, BlackRole.MEMBER)) hasMinimumRequiredRole = true;
            case ADMIN:
                if (hasRole(member, BlackRole.ADMIN)) hasMinimumRequiredRole = true;
        }
        return hasMinimumRequiredRole;
    }


}
