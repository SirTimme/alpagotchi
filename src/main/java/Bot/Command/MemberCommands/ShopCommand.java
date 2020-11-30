package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.time.Instant;

public class ShopCommand implements ICommand {

    @Override
    public void handle(CommandContext commandContext) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        final Member botCreator = commandContext.getGuild().getMemberById(Config.get("OWNER_ID"));

        embedBuilder.setTitle("Alpaca Shop");
        embedBuilder.addField("\uD83E\uDE99 10 - Salad", "A green salad, nothing special (Hunger + 10)", false);
        embedBuilder.setFooter("Created by " + botCreator.getEffectiveName(), botCreator.getUser().getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());

        commandContext.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "shop`\nShows the shop to buy things for your alpaca";
    }

    @Override
    public String getName() {
        return "shop";
    }
}
