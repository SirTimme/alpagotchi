package bot.utils;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;

import java.text.MessageFormat;
import java.util.Collections;

/**
 * Static class providing convenience methods for replying to a slashcommand
 */
public class MessageService {
	/**
	 * Convenience method for responding to a slashcommand interaction
	 * @param event The slashcommand event to reply to
	 * @param msg The bots' response to the user
	 * @param ephemeral If the message is sent ephemeral or not
	 */
	public static void reply(final SlashCommandEvent event, final String msg, final boolean ephemeral) {
		event.reply(msg).setEphemeral(ephemeral).queue();
	}

	/**
	 * Convenience method for responding to a slashcommand interaction
	 * @param event The slashcommand event to reply to
	 * @param msg The bots' response to the user
	 * @param ephemeral If the message is sent ephemeral or not
	 * @param buttons Adds buttons to the message
	 */
	public static void reply(final SlashCommandEvent event, final MessageFormat msg, final boolean ephemeral, final Button... buttons) {
		event.reply(msg.format(new Object[]{})).setEphemeral(ephemeral).addActionRow(buttons).queue();
	}

	/**
	 * Convenience method for responding to a slashcommand interaction
	 * @param event The slashcommand event to reply to
	 * @param embed Sends an embed as a response
	 * @param ephemeral If the message is sent ephemeral or not
	 * @param components Adds slashcommand components to the message
	 */
	public static void reply(final SlashCommandEvent event, final MessageEmbed embed, final boolean ephemeral, final Component... components) {
		event.replyEmbeds(embed).setEphemeral(ephemeral).addActionRow(components).queue();
	}

	/**
	 * Convenience method for responding to a slashcommand interaction
	 * @param event The slashcommand event to reply to
	 * @param msg The bots' response to the user
	 * @param ephemeral If the message is sent ephemeral or not
	 * @param components Adds slashcommand components to the message
	 */
	public static void reply(final SlashCommandEvent event, final MessageFormat msg, final boolean ephemeral, final Component... components) {
		event.reply(msg.format(new Object[]{})).setEphemeral(ephemeral).addActionRow(components).queue();
	}

	/**
	 * Convenience method for responding to a slashcommand interaction
	 * @param event The slashcommand event to reply to
	 * @param embed Sends an embed as a response
	 * @param ephemeral If the message is sent ephemeral or not
	 */
	public static void reply(final SlashCommandEvent event, final MessageEmbed embed, final boolean ephemeral) {
		event.replyEmbeds(embed).setEphemeral(ephemeral).queue();
	}

	/**
	 * Convenience method for responding to a slashcommand interaction if no messageformat params are needed
	 * @param event The slashcommand event to reply to
	 * @param msg The bots' response to the user
	 * @param ephemeral If the message is sent ephemeral or not
	 */
	public static void reply(final SlashCommandEvent event, final MessageFormat msg, final boolean ephemeral) {
		event.reply(msg.format(new Object[]{})).setEphemeral(ephemeral).queue();
	}

	/**
	 * Convenience method for editing a existing message
	 * @param event Data about the button which was clicked
	 * @param msg The bots' response
	 */
	public static void edit(final ButtonClickEvent event, final MessageFormat msg) {
		event.editMessage(msg.format(new Object[]{})).setActionRows(Collections.emptyList()).setEmbeds(Collections.emptyList()).queue();
	}

	/**
	 * Convenience method for editing a existing message
	 * @param event Data about the selected choice
	 * @param msg The bots' response
	 */
	public static void edit(final SelectionMenuEvent event, final MessageFormat msg) {
		event.editMessage(msg.format(new Object[]{})).setActionRows(Collections.emptyList()).setEmbeds(Collections.emptyList()).queue();
	}
}

