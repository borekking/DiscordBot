# DiscordBot
## Prioritized Features Checklist

- [ ] Ticket System
- [ ] Levelsystem
- [x] Welcome / Leave
- [x] Announcements
- [x] Baning
- [x] Muting
- [x] Kicking
- [ ] Warns
- [ ] Giveaways
- [x] Reload Command
- [x] Configurable Activity + Activity Command
- [ ] Permission System

## Prioritized Features in detail
### Ticket System:
- Embed with Logo, Header, Text with Message Components to click (one or more...?)
- Ticket´s Names: Name-ID -> Command
- On Opening: New Channel in specified section, automatic message with Message Component to close (Text in Config)
- On Close: Confirmation message with Message Components (enable, text, etc. in config), message that the ticket was closed. Config setting if users can close tickets?
- Logs for tickets.

### Levelsystem (Database)
  - On special amount of messages a message in a specified channel (config). Message also configurable.
level 1: 5 messages, level 2: 25 messages, level 3: 50 messages, level 4: 75 messages, etc.
-> Database needed?

### Welcome / Leave
- Message in specified channel (config) (configurable message).

### Announcements
- Write in specified channel with commands (Header, Text, Channel, (Role to ping))

### Baning
- Command: /ban [user] [reason]
- Confirmation: Configurable message (placeholder) in chat (specific channel, or sender´s configurable)
-> Ban user from Server

### Muting (Database)
- Command: /mute [user] [reason] [time]
- Confirmation: Configurable message (placeholder) in chat (specific channel, or sender´s configurable)
-> Mute with a role
-> Database, create role?

### Kicking
- Command: /kick [user] [reason]
- Confirmation: Configurable message (placeholder) in chat (specific channel, or sender´s configurable)
-> Kick user from Server

### Warns
- Command: /warn [user] [reason]
- Info to user per DM: Message configurable
- 3 Warns = mute for configurable time
- 5 Warns = kick
- Confirmation: Configurable message (placeholder) in chat (specific channel, or sender´s configurable)

### Giveaways (Database)
- command: /giveaway -> questions: (channel, time what, winners amount) -> confirmation message -> giveaway into a channel
-> After time a winner (database -> table with information, check by reaction?)

### (Config-) Reload
- reload config

### Activity 
- Configurable Activity or maybe also "listening to", etc.
- Command to change Activity

### Permission System
- Add PermissionSystem:
- Saved in Database
- You can define roles with a command: "role create <name> [<permission_1>, <permission_2>, ...]"
- You can remove roles "roles: role remove <name>"
- You can list all roles: "role list"
- Permissions:
  - Strings
  - Documented List for all permissions 
