package Bot.Command.Commands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.io.File;
import java.time.Instant;
import java.util.List;

public class MyAlpacaCommand implements ICommand {

    @Override
    public void handle(CommandContext commandContext) {
        File alpacaFile = new File("C:\\Users\\timpi\\Documents\\GitHub\\Alpaca\\src\\main\\resources\\alpaka.gif");
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Member botCreator = commandContext.getGuild().getMemberById(Config.get("OWNER_ID"));

        embedBuilder.setTitle("" + commandContext.getMember().getEffectiveName() + "'s Alpaca");
        embedBuilder.addField("Hunger \uD83C\uDF56", "100/100", true);
        embedBuilder.addField("Thirst \uD83C\uDF7C", "100/100", true);
        embedBuilder.addField("Energy \uD83D\uDD0B", "100/100", true);
        embedBuilder.setThumbnail(commandContext.getMember().getUser().getAvatarUrl());
        embedBuilder.setFooter("Created by " + botCreator.getEffectiveName(), botCreator.getUser().getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setImage("attachment://alpaca.gif");

        commandContext.getChannel().sendFile(alpacaFile, "alpaca.gif").embed(embedBuilder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "`Usage: a!myalpaca`\nShows the stats of your alpaca";
    }

    @Override
    public String getName() {
        return "myalpaca";
    }

    @Override
    public List<String> getAliases() {
        return List.of("alpaca", "stats");
    }
}
