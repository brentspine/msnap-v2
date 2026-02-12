package net.msnap.discordservice.util;

import net.dv8tion.jda.api.entities.Member;

public class PermissionUtil {

    private static final long[] ADMINS = new long[] {
        1456288497324261509L,
        533779674824966154L
    };

    public static boolean isAdmin(long userId) {
        for (long admin : ADMINS) {
            if (admin == userId) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAdmin(Member member) {
        return member != null && isAdmin(member.getIdLong());
    }

}
