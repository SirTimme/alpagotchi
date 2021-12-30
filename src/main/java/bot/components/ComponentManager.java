package bot.components;

import bot.components.buttons.BtnDeleteAccept;
import bot.components.buttons.BtnDeleteCancel;
import bot.components.buttons.BtnInitAccept;
import bot.components.buttons.BtnInitCancel;
import bot.components.menus.SelectEnglish;
import bot.components.menus.SelectGerman;
import bot.db.IDatabase;
import bot.models.GuildSettings;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

import java.util.HashMap;
import java.util.Map;

public class ComponentManager {
    private final Map<String, IComponent> components;

    public ComponentManager() {
        this.components = new HashMap<>() {{
            put("selectEnglish", new SelectEnglish());
            put("selectGerman", new SelectGerman());
            put("btnDeleteAccept", new BtnDeleteAccept());
            put("btnDeleteCancel", new BtnDeleteCancel());
            put("btnInitAccept", new BtnInitAccept());
            put("btnInitCancel", new BtnInitCancel());
        }};
    }

    public void handle(final GenericComponentInteractionCreateEvent event) {
        final IComponent component = this.components.get(getKey(event));
        final GuildSettings settings = IDatabase.INSTANCE.getGuildSettings(event.getGuild().getIdLong());

        component.execute(event, settings);
    }

    private String getKey(final GenericComponentInteractionCreateEvent event) {
        return event instanceof SelectionMenuEvent ? ((SelectionMenuEvent) event).getValues().get(0) : event.getComponentId();
    }
}
