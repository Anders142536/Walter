package Walter;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class RoleHandler {
    public static RoleHandler instance;

    public void assignRole(Member member, RoleID roleToAssign) {
        Helper.instance.getGuild().addRoleToMember(member, roleToAssign.getInstance()).queue();
    }

    public void removeRole(Member member, RoleID roleToRemove) {
        Helper.instance.getGuild().removeRoleFromMember(member, roleToRemove.getInstance()).queue();
    }

    public void toggleRole(Member member, RoleID roleToToggle) {

    }

    public boolean hasRole(Member member, RoleID roleToCheck) {
        return member.getRoles().contains(roleToCheck.getInstance());
    }

    Role getRole(long roleID) {
        return Helper.instance.getGuild().getRoleById(roleID);
    }

    public boolean hasMinimumRequiredRole(Member member, RoleID roleToCheck) {
        boolean hasMinimumRequiredRole = false;
        switch (roleToCheck) {
            case GUEST:
                if (hasRole(member, RoleID.GUEST)) hasMinimumRequiredRole = true;
            case MEMBER:
                if (hasRole(member, RoleID.MEMBER)) hasMinimumRequiredRole = true;
            case ADMIN:
                if (hasRole(member, RoleID.ADMIN)) hasMinimumRequiredRole = true;
        }
        return hasMinimumRequiredRole;
    }


}
