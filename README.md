# Honey

Framework for plugins that extends Minecraft in a new way
## What is Honey?

Honey consists of two elements. A mod and a plugin for servers. The mod is quite boring for the user as it can't do
anything without the plugin. In combination with the plugin, however, Honey can create the impossible! For example,
developers can use Honey to create their own GUIs (Graphical User Interfaces), i.e. their own inventories, mini-games,
menus, simply everything! Honey makes it possible for new elements to suddenly appear on your screen in your Minecraft
game. However, Honey does not do this itself. Any developer can use Honey to create their own screens and do whatever
they want with them. Honey only serves as a framework here, since Minecraft does not normally allow a server to open
custom screens, or even create them at all. However, Honey adds this functionality, and also provides enough security so
that developers cannot inject malware via the server (this is absolutely not possible, since Honey only serves as a
bridge between server and client. The developer creates on the server a Gui, passes this on to Honey, and Honey then
displays this Gui to a user). However, Honey also has other functions. For some minigames you have to find out which
keys a user presses. This is unfortunately not possible in Minecraft, but Honey makes it possible! (**There is a risk
here: since all keys pressed are sent to the server, do not enter passwords or other sensitive data**!). However, so
that such a risk does not become too great, the user is informed in advance which data the server wants to access and
must confirm this.

Now you know what honey is, and **the best thing is to just try it**! Start creating your own guis now and experience a
lot of new functions in the Minecraft server plugin development!

## Missing features / under development

| Feature                                 | Status | Note                                                      
|-----------------------------------------|--------|-----------------------------------------------------------|
| Custom Guis                             | ❌      | It works, but not without errors                          |
| Communication between Server and Client | ✅      | Communication with Packets                                |
| Honey Packet System                     | ✅      | Packet System for communication between client & server   |
| Gui Elements & Widgets                  | 🟡     | Basic Elements done, but there will be more in the future |
| Custom Toast System                     | ✅      | Server can send custom Toasts to a client                 |
| Automatic Client and Server System      | ✅      | Automatic Client Startup on Server join                   |
| Honey Server Detection                  | ❌      | Detect whether a server uses the honey plugin or not      |
| Event API for Devs                      | ❌      | Always adding some Events                                 |
| Player specification                    | ✅      | Server can specify which player should receive packets    |
| Data Warning                            | ❌      | Client is informed about data access by server            |
| Key Data                                | ❌      | Sending pressed keys to the server                        |
| Crash & Reload Detection                | ❌      | Detect when the server restarts, crashes or reloads       
| Individual Language System              | ✅      | Individual language for every user                        

- ❌ Not implemented
- 🟡 Under development
- ✅ Done

## Version

|  Minecraft Version| Forge Mod  | Fabric Mod | Plugin
|--|--|--|--|
|  1.20.1 | ❌ |🟡 |🟡 

No Forge Mod is currently being developed, since Fabric is mostly used in newer Minecraft versions. A forge mod is not
planned yet.

- ❌ Not implemented
- 🟡 Under development
- ✅ Done

## Contributors

- TheWebcode