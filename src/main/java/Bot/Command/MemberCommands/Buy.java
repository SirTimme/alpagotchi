package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Shop.IShopItem;
import Bot.Database.IDataBaseManager;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Buy implements ICommand {
    private final ShopItemManager shopItemManager;

    public Buy(ShopItemManager shopItemManager) {
        this.shopItemManager = shopItemManager;
    }

    @Override
    public void handle(CommandContext commandContext) {
        final List<String> args = commandContext.getArgs();
        final long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();
        final TextChannel channel = commandContext.getChannel();
        int itemAmount = 1;

        if (args.isEmpty()) {
            channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
            return;
        }

        if (args.size() > 1) {
            try {
                itemAmount = Integer.parseInt(args.get(1));
            } catch (NumberFormatException error) {
                channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, could not resolve the amount of items").queue();
                return;
            }
        }

        if (itemAmount > 10) {
            channel.sendMessage("<:RedCross:782229279312314368> You can purchase max. 10 items at a time").queue();
            return;
        }

        IShopItem item = shopItemManager.getShopItem(args.get(0));

        if (item == null) {
            channel.sendMessage("<:RedCross:782229279312314368> No item with this name found").queue();
            return;
        }

        if (IDataBaseManager.INSTANCE.getInventory(memberID, "currency") - (item.getPrice() * itemAmount) < 0) {
            channel.sendMessage("<:RedCross:782229279312314368> Insufficient amount of fluffies").queue();
            return;
        }

        IDataBaseManager.INSTANCE.setInventory(memberID, "currency", -item.getPrice());
        IDataBaseManager.INSTANCE.setInventory(memberID, item.getName(), itemAmount);

        channel.sendMessage(":moneybag: Congratulations, you successfully bought **" + itemAmount + " " + item.getName() + "** for **" + (item.getPrice() * itemAmount) + "** fluffies").queue();
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "buy [itemname] [1-10]`\nBuys the specified amount of a item from the shop";
    }

    @Override
    public String getName() {
        return "buy";
    }
}
