# DiscordBot
## Prioritized Features Checklist

- [ ] Ticket System
- [ ] Levelsystem
- [ ] Welcome / Leave
- [ ] Announcements
- [ ] Baning
- [ ] Muting
- [ ] Kicking
- [ ] Warns
- [ ] Giveaways

## Prioritized Features in detail
### Ticket System:
- Embed with Logo, Header, Text (Config or Command?) with Message Components to click (one or more...?)
- Ticket´s Names: Name-ID
- On Opening: New Channel in specified section, automatic message with Message Component to close (Text in Config)
- On Close: Confirmation message with Message Components (enable, text, etc. in config), message that the ticket was closed. Config setting if users can close tickets?
- Logs for tickets.

### Levelsystem
  - On special amount of messages a message in a specified channel (config). Message also configurable.
level 1: 5 messages, level 2: 25 messages, level 3: 50 messages, level 4: 75 messages, etc.
-> Database needed?

### Welcome / Leave
- Message in specified channel (config) (configurable message).

### Announcements
- Specified channel (config
- Write in with commands (Header, Text)

### Baning
- Command: !ban <user> (or slashcommand)
- Confirmation: Configurable message (placeholder) in chat (specfic channel, or sender´s configurable)
-> Ban user from Server

### Muting
- Command: !mute <user> <reason> <time> (or slashcommand)
- Confirmation: Configurable message (placeholder) in chat (specfic channel, or sender´s configurable)
-> Mute with a role
-> Database, create role?

### Kicking
- Command: !ban <user> (or slashcommand)
- Confirmation: Configurable message (placeholder) in chat (specfic channel, or sender´s configurable)
-> Kick user from Server

### Warns
- Command: !warn @User (Grund) (Zeit) (or slashcommand)
- Info to user per DM: Message configurable
- 3 Warns = mute for configurable time
- 5 Warns = kick
- Confirmation: Configurable message (placeholder) in chat (specfic channel, or sender´s configurable)

### Giveaways
- command: !giveaway (or slashcommand) -> questions? (channel, time what, winners amount) -> confirmation message -> giveaway into a channel
-> After time a winner (database -> table with infos, check by reaction?)
