package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.io.File;
import java.time.Instant;
import java.util.List;

public class MyAlpaca implements ICommand {

    @Override
    public void handle(CommandContext commandContext) {
        final File myAlpacaFile = new File("src/main/resources/myAlpaca.gif");
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final Member botCreator = commandContext.getGuild().getMemberById(Config.get("OWNER_ID"));
        final long memberID = commandContext.getMessage().getAuthor().getIdLong();
        final int hunger = IDataBaseManager.INSTANCE.getStatus(memberID, "hunger");
        final int thirst = IDataBaseManager.INSTANCE.getStatus(memberID, "thirst");
        final int energy = IDataBaseManager.INSTANCE.getStatus(memberID, "energy");

        embedBuilder.setTitle("" + IDataBaseManager.INSTANCE.getNickname(memberID) + "");
        embedBuilder.addField("Hunger \uD83C\uDF56",  hunger + "/100", true);
        embedBuilder.addField("Thirst \uD83C\uDF7C",  thirst + "/100", true);
        embedBuilder.addField("Energy \uD83D\uDD0B",  energy + "/100", true);
        embedBuilder.setThumbnail(commandContext.getMember().getUser().getAvatarUrl());
        embedBuilder.setFooter("Created by " + botCreator.getEffectiveName(), botCreator.getUser().getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setImage("attachment://myAlpaca.gif");

        if ((hunger + thirst + energy) / 3 >= 66) {
            embedBuilder.setColor(Color.GREEN);

        } else if ((hunger + thirst + energy) / 3 >= 33) {
            embedBuilder.setColor(Color.YELLOW);

        } else {
            embedBuilder.setColor(Color.RED);
        }

        commandContext.getChannel().sendFile(myAlpacaFile, "myAlpaca.gif").embed(embedBuilder.build()).queue();
    }

    @Override
    public String getHelp(String prefix) {
        return "`Usage: " + prefix + "myalpaca\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Shows the stats of your alpaca";
    }

    @Override
    public String getName() {
        return "myalpaca";
    }

    @Override
    public String getPermissionLevel() {
        return "member";
    }

    @Override
    public List<String> getAliases() {
        return List.of("ma", "stats", "alpaca");
    }
}
