package bot.commands.member;

import bot.commands.InfoCommand;
import bot.shop.Item;
import bot.shop.ItemManager;
import bot.utils.CommandType;
import bot.utils.Env;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.Instant;
import java.util.Comparator;
import java.util.Locale;

public class Shop extends InfoCommand {
    private final ItemManager items;

    public Shop(final ItemManager items) {
        this.items = items;
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event, final Locale locale) {
        event.getJDA().retrieveUserById(Env.get("DEV_ID")).queue(dev -> {
            final var embed = new EmbedBuilder()
                    .setTitle("Shop")
                    .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/839072735182323732/shop.png")
                    .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                    .addField("__**:meat_on_bone: Hunger items**__", "These items are used to fill up the hunger of your alpaca", false)
                    .setTimestamp(Instant.now());

            this.items.getItemsByStat("hunger")
                    .stream()
                    .sorted(Comparator.comparingInt(Item::getPrice))
                    .forEach(item -> embed.addField(
                                    ":package: " + item.getName(),
                                    "Saturation: " + item.getSaturation() + "\nPrice: " + item.getPrice(),
                                    true
                            )
                    );

            embed.addBlankField(false)
                 .addField("__**:beer: Thirst items**__", "Following items replenish the thirst of your alpaca", false);

            this.items.getItemsByStat("thirst")
                    .stream()
                    .sorted(Comparator.comparingInt(Item::getPrice))
                    .forEach(item -> embed.addField(
                            ":package: " + item.getName(),
                            "Saturation: " + item.getSaturation() + "\nPrice: " + item.getPrice(),
                            true)
                    );

            event.replyEmbeds(embed.build()).queue();
        });
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("shop", "Shows the shop with all the items to buy");
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.INFO;
    }
}