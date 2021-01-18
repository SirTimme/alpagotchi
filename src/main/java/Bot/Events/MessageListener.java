package Bot.Events;

import Bot.Command.CommandManager;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.EnumSet;

public class MessageListener extends ListenerAdapter {
	private final CommandManager cmdManager;

	public MessageListener() {
		this.cmdManager = new CommandManager();
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		final String prefix = IDataBaseManager.INSTANCE.getPrefix(event.getGuild().getIdLong());

		if (event.getAuthor().isBot() || event.isWebhookMessage() || !event.getMessage().getContentRaw().startsWith(prefix) || !checkPermissions(event)) {
			return;
		}

		cmdManager.handle(event, prefix);
	}

	private boolean checkPermissions(GuildMessageReceivedEvent event) {
		final Role everyoneRole = event.getGuild().getRoleById(event.getGuild().getIdLong());
		final Member botClient = event.getGuild().getMemberById(event.getJDA().getSelfUser().getIdLong());

		EnumSet<Permission> deniedPermissions = EnumSet.noneOf(Permission.class);
		EnumSet<Permission> allowedPermissions = EnumSet.noneOf(Permission.class);

		for (PermissionOverride permissionOverwrite : event.getChannel().getPermissionOverrides()) {
			if (permissionOverwrite.isRoleOverride() && (botClient.getRoles().contains(permissionOverwrite.getRole()) || permissionOverwrite.getRole() == everyoneRole)) {
				allowedPermissions.addAll(permissionOverwrite.getAllowed());
				deniedPermissions.addAll(permissionOverwrite.getDenied());
			} else if (permissionOverwrite.isMemberOverride() && permissionOverwrite.getMember().equals(botClient)) {
				deniedPermissions.addAll(permissionOverwrite.getDenied());
				allowedPermissions.addAll(permissionOverwrite.getAllowed());
			}
		}

		EnumSet<Permission> requiredPermissions = EnumSet.of(
				Permission.MESSAGE_WRITE,
				Permission.MESSAGE_ATTACH_FILES,
				Permission.MESSAGE_ADD_REACTION,
				Permission.VIEW_CHANNEL,
				Permission.MESSAGE_HISTORY,
				Permission.MESSAGE_EMBED_LINKS,
				Permission.MESSAGE_EXT_EMOJI,
				Permission.MESSAGE_READ
		);

		deniedPermissions.removeIf(allowedPermissions::contains);

		for (Permission permission : requiredPermissions) {
			if (deniedPermissions.contains(permission) && permission != Permission.MESSAGE_WRITE) {
				event.getChannel().sendMessage("⚠ I am missing at least the **" + permission.getName() + "** permission to work properly").queue();
				return false;
			}
		}
		return true;
	}
}
