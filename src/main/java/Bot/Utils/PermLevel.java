package Bot.Utils;

import Bot.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public enum PermLevel {
	DEVELOPER, ADMIN, MEMBER;

	public boolean hasPermission(Member member) {
		return switch (this) {
			case DEVELOPER -> member.getId().equals(Config.get("DEV_ID"));
			case ADMIN -> member.hasPermission(Permission.MANAGE_SERVER);
			default -> true;
		};
	}
}
