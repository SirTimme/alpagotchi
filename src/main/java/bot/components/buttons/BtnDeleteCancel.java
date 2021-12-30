package bot.components.buttons;

import bot.components.IComponent;
import bot.models.GuildSettings;
import bot.utils.MessageService;
import bot.utils.Responses;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;

import java.text.MessageFormat;

public class BtnDeleteCancel implements IComponent {
    @Override
    public void execute(GenericComponentInteractionCreateEvent event, GuildSettings settings) {
        MessageService.editReply(event, new MessageFormat(Responses.get("dataCancelled", settings.getLocale())));
    }
}
