package com.example;

import com.example.modules.discord.Listener;
import com.example.modules.discord.commands.Dupa;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;


// todo make this shitty class cleaner
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    
    public static void main(String[] args) throws InterruptedException {
        
        EnumSet<GatewayIntent> intents = EnumSet.of(
                // Enables MessageReceivedEvent for guild (also known as servers)
                GatewayIntent.GUILD_MESSAGES,
                // Enables the event for private channels (also known as direct messages)
                GatewayIntent.DIRECT_MESSAGES,
                // Enables access to message.getContentRaw()
                GatewayIntent.MESSAGE_CONTENT,
                // Enables MessageReactionAddEvent for guild
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                // Enables MessageReactionAddEvent for private channels
                GatewayIntent.DIRECT_MESSAGE_REACTIONS
        );
        
        String discordToken = args[0];
        
        // start the bot
        JDA jda = JDABuilder.createDefault(discordToken, intents)
                .addEventListeners(new Listener())
                .setActivity(Activity.watching("waiting for a prompt"))
                .addEventListeners(new Dupa())
                .build();
    
        jda.getRestPing().queue(ping ->
                // shows ping in milliseconds
                LOGGER.info("Logged in with ping: " + ping)
        );
    
        // If you want to access the cache, you can use awaitReady() to block the main thread until the jda instance is fully loaded
        jda.awaitReady();
        
        
        // Spotify
        //Playlist.getPlaylistsItems_Async();
        
    }
}