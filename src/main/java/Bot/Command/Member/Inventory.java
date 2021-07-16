package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Database.IDatabase;
import Bot.Models.User;
import Bot.Shop.ItemManager;
import Bot.Utils.Emote;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.time.Instant;

public class Inventory implements ISlashCommand {
    private final ItemManager itemMan;

    public Inventory(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        User user = IDatabase.INSTANCE.getUser(authorID);

        if (user == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Inventory")
             .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/839074173459365908/inventory.png")
             .addField("__**:meat_on_bone: Hunger items**__", "These items are used to fill up the hunger of your alpaca", false)
             .setFooter("Created by SirTimme", "https://cdn.discordapp.com/avatars/483012399893577729/ba3996b7728a950565a79bd4b550b8dd.png")
             .setTimestamp(Instant.now());

        itemMan.getItems(Stat.HUNGER)
               .forEach(item -> embed.addField(":package: " + item.getName(), "Quantity: **" + user.getInventory().getItem(item.getName()) + "**", true));

        embed.addBlankField(false)
             .addField("__**:beer: Thirst items**__", "Following items replenish the thirst of your alpaca", false);

        itemMan.getItems(Stat.THIRST)
                   .forEach(item -> embed.addField(":package: " + item.getName(), "Quantity: **" + user.getInventory().getItem(item.getName()) + "**", true));

        event.replyEmbeds(embed.build()).queue();
    }
}
