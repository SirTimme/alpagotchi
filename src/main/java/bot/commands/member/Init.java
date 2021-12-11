package bot.commands.member;

import bot.commands.interfaces.IStaticUserCommand;
import bot.models.Entry;
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

public class Init implements IStaticUserCommand {
    @Override
    public void execute(final SlashCommandEvent event, final Entry user, final Locale locale) {
        if (user != null) {
            MessageService.reply(event, new MessageFormat(Responses.get("alpacaAlreadyOwned", locale)), true);
            return;
        }

        final User dev = event.getJDA().getUserById(Env.get("DEV_ID"));
        final Button accept = Button.success("acceptInit", "Accept");
        final Button cancel = Button.danger("declineInit", "Decline");

        final MessageEmbed embed = new EmbedBuilder()
                .setTitle(Responses.get("userInformation", locale))
                .setDescription(Responses.get("initIntro", locale))
                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                .addField(Responses.get("headerStorageId", locale), Responses.get("bodyStorageId", locale),false)
                .addField(Responses.get("headerDeletionId", locale), Responses.get("bodyDeletionId", locale),false)
                .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                .setTimestamp(Instant.now())
                .build();

        MessageService.reply(event, embed, true, accept, cancel);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("init", "Initializes a new alpaca");
    }
}
