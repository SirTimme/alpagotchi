package Bot.Utils;

import Bot.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public enum Level {
	DEVELOPER, ADMIN, MEMBER;

	public boolean hasPermission(Member member) {
		switch (this) {
			case DEVELOPER:
				return member.getId().equals(Config.get("DEV_ID"));
			case ADMIN:
				return member.hasPermission(Permission.MANAGE_SERVER);
			default:
				return true;
		}
	}
}
