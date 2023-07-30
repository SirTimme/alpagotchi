<h1 align="center">Alpagotchi | Alpaca + Tamagotchi</h1>

<h4 align="center">
  <a href="https://alpagotchi.github.io" target="_blank">Alpagotchi Website</a> |
  <a href="https://discord.com/api/oauth2/authorize?client_id=780910199875567616&permissions=265216&scope=bot%20applications.commands" target="_blank">Invite Link</a> |
  <a href="https://discord.gg/DXtYyzGhXR" target="_blank">Support Discord</a>
</h4>

<h3 align="center">Alpagotchi allows any user to create and take care of their own digital alpaca.</h3>

<div align="center">
    <img alt="Alpagotchi" src="src/main/resources/assets/showcase.png" />
</div>

<h2 align=center>Commands overview</h2>

| Usage                         | Task                                            |
|-------------------------------|-------------------------------------------------|
| buy [itemName] [1-10]         | Buys the amount of items from the shop          |
| feed [itemName] [1-5]         | Feeds the alpaca with the item                  |
| gift [@user] [itemName] [1-5] | Gifts the amount of items to the mentioned user |
| help                          | Displays all commands of Alpagotchi             |
| inventory                     | Shows the bought items from the shop            |
| alpaca                        | Shows your alpaca and his stats                 |
| nick [nickname]               | Gives your alpaca a nickname                    |
| pet                           | Increases joy of your alpaca                    |
| shop                          | Displays all items which can be bought          |
| balance                       | Shows your balance of fluffies                  |
| work                          | Work to earn a random amount of fluffies        |
| sleep [minutes]               | Let your alpaca sleep and regenerates energy    |
| outfit [outfit]               | Change the appearance of your alpaca            |
| delete                        | Deletes all of your stored data                 |
| init                          | Initialize your alpaca in the database          |
| language                      | Sets the bots' language within a guild          |

<h2 align=center>Self-hosting</h2>

Alpagotchi provides a docker image to host it yourself. The docker image can be found on [dockerhub](https://hub.docker.com/r/alpagotchi/discord-bot).\
The `docker-compose` file below configures all required services. The `adminer` service is optional and just provides a GUI for the postgres database.
````yml
version: '3.8'

name: alpagotchi
services:
  database:
    container_name: database
    image: postgres
    restart: on-failure
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB}
    volumes:
      - pg-data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  bot:
    container_name: bot
    image: alpagotchi/discord-bot
    depends_on:
      - database
    restart: on-failure
    environment:
      TOKEN: ${TOKEN}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_URL: ${POSTGRES_URL}

  adminer:
    container_name: adminer
    image: adminer
    depends_on:
      - database
    restart: on-failure
    environment:
      ADMINER_DESIGN: pepa-linha-dark
    ports:
      - "8080:8080"

volumes:
  pg-data:
    name: pg-data
````
The following directory structure is needed for Alpagotchi to be able to run:
````
/
├── docker-compose.yml
└── .env
````
The `.env` file needs the following entries:
````
TOKEN=                  // The token obtained at the bot page at the discord developer portal
POSTGRES_USER=          // The username of the postgres user
POSTGRES_PASSWORD=      // The password of the postgres user
POSTGRES_URL=           // The whole jdbc url of the postgres database
POSTGRES_DB=            // The name of the database
````