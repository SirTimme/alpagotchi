package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import Bot.Shop.IShopItem;
import Bot.Shop.ShopItemManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Gift implements ICommand {
   private final ShopItemManager shopItemManager;

   public Gift(ShopItemManager shopItemManager) {
      this.shopItemManager = shopItemManager;
   }

   @Override
   public void handle(CommandContext commandContext) {
      final TextChannel channel = commandContext.getChannel();
      final List<String> args = commandContext.getArgs();
      final long donaterUserID = commandContext.getAuthor().getIdLong();
      int giftedItemAmount;

      if (args.isEmpty() || args.size() < 3) {
         channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      if (commandContext.getMessage().getMentionedUsers().isEmpty()) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, could not resolve the mentioned user").queue();
         return;
      }

      final long giftedUserID = commandContext.getMessage().getMentionedUsers().get(0).getIdLong();

      if (giftedUserID == donaterUserID) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, you can not gift yourself items").queue();
         return;
      }

      IShopItem giftedItem = shopItemManager.getShopItem(args.get(1));

      if (giftedItem == null) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, could not resolve the specified item").queue();
         return;
      }

      try {
         giftedItemAmount = Integer.parseInt(args.get(2));
      } catch (NumberFormatException error) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, could not resolve the amount of items").queue();
         return;
      }

      if (giftedItemAmount > 5) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, you can gift max. 5 items at a time").queue();
         return;
      }

      if (IDataBaseManager.INSTANCE.getInventory(donaterUserID, giftedItem.getName()) - giftedItemAmount < 0) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, u dont own that many items to gift").queue();
         return;
      }

      IDataBaseManager.INSTANCE.setInventory(donaterUserID, giftedItem.getName(), -giftedItemAmount);
      IDataBaseManager.INSTANCE.setInventory(giftedUserID, giftedItem.getName(), giftedItemAmount);

      channel.sendMessage("\uD83C\uDF81 You successfully gifted **" + giftedItemAmount + " " +  giftedItem.getName() + "** to **" + commandContext.getJDA().getUserById(giftedUserID).getName() + "**").queue();
   }

   @Override
   public String getHelp(CommandContext commandContext) {
      return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "gift [@User] [itemname] [1-5]`\nGifts the mentioned user the specified amount of items";
   }

   @Override
   public String getName() {
      return "gift";
   }
}
