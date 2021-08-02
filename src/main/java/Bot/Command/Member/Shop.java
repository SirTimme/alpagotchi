package Bot.Command.Member;

import Bot.Command.IInfoCommand;
import Bot.Shop.Item;
import Bot.Shop.ItemManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.time.Instant;
import java.util.Comparator;

import static Bot.Utils.Language.SINGULAR;

public class Shop implements IInfoCommand {
    private final ItemManager itemMan;

    public Shop(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Shop")
                .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/839072735182323732/shop.png")
                .setFooter("Created by SirTimme", "https://cdn.discordapp.com/avatars/483012399893577729/ba3996b7728a950565a79bd4b550b8dd.png")
                .addField("__**:meat_on_bone: Hunger items**__", "These items are used to fill up the hunger of your alpaca", false)
                .setTimestamp(Instant.now());

        itemMan.getItems("hunger")
               .stream()
               .sorted(Comparator.comparingInt(Item::getPrice))
               .forEach(item -> embed.addField(
                       ":package: " + item.getName(SINGULAR),
                       "Saturation: " + item.getSaturation() + "\nPrice: " + item.getPrice(),
                       true)
               );

        embed.addBlankField(false)
             .addField("__**:beer: Thirst items**__", "Following items replenish the thirst of your alpaca", false);

        itemMan.getItems("thirst")
               .stream()
               .sorted(Comparator.comparingInt(Item::getPrice))
               .forEach(item -> embed.addField(
                       ":package: " + item.getName(SINGULAR),
                       "Saturation: " + item.getSaturation() + "\nPrice: " + item.getPrice(),
                       true)
               );

        event.replyEmbeds(embed.build()).queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("shop", "Shows the shop with all the items to buy");
    }
}
