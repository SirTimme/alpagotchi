package bot.db;

import bot.models.Entry;
import bot.utils.Env;
import bot.utils.DocumentService;
import com.mongodb.client.*;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;

public class MongoDB implements IDatabase {
	private final MongoCollection<Document> users;

	public MongoDB() {
		final MongoClient client = MongoClients.create(Env.get("DB_URI"));
		final MongoDatabase db = client.getDatabase(Env.get("DB_NAME"));

		this.users = db.getCollection("alpacas_manager");
	}

	@Override
	public Entry getUser(final long memberID) {
		final Document result = this.users.find(eq("_id", memberID)).first();

		return result != null ? DocumentService.parseEntry(result) : null;
	}

	@Override
	public void updateUser(final Entry entry) {
		this.users.replaceOne(eq("_id", entry.getMemberID()), DocumentService.updateDocument(entry));
	}

	@Override
	public void createUser(final long memberID) {
		this.users.insertOne(DocumentService.createDocument(memberID));
	}

	@Override
	public void deleteUser(final long memberID) {
		this.users.deleteOne(eq("_id", memberID));
	}

	@Override
	public long getEntries() {
		return this.users.countDocuments();
	}
}
