package Bot.Command;

import Bot.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public enum PermissionLevel {
   DEVELOPER, ADMIN, MEMBER;

   public boolean hasPerms(Member member) {
      if (this == DEVELOPER) {
         String memberID = member.getId();
         return memberID.equals(Config.get("OWNER_ID"));

      } else if (this == ADMIN) {
         return member.hasPermission(Permission.MANAGE_SERVER);

      } else {
         return member.hasPermission(Permission.MESSAGE_WRITE);
      }
   }
}
