package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.components.buttons.BtnDeleteAccept;
import bot.components.buttons.BtnDeleteCancel;
import bot.components.buttons.ButtonManager;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.UUID;

public class Delete implements ISlashCommand {
    @Override
    public CommandData getCommandData() {
        return new CommandData("delete", "Deletes your personal data");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }

    @Override
    public void execute(final SlashCommandEvent event, final Locale locale, final Entry user) {
        final Button btnSuccess = Button.success(UUID.randomUUID().toString(), "Accept");
        final Button btnCancel = Button.danger(UUID.randomUUID().toString(), "Cancel");

        ButtonManager.addButton(btnSuccess.getId(), new BtnDeleteAccept());
        ButtonManager.addButton(btnCancel.getId(), new BtnDeleteCancel());

        MessageService.queueComponentReply(event,
                                           new MessageFormat(Responses.get("dataDeletion", locale)),
                                           true,
                                           btnSuccess,
                                           btnCancel
        );
    }
}
