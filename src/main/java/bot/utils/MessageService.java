package bot.utils;

import bot.db.IDatabase;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.*;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.components.Component;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.function.Consumer;

import static net.dv8tion.jda.api.requests.ErrorResponse.*;

public class MessageService {
    public static void queueReply(final SlashCommandEvent event, final String msg, final boolean ephemeral) {
        event.reply(msg).setEphemeral(ephemeral).queue(null, getHandler(event));
    }

    public static void queueReply(final SlashCommandEvent event, final MessageFormat msg, final boolean ephemeral) {
        event.reply(msg.format(new Object[]{})).setEphemeral(ephemeral).queue(null, getHandler(event));
    }

    public static void queueComponentReply(final SlashCommandEvent event, final MessageEmbed embed, final boolean ephemeral, final Component... components) {
        event.replyEmbeds(embed).setEphemeral(ephemeral).addActionRow(components).queue(null, getHandler(event));
    }

    public static void queueComponentReply(final SlashCommandEvent event, final MessageFormat msg, final boolean ephemeral, final Component... components) {
        event.reply(msg.format(new Object[]{})).setEphemeral(ephemeral).addActionRow(components).queue(null, getHandler(event));
    }

    public static void queueEmbedReply(final SlashCommandEvent event, final MessageEmbed embed, final boolean ephemeral) {
        event.replyEmbeds(embed).setEphemeral(ephemeral).queue(null, getHandler(event));
    }

    public static void queueImageReply(final SlashCommandEvent event, final MessageEmbed embed, final byte[] imgData, final boolean ephemeral) {
        event.replyEmbeds(embed).addFile(imgData, "alpagotchi.png").setEphemeral(ephemeral).queue(null, getHandler(event));
    }

    public static void completeReply(final SlashCommandEvent event, final String msg, final boolean ephemeral) {
        event.reply(msg).setEphemeral(ephemeral).complete();
    }

    public static void editReply(final GenericComponentInteractionCreateEvent event, final MessageFormat msg) {
        event.editMessage(msg.format(new Object[]{}))
             .setActionRows(Collections.emptyList())
             .setEmbeds(Collections.emptyList())
             .queue();
    }

    public static Locale getLocale(final GenericInteractionCreateEvent event) {
        return event.getGuild() == null
                ? Locale.ENGLISH
                : IDatabase.INSTANCE.getGuildSettings(event.getGuild().getIdLong()).getLocale();
    }

    private static ErrorHandler getHandler(final SlashCommandEvent event) {
        final Consumer<ErrorResponseException> action = ex -> {
            event.getChannel().sendMessage(Responses.get("commandTimeout", getLocale(event))).queue();
        };

        return new ErrorHandler().handle(UNKNOWN_INTERACTION, action);
    }
}

