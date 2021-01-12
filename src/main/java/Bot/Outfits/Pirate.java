package Bot.Outfits;

public class Pirate implements IOutfit {

	@Override
	public String getOutfitName() {
		return "pirate";
	}

	@Override
	public String getOutfitURl() {
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
	public String getEmoji() {
		return "\uD83C\uDFF4\u200D";
	}

	@Override
	public String getDescription() {
		return "find the hidden treasure as a pirate";
	}
}
