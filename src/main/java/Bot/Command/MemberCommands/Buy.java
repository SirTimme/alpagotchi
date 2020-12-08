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

        if (args.isEmpty()) {
            channel.sendMessage("<:RedCross:782229279312314368> Missing Arguments").queue();
            return;
        }

        IShopItem item = shopItemManager.getShopItem(args.get(0));

        if (item != null) {
            if (IDataBaseManager.INSTANCE.getInventory(memberID, "currency") - item.getItemValue() < 0) {
                channel.sendMessage("<:RedCross:782229279312314368> Insufficient amount of fluffies").queue();
                return;
            }

            IDataBaseManager.INSTANCE.setInventory(memberID, "currency", -item.getItemValue());
            IDataBaseManager.INSTANCE.setInventory(memberID, item.getItemName(), 1);

            channel.sendMessage(":moneybag: Congratulations, you successfully bought a **" + item.getItemName() + "** for **" + item.getItemValue() + "** fluffies").queue();
        } else {
            channel.sendMessage("<:RedCross:782229279312314368> No item with this name found").queue();
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
