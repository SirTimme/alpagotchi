package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.components.buttons.BtnInitAccept;
import bot.components.buttons.BtnInitCancel;
import bot.components.buttons.ButtonManager;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.Env;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

public class Init implements ISlashCommand {
    @Override
    public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
        if (user != null) {
            MessageService.queueReply(event, new MessageFormat(Responses.get("alpacaAlreadyOwned", locale)), true);
            return;
        }

        final Button btnAccept = Button.success(UUID.randomUUID().toString(), "Accept");
        final Button btnCancel = Button.danger(UUID.randomUUID().toString(), "Decline");

        ButtonManager.addButton(btnAccept.getId(), new BtnInitAccept());
        ButtonManager.addButton(btnCancel.getId(), new BtnInitCancel());

        event.getJDA().retrieveUserById(Env.get("DEV_ID")).queue(dev -> {
            final MessageEmbed embed = new EmbedBuilder()
                    .setTitle(Responses.get("userInformation", locale))
                    .setDescription(Responses.get("initIntro", locale))
                    .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                    .addField(Responses.get("headerStorageId", locale), Responses.get("bodyStorageId", locale),false)
                    .addField(Responses.get("headerDeletionId", locale), Responses.get("bodyDeletionId", locale),false)
                    .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                    .setTimestamp(Instant.now())
                    .build();

            MessageService.queueComponentReply(event, embed, true, btnAccept, btnCancel);
        });
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("init", "Initializes a new alpaca");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INIT;
    }
}
