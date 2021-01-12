package Bot.Outfits;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class OutfitManager {
	private final List<IOutfit> outfits = new ArrayList<>();

	public OutfitManager() {
		addOutfit(new King());
		addOutfit(new Gentleman());
		addOutfit(new Pirate());
		addOutfit(new Default());
	}

	public List<IOutfit> getOutfits() {
		return this.outfits;
	}

	@Nullable
	public IOutfit getOutfit(String search) {
		String searchLower = search.toLowerCase();

		for (IOutfit outfit : this.outfits) {
			if (outfit.getOutfitName().equals(searchLower)) {
				return outfit;
			}
		}
		return null;
	}

	private void addOutfit(IOutfit outfit) {
		outfits.add(outfit);
	}
}
