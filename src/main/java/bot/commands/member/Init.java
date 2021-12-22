package bot.commands.member;

import bot.commands.SlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Locale;

public class Init extends SlashCommand {
    @Override
    public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
        if (user != null) {
            final MessageFormat msg = new MessageFormat(Responses.get("alpacaAlreadyOwned", locale));

            MessageService.queueReply(event, msg, true);
            return;
        }

        final User dev = event.getJDA().getUserById(Env.get("DEV_ID"));
        final Button btnAccept = Button.success("acceptInit", "Accept");
        final Button btnCancel = Button.danger("declineInit", "Decline");

        final MessageEmbed embed = new EmbedBuilder()
                .setTitle(Responses.get("userInformation", locale))
                .setDescription(Responses.get("initIntro", locale))
                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                .addField(Responses.get("headerStorageId", locale), Responses.get("bodyStorageId", locale),false)
                .addField(Responses.get("headerDeletionId", locale), Responses.get("bodyDeletionId", locale),false)
                .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                .setTimestamp(Instant.now())
                .build();

        MessageService.queueReply(event, embed, true, btnAccept, btnCancel);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("init", "Initializes a new alpaca");
    }

    @Override
    protected CommandType getCommandType() {
        return CommandType.USER;
    }
}
