package bot.commands.member;

import bot.commands.interfaces.IStaticUserCommand;
import bot.models.Entry;
import bot.utils.Env;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.time.Instant;

import static bot.utils.Emote.REDCROSS;

public class Init implements IStaticUserCommand {
    @Override
    public void execute(SlashCommandEvent event, Entry user) {
        if (user != null) {
            event.reply(REDCROSS + " You already own an alpaca")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        final User dev = event.getJDA().getUserById(Env.get("DEV_ID"));

        final EmbedBuilder embed = new EmbedBuilder()
                .setTitle("User information")
                .setDescription("Im glad, that Alpagotchi interests you.\nHere are two important points before you can start:")
                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                .addField(
                        "__§1 Storage of the UserID__",
                        "Alpagotchi stores your personal Discord UserID in order to work, but this is public information and can be accessed by everyone.",
                        false
                )
                .addField(
                        "__§2 Deletion of the UserID__",
                        "If you change your mind about storing your UserID,\nuse the `/delete` command to delete your data at any time.",
                        false
                )
                .setFooter("Created by " + dev.getName(), dev.getAvatarUrl())
                .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build())
             .addActionRow(
                     Button.success("acceptInit", "Accept"),
                     Button.danger("declineInit", "Decline")
             )
             .setEphemeral(true)
             .queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("init", "Initializes a new alpaca");
    }
}
