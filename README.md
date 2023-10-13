# United Base Alert
### Summary
On the Minecraft server I founded, there was a mechanic in which players formed towns, then attacked other players' towns to gain loot. This naturally created a problem where players,
especially in other time zones, would not know they were being attacked, waking up to find their towns pillaged and destroyed. Initially, we delayed all battles until four designated hours
(10:00AM to 2:00PM PST) which worked for our largely American, Latin American, and European playerbase, but this system was unpopular and cumbersome. 

I saw that many groups of users primarily communicated with each other using private Discords. Seeking to connect these groups with the game, I created an alert system that notifies
private Discord servers associated with towns when towns are under attack or when enemy players enter their territory. This system connects towns stored in an external API to discord channels,
then alerts channels through a system of event listeners. Each account is assigned to a single user, who can change the town or Discord channel the account is connected to. 

Using this model, all players were equally aware of whether their settlement was being attacked, making our Minecraft battles equitable for players of all time zones and skill levels. 
Anecdotally, the conflicts of the server were more evenly pitched, and the suprise attacks we had to tolerate became much less effective. The private Discord communities were better integrated
with our central Discord server, making the community more cohesive.

### Reflection
This project allowed me to connect multiple areas of interest together, hosting a Discord Agent on a Java server, communicating with open-source APIs. Connecting in-game state to
communication platforms as always been an interest of mine, and this project allowed me to follow this interest.

---

### Used APIs/Technologies
 - Java
 - Maven
 - Spigot API (communicates with the Minecraft Server)
 - Towny API (communicates with the framework we used for player towns)
 - FlagWar API (communicates the the framework we used for player wars)
 - Discord API (Creates an Agent, then communicates with individual servers)
 - Gson (for storage of player registrations)
