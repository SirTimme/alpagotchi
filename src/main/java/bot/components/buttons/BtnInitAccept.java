package bot.components.buttons;

import bot.components.IComponent;
import bot.db.IDatabase;
import bot.models.GuildSettings;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;

import java.text.MessageFormat;

public class BtnInitAccept implements IComponent {
    @Override
    public void execute(GenericComponentInteractionCreateEvent event, GuildSettings settings) {
        IDatabase.INSTANCE.createUser(event.getUser().getIdLong());

        MessageService.editReply(event, new MessageFormat(Responses.get("initSuccess", settings.getLocale())));
    }
}
