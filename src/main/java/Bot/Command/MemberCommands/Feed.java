package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Feed implements ICommand {
    private final ShopItemManager shopItemManager;

    public Feed(ShopItemManager shopItemManager) {
        this.shopItemManager = shopItemManager;
    }

    @Override
    public void handle(CommandContext commandContext) {
        final TextChannel channel = commandContext.getChannel();
        final long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();
        final List<String> args = commandContext.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("<:RedCross:782229279312314368> Missing Arguments").queue();
            return;
        }

        IShopItem item = shopItemManager.getShopItem(args.get(0));

        if (item != null) {
            if (IDataBaseManager.INSTANCE.getInventory(memberID, item.getItemName()) == 0) {
                channel.sendMessage("<:RedCross:782229279312314368> Your inventory doesnt contain this specified item").queue();
                return;
            }
            IDataBaseManager.INSTANCE.setInventory(memberID, item.getItemName(), -1);
            int oldValue = IDataBaseManager.INSTANCE.getAlpaca(memberID, item.getItemCategory());

            if (oldValue + 10 > 100) {
                IDataBaseManager.INSTANCE.setAlpaca(memberID, item.getItemCategory(), 100 - oldValue);
            } else {
                IDataBaseManager.INSTANCE.setAlpaca(memberID, item.getItemCategory(), 10);
            }

            if (args.get(0).equals("salad")) {
                channel.sendMessage("\uD83E\uDD57 Your alpaca consumes the green salad in one bite and is happy **Hunger + 10**").queue();
            } else if (args.get(0).equals("waterbottle")) {
                channel.sendMessage("\uD83D\uDCA7 Your alpaca eagerly drinks the waterbottle empty **Thirst + 10**").queue();
            } else {
                channel.sendMessage("\uD83D\uDD0B Your alpaca feels full of energy **Energy + 10**").queue();
            }
        } else {
            channel.sendMessage("<:RedCross:782229279312314368> No item with this name found").queue();
        }
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "feed [itemname]`\nFeeds your alpaca with the specified item";
    }

    @Override
    public String getName() {
        return "feed";
    }
}
