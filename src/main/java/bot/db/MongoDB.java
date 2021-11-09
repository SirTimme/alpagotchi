package bot.db;

import bot.models.Entry;
import bot.utils.Env;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.client.model.Filters.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB implements IDatabase {
	private final MongoCollection<Entry> entries;

	public MongoDB() {
		final CodecRegistry pojoRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
		final CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoRegistry);

		final MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(Env.get("DB_URI")))
				.codecRegistry(codecRegistry)
				.build();

		final MongoClient client = MongoClients.create(settings);
		final MongoDatabase db = client.getDatabase(Env.get("DB_NAME"));

		this.entries = db.getCollection("testDB", Entry.class);
	}

	@Override
	public Entry getUser(final long memberID) {
		return this.entries.find(eq("_id", memberID)).first();
	}

	@Override
	public void updateUser(final Entry entry) {
		this.entries.replaceOne(eq("_id", entry.getMemberID()), entry);
	}

	@Override
	public void createUser(final long memberID) {
		this.entries.insertOne(new Entry(memberID));
	}

	@Override
	public void deleteUser(final long memberID) {
		this.entries.deleteOne(eq("_id", memberID));
	}

	@Override
	public long getEntries() {
		return this.entries.countDocuments();
	}
}
