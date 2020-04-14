# TelegramMC
Telegram Bot to manage minecraft servers

This is a Maven project :)

## Setup
1. Import as maven Project
2. Setup bot credentials in lolsalat/telegramMC/Bot.java (Username looks something like 'example_bot' and token "123467:ABCdef_asdsdasds")
3. Setup servers in settings.json:

### settings.json
*java*: Path to java executable of java11 (or higher). E.g. 'C:\Program Files\Java\jdk11\bin\java.exe' on Windows. If java11 (or higher) is in PATH, just 'java' is fine.

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
