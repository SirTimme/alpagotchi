package bot.db;

import bot.models.Entry;

public interface IDatabase {
	IDatabase INSTANCE = new MongoDB();

	Entry getUser(final long memberID);

	void updateUser(final Entry DBUser);

	void createUser(final long memberID);

	void deleteUser(final long memberID);

	long getEntries();
}
