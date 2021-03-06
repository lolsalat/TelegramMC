# TelegramMC
Telegram Bot to manage minecraft servers

This is a Maven project :)

# TODO
* [x] start server
* [x] stop server
* [x] save server to git
* [x] load server from git
* [x] execute commands (command output missing yet)
* [ ] help
* [ ] authentication (passwords + whitelist for certain commands)
* [ ] list servers
* [ ] add server
* [ ] remove server
* [ ] read server log
* [ ] server stats (we could itegrate the query api)
* [ ] remote controll PC (shutdown etc. This requires authentication to be implemented though)

## Setup from Release
0. Git must be installed and in path, else *save* and *load* won't work
1. download stuff
2. Setup servers, java and bot credentials in settings.json (see below)
3. Start jar with java version 11 or higher

## Setup from Source
0. Git must be installed and in path, else *save* and *load* won't work
1. Import as maven Project
2. Setup bot credentials in lolsalat/telegramMC/Bot.java (Username looks something like 'example_bot' and token "123467:ABCdef_asdsdasds") or set them up in settings.json
3. Setup servers in settings.json:

### settings.json
*bot*: Your Telegram bot name (can also be set in Java code. Json overwrites Java setting)

*token*: Your Telegram bot token (can also be set in Java code. Json overwrites Java setting)

*java*: Path to java executable of Java8. E.g. 'C:\Program Files\Java\jdk8\bin\java.exe' on Windows. If java8 is in PATH, just 'java' is fine. Note that it kinda has to be Java8 exspecially for older minecraft versions, else they will just crash.

*servers*: List of Server configuration Objects
* *name*: Name of server (in Telegram Bot)
* *cmd*: Additional commandline arguments (added directly after java)
* *path*: Path to server jar. NOTE: this file's parent directory must be within the git repository for the *save* and *load* commands to work

## commands:
* server <name> cmd <minecraft command>
  --> executes command in server <name>
* server <name> start
  --> starts server <name>
* server <name> stop
  --> stops server <name>
* server <name> save
  --> pushes server <name> to git
* server <name> load
  --> pulls server <name> from git
