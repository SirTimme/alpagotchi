package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.PermissionLevel;
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
      final String prefix = IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong());

      if (args.isEmpty()) {
         EmbedBuilder embed = new EmbedBuilder();
         final Member botCreator = commandContext.getGuild().getMemberById(Config.get("OWNER_ID"));
         File listIcon = new File("src/main/resources/list.png");

         embed.setTitle("Overview of all commands");

         embed.addField("\uD83D\uDD27 Developer commands", getCommandsByPerms(prefix, PermissionLevel.DEVELOPER), false);

         embed.addField("\uD83D\uDC6E Admin commands", getCommandsByPerms(prefix, PermissionLevel.ADMIN), false);

         embed.addField("\uD83D\uDD13 Member commands", getCommandsByPerms(prefix, PermissionLevel.MEMBER), false);

         embed.setThumbnail("attachment://list.png");
         embed.setFooter("Created by " + botCreator.getEffectiveName(), botCreator.getUser().getEffectiveAvatarUrl());
         embed.setTimestamp(Instant.now());

         channel.sendFile(listIcon, "list.png").embed(embed.build()).queue();
         return;
      }

      ICommand command = cmdManager.getCommand(args.get(0));

      if (command == null) {
         channel.sendMessage("<:RedCross:782229279312314368> No help found").queue();
         return;
      }

      channel.sendMessage(command.getHelp(prefix)).queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "help [command]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Displays further information to a specific command";
   }

   @Override
   public String getName() {
      return "help";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.MEMBER;
   }

   @Override
   public List<String> getAliases() {
      return List.of("commands");
   }

   private String getCommandsByPerms(String prefix, Enum<PermissionLevel> permLevel) {
      StringBuilder stringBuilder = new StringBuilder();

      cmdManager.getCommands()
              .stream()
              .filter((cmd) -> cmd.getPermissionLevel() == permLevel)
              .map(ICommand::getName)
              .sorted()
              .forEach((cmd) -> stringBuilder.append("`").append(prefix).append(cmd).append("`\n"));

      return stringBuilder.toString();
   }
}

