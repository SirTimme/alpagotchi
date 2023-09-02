package bot.commands.member;

import bot.commands.types.UserCommand;
import bot.models.user.User;
import bot.utils.CommandType;
import bot.localization.LocalizedResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.time.Instant;
import java.util.Locale;

public class Init extends UserCommand {
    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale, final User user) {
        if (user != null) {
            event.reply(LocalizedResponse.get("init.error.alreadyOwned", locale)).setEphemeral(true).queue();
            return;
        }

        final var userId = event.getUser().getId();

        final var btnAccept = Button.success(userId + ":initAccept", LocalizedResponse.get("button.accept", locale));
        final var btnCancel = Button.danger(userId + ":initCancelled", LocalizedResponse.get("button.cancel", locale));

        final var embed = new EmbedBuilder()
                .setTitle(LocalizedResponse.get("init.embed.title", locale))
                .addField(LocalizedResponse.get("init.embed.field.title.storage", locale),
                          LocalizedResponse.get("init.embed.field.body.storage", locale),
                          false)
                .addField(LocalizedResponse.get("init.embed.field.title.deletion", locale),
                          LocalizedResponse.get("init.embed.field.body.deletion", locale),
                          false)
                .addField(LocalizedResponse.get("init.embed.field.title.agreement", locale),
                          LocalizedResponse.get("init.embed.field.body.agreement", locale),
                          false)
                .setFooter(LocalizedResponse.get("general.embed.footNote.createdBy", locale))
                .setTimestamp(Instant.now())
                .build();

        event.replyEmbeds(embed).addActionRow(btnAccept, btnCancel).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("init", "Initializes a new alpaca")
                       .setDescriptionLocalization(DiscordLocale.GERMAN, "Initialisiert ein neues Alpaka");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INIT;
    }
}