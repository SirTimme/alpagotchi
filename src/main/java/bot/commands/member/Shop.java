package bot.commands.member;

import bot.commands.interfaces.IInfoCommand;
import bot.shop.Item;
import bot.shop.ItemManager;
import bot.utils.Env;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.time.Instant;
import java.util.Comparator;

public class Shop implements IInfoCommand {
    private final ItemManager itemMan;

    public Shop(ItemManager itemMan) {
        this.itemMan = itemMan;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        final User dev = event.getJDA().getUserById(Env.get("DEV_ID"));

        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Shop")
                .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/839072735182323732/shop.png")
                .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                .addField("__**:meat_on_bone: Hunger items**__", "These items are used to fill up the hunger of your alpaca", false)
                .setTimestamp(Instant.now());

        itemMan.getItemsByStat("hunger")
               .stream()
               .sorted(Comparator.comparingInt(Item::getPrice))
               .forEach(item -> embed.addField(
                       ":package: " + item.getName(),
                       "Saturation: " + item.getSaturation() + "\nPrice: " + item.getPrice(),
                       true)
               );

        embed.addBlankField(false)
             .addField("__**:beer: Thirst items**__", "Following items replenish the thirst of your alpaca", false);

        itemMan.getItemsByStat("thirst")
               .stream()
               .sorted(Comparator.comparingInt(Item::getPrice))
               .forEach(item -> embed.addField(
                       ":package: " + item.getName(),
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
