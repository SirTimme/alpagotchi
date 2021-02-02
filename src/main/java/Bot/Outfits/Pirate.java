package Bot.Outfits;

public class Pirate implements IOutfit {

	@Override
	public String getName() {
		return "pirate";
	}

	@Override
	public String getImgUrl() {
		return "src/main/resources/outfits/pirate.png";
	}

	@Override
	public int getX() {
		return 211;
	}

	@Override
	public int getY() {
		return 45;
	}

	@Override
	public String getDescription() {
		return "find the hidden treasure as a pirate";
	}
}
