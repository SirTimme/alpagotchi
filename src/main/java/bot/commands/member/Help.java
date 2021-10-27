package bot.commands.member;

import bot.commands.IInfoCommand;
import bot.commands.SlashCommandManager;
import bot.utils.Env;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.time.Instant;

public class Help implements IInfoCommand {
    private final SlashCommandManager slashCmdMan;

    public Help(SlashCommandManager slashCmdMan) {
        this.slashCmdMan = slashCmdMan;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        final User dev = event.getJDA().getUserById(Env.get("DEV_ID"));

        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Overview of all commands")
                .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/836542447186214942/avatar.png")
                .addField("Commands", slashCmdMan.getCommandsString(), true)
                .addField("Need further help or found a bug?", "Join the [Alpagotchi Support](https://discord.gg/DXtYyzGhXR) server!", false)
                .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build()).queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("help", "Shows all commands of Alpagotchi");
    }
}
