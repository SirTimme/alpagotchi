package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.Shop.IShopItem;
import Bot.Database.IDataBaseManager;
import Bot.ShopItemManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class BuyCommand implements ICommand {
    private final ShopItemManager shopItemManager;

    public BuyCommand(ShopItemManager shopItemManager) {
        this.shopItemManager = shopItemManager;
    }

    @Override
    public void handle(CommandContext commandContext) {
        final List<String> args = commandContext.getArgs();
        final long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();
        final TextChannel channel = commandContext.getChannel();

        if (args.isEmpty()) {
            channel.sendMessage("<:RedCross:782229279312314368> Missing Arguments").queue();
            return;
        }

        IShopItem item = shopItemManager.getShopItem(args.get(0));

        if (item != null) {
            if (Integer.parseInt(IDataBaseManager.INSTANCE.getAlpacaStats(memberID, "currency")) - 10 < 0) {
                channel.sendMessage("<:RedCross:782229279312314368> Insufficient amount of fluffies").queue();
                return;
            }

            IDataBaseManager.INSTANCE.setAlpacaStats(memberID, String.valueOf(10), "currency", "subtract");

            channel.sendMessage("\uD83D\uDCB2 Congratulations, you successfully bought a `" + item.getItemName() + "` for `" + item.getItemValue() + "` fluffies").queue();
        } else {
            channel.sendMessage("<:RedCross:782229279312314368> No shop item with this name found").queue();
        }
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "buy [itemname]`\nBuys a specifiy item from the shop";
    }

    @Override
    public String getName() {
        return "buy";
    }
}
