package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Command.SlashCommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.time.Instant;

public class Help implements ISlashCommand {
    private final SlashCommandManager slashCmdMan;

    public Help(SlashCommandManager slashCmdMan) {
        this.slashCmdMan = slashCmdMan;
    }

    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        final EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Overview of all commands")
             .setDescription("Further information to any command:\n**```fix\n/help (command)\n```**")
             .setThumbnail("https://cdn.discordapp.com/attachments/795637300661977132/836542447186214942/avatar.png")
             .addField("Commands", slashCmdMan.getCommandsAsString(), true)
             .addField("Need further help or found a bug?", "Join the [Alpagotchi Support](https://discord.gg/DXtYyzGhXR) server!", false)
             .setFooter("Created by SirTimme", "https://cdn.discordapp.com/avatars/483012399893577729/ba3996b7728a950565a79bd4b550b8dd.png")
             .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build()).queue();
    }
}
