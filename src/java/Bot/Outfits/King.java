package Bot.Outfits;

public class King implements IOutfit {

	@Override
	public String getName() {
		return "king";
	}

	@Override
	public String getImgUrl() {
		return "src/resources/outfits/king.png";
	}

	@Override
	public int getX() {
		return 212;
	}

	@Override
	public int getY() {
		return 55;
	}

	@Override
	public String getDescription() {
		return "rule over the world with this stylish outfit";
	}
}
