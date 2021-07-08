package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Shop.Item;
import Bot.Shop.ItemManager;
import Bot.Utils.Stat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.time.Instant;
import java.util.Comparator;

public class Shop implements ISlashCommand {
    private final ItemManager itemMan;

    public Shop(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        final EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Shop")
             .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/839072735182323732/shop.png")
             .setFooter("Created by SirTimme", "https://cdn.discordapp.com/avatars/483012399893577729/ba3996b7728a950565a79bd4b550b8dd.png")
             .addField("__**:meat_on_bone: Hunger items**__", "These items are used to fill up the hunger of your alpaca", false)
             .setTimestamp(Instant.now());

        itemMan.getItems(Stat.HUNGER)
               .stream()
               .sorted(Comparator.comparingInt(Item::getPrice))
               .forEach(item -> embed.addField(":package: " + item.getName(), "Saturation: " + item.getSaturation() + "\nPrice: " + item.getPrice(), true));

        embed.addBlankField(false)
             .addField("__**:beer: Thirst items**__", "Following items replenish the thirst of your alpaca", false);

        itemMan.getItems(Stat.THIRST)
               .stream()
               .sorted(Comparator.comparingInt(Item::getPrice))
               .forEach(item -> embed.addField(":package: " + item.getName(), "Saturation: " + item.getSaturation() + "\nPrice: " + item.getPrice(), true));

        event.replyEmbeds(embed.build()).queue();
    }
}
