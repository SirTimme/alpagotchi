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
         channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      IShopItem item = shopItemManager.getShopItem(args.get(0));

      if (item == null) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, could not resolve this item").queue();
         return;
      }

      if (IDataBaseManager.INSTANCE.getInventory(memberID, item.getName()) != 0) {
         feedAlpaca(args.get(0), memberID, item, channel);
         return;
      }

      channel.sendMessage("<:RedCross:782229279312314368> Your inventory is empty, attempting to automatically purchase a **" + item.getName() + "**...").queue(message -> {

         if (IDataBaseManager.INSTANCE.getInventory(memberID, "currency") - item.getPrice() < 0) {
            message.editMessage("<:RedCross:782229279312314368> Automatic purchase failed, insufficient amount of fluffies").queue();
            return;
         }

         try {
            Thread.sleep(1500);
         } catch (InterruptedException error) {
            error.printStackTrace();
         }

         IDataBaseManager.INSTANCE.setInventory(memberID, "currency", -item.getPrice());
         IDataBaseManager.INSTANCE.setInventory(memberID, item.getName(), 1);
         feedAlpaca(args.get(0), memberID, item, channel);

         message.editMessage("<:GreenTick:782229268914372609> Automatic purchase successful **" + item.getName() + " + 1** | **fluffies - " + item.getPrice() + "**").queue();
      });
   }

   @Override
   public String getHelp(CommandContext commandContext) {
      return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "feed [itemname]`\nFeeds your alpaca with the specified item";
   }

   @Override
   public String getName() {
      return "feed";
   }

   private void feedAlpaca(String args, long memberID, IShopItem item, TextChannel channel) {
      IDataBaseManager.INSTANCE.setInventory(memberID, item.getName(), -1);
      int oldValue = IDataBaseManager.INSTANCE.getStatus(memberID, item.getCategory());

      if (oldValue + item.getSaturation() > 100) {
         IDataBaseManager.INSTANCE.setStatus(memberID, item.getCategory(), 100 - oldValue);

      } else {
         IDataBaseManager.INSTANCE.setStatus(memberID, item.getCategory(), item.getSaturation());
      }

      if (args.equals("salad")) {
         channel.sendMessage("\uD83E\uDD57 Your alpaca consumes the green salad in one bite and is happy **Hunger + " + item.getSaturation() + "**").queue();

      } else if (args.equals("waterbottle")) {
         channel.sendMessage("\uD83D\uDCA7 Your alpaca eagerly drinks the waterbottle empty **Thirst + " + item.getSaturation() + "**").queue();

      } else {
         channel.sendMessage("\uD83D\uDD0B Your alpaca feels full of energy **Energy + " + item.getSaturation() + "**").queue();
      }
   }
}