package Walter;

import Walter.entities.BlackRole;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class RoleHandler {

    public static void assignRole(Member member, BlackRole blackRoleToAssign) {
        Helper.getGuild().addRoleToMember(member, blackRoleToAssign.getInstance()).queue();
    }

    public static void removeRole(Member member, BlackRole blackRoleToRemove) {
        Helper.getGuild().removeRoleFromMember(member, blackRoleToRemove.getInstance()).queue();
    }

    public static boolean hasRole(Member member, BlackRole blackRoleToCheck) {
        return member.getRoles().contains(blackRoleToCheck.getInstance());
    }

    public static boolean hasRole(User user, BlackRole blackRoleToCheck) {
        Member member = Helper.getMember(user);
        if (member == null) return false;
        return hasRole(member, blackRoleToCheck);
    }

    public static Role getRole(long roleID) {
        return Helper.getGuild().getRoleById(roleID);
    }

    public static boolean hasMinimumRequiredRole(Member member, BlackRole blackRoleToCheck) {
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
