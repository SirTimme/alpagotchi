package Bot.Outfits;

public class Gentleman implements IOutfit {

	@Override
	public String getName() {
		return "gentleman";
	}

	@Override
	public String getImgUrl() {
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
	public String getDescription() {
		return "show your best manners with this";
	}
}
