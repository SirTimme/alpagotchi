package Bot.Outfits;

public class Gentleman implements IOutfit {

	@Override
	public String getOutfitName() {
		return "gentleman";
	}

	@Override
	public String getOutfitURl() {
		return "src/main/resources/outfits/gentleman.png";
	}

	@Override
	public int getX() {
		return 229;
	}

	@Override
	public int getY() {
		return 30;
	}

	@Override
	public String getEmoji() {
		return "\uD83C\uDFA9";
	}

	@Override
	public String getDescription() {
		return "show your best manners with this";
	}
}
