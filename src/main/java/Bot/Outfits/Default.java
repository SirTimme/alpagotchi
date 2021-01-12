package Bot.Outfits;

public class Default implements IOutfit {

	@Override
	public String getOutfitName() {
		return "default";
	}

	@Override
	public String getOutfitURl() {
		return null;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public String getEmoji() {
		return "\uD83E\uDD99";
	}

	@Override
	public String getDescription() {
		return "standard outfit";
	}
}
