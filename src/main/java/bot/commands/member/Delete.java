package bot.commands.member;

import bot.commands.ISlashCommand;
import bot.models.Entry;
import bot.utils.CommandType;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.text.MessageFormat;
import java.util.Locale;

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
        final MessageFormat msg = new MessageFormat(Responses.get("dataDeletion", locale));
        final Button btnSuccess = Button.success("btnDeleteAccept", "Accept");
        final Button btnCancel = Button.danger("btnDeleteCancel", "Cancel");

        MessageService.queueComponentReply(event, msg, true, btnSuccess, btnCancel);
    }
}
