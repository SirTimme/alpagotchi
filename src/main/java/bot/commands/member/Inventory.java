package bot.commands.member;

import bot.commands.IUserCommand;
import bot.models.Entry;
import bot.shop.ItemManager;
import bot.utils.Env;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.time.Instant;

import static bot.utils.Language.SINGULAR;

public class Inventory implements IUserCommand {
    private final ItemManager itemMan;

    public Inventory(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public Entry execute(SlashCommandEvent event, Entry user) {
        final User dev = event.getJDA().getUserById(Env.get("DEV_ID"));

        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Inventory")
                .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/839074173459365908/inventory.png")
                .addField("__**:meat_on_bone: Hunger items**__", "These items are used to fill up the hunger of your alpaca", false)
                .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                .setTimestamp(Instant.now());

        itemMan.getItems("hunger")
               .forEach(item -> embed.addField(
                       ":package: " + item.getName(SINGULAR),
                       "Quantity: **" + user.getItem(item.getName(SINGULAR)) + "**",
                       true)
               );

        embed.addBlankField(false).addField(
                "__**:beer: Thirst items**__",
                "Following items replenish the thirst of your alpaca",
                false
        );

        itemMan.getItems("thirst").forEach(item -> embed.addField(
                ":package: " + item.getName(SINGULAR),
                "Quantity: **" + user.getItem(item.getName(SINGULAR)) + "**",
                true)
        );

        event.replyEmbeds(embed.build()).queue();

        return null;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("inventory", "Shows your items for your alpaca");
    }
}
