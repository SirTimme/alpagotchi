package bot.utils;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.text.MessageFormat;

/**
 * Static class providing convenience methods for the slashcommand replyaction
 */
public class MessageService {
	/**
	 * Convenience method for responding to a slashcommand interaction
	 * @param event The slashcommand event to reply to
	 * @param msg The bots' response to the user
	 * @param ephemeral If the message is sent ephemeral or not
	 */
	public static void reply(SlashCommandEvent event, String msg, boolean ephemeral) {
		event.reply(msg).setEphemeral(ephemeral).queue();
	}

	/**
	 * Convenience method for responding to a slashcommand interaction if no messageformat params are needed
	 * @param event The slashcommand event to reply to
	 * @param msg The bots' response to the user
	 * @param ephemeral If the message is sent ephemeral or not
	 */
	public static void reply(SlashCommandEvent event, MessageFormat msg, boolean ephemeral) {
		event.reply(msg.format(new Object[]{})).setEphemeral(ephemeral).queue();
	}
}

