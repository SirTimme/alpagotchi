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
        int itemAmount = 0;

        if (args.isEmpty()) {
            channel.sendMessage("<:RedCross:782229279312314368> Missing Arguments").queue();
            return;
        }

        if (args.size() == 1) {
            itemAmount = 1;
        } else {
            itemAmount = Integer.parseInt(args.get(1));
        }

        if (itemAmount > 10) {
            channel.sendMessage("<:RedCross:782229279312314368> You can purchase max. 10 items at a time").queue();
            return;
        }

        IShopItem item = shopItemManager.getShopItem(args.get(0));

        if (item != null) {
            if (IDataBaseManager.INSTANCE.getInventory(memberID, "currency") - (item.getItemValue() * itemAmount) < 0) {
                channel.sendMessage("<:RedCross:782229279312314368> Insufficient amount of fluffies").queue();
                return;
            }

            IDataBaseManager.INSTANCE.setInventory(memberID, "currency", -item.getItemValue());
            IDataBaseManager.INSTANCE.setInventory(memberID, item.getItemName(), 1);

            channel.sendMessage(":moneybag: Congratulations, you successfully bought **" + itemAmount + " " + item.getItemName() + "** for **" + (item.getItemValue() * itemAmount) + "** fluffies").queue();
        } else {
            channel.sendMessage("<:RedCross:782229279312314368> No item with this name found").queue();
        }
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
