package bot.buttons;

import bot.buttons.deletion.DeleteAccept;
import bot.buttons.deletion.DeleteCancel;
import bot.buttons.initialization.InitAccept;
import bot.buttons.initialization.InitCancel;
import bot.db.IDatabase;
import bot.models.GuildSettings;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ButtonManager {
    private final Map<String, IButton> buttons;

    public ButtonManager() {
        this.buttons = new HashMap<>() {{
            put("acceptInit", new InitAccept());
            put("declineInit", new InitCancel());
            put("acceptDelete", new DeleteAccept());
            put("cancelDelete", new DeleteCancel());
        }};
    }

    public void handle(ButtonClickEvent event) {
        final GuildSettings settings = IDatabase.INSTANCE.getGuildSettings(event.getGuild().getIdLong());
        final Locale locale = settings.getLocale();

        this.buttons.get(event.getComponentId()).execute(event, locale);
    }
}
