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
		for (IOutfit outfit : this.outfits) {
			if (outfit.getName().equals(search)) {
				return outfit;
			}
		}
		return null;
	}

	private void addOutfit(IOutfit outfit) {
		this.outfits.add(outfit);
	}
}
