package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Config;
import Bot.Handler.CommandManager;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.File;
import java.time.Instant;
import java.util.List;

public class Help implements ICommand {
    private final CommandManager cmdManager;

    public Help(CommandManager manager) {
        this.cmdManager = manager;
    }

    @Override
    public void handle(CommandContext commandContext) {
        final List<String> args = commandContext.getArgs();
        final TextChannel channel = commandContext.getChannel();

        if (args.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder();
            StringBuilder stringBuilder = new StringBuilder();
            String prefix = IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong());
            final Member botCreator = commandContext.getGuild().getMemberById(Config.get("OWNER_ID"));
            File listIcon = new File("src/main/resources/list.png");


            embed.setTitle("Overview of all commands");

            cmdManager.getCommands()
                    .stream()
                    .filter( (cmd) -> cmd.getPermissionLevel().equals("admin"))
                    .map(ICommand::getName)
                    .sorted()
                    .forEach( (cmd) -> stringBuilder.append("`").append(prefix).append(cmd).append("`\n"));

            embed.addField("\uD83D\uDD12 Admin commands", stringBuilder.toString(),false);

            stringBuilder.setLength(0);

            cmdManager.getCommands()
                    .stream()
                    .filter(cmd -> cmd.getPermissionLevel().equals("member"))
                    .map(ICommand::getName)
                    .sorted()
                    .forEach(
                            cmd -> stringBuilder.append("`").append(prefix).append(cmd).append("`\n")
                    );

            embed.addField("\uD83D\uDD13 Member commands", stringBuilder.toString(),false);
            embed.setThumbnail("attachment://list.png");
            embed.setFooter("Created by " + botCreator.getEffectiveName(), botCreator.getUser().getEffectiveAvatarUrl());
            embed.setTimestamp(Instant.now());

            channel.sendFile(listIcon, "list.png").embed(embed.build()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = cmdManager.getCommand(search);

        if (command == null) {
            channel.sendMessage("<:RedCross:782229279312314368> No help found for " + search).queue();
            return;
        }

        channel.sendMessage(command.getHelp(commandContext)).queue();
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "help [command]\n" +
                (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") +
                "Displays further information to a specific command";
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPermissionLevel() {
        return "member";
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands");
    }
}

