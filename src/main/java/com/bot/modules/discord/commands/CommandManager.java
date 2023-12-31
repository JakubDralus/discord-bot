package com.bot.modules.discord.commands;

import com.bot.modules.discord.commands.admin.RandomSong;
import com.bot.modules.discord.commands.admin.Echo;
import com.bot.modules.discord.commands.other.Help;
import com.bot.modules.discord.commands.other.Info;
import com.bot.modules.discord.commands.other.Twitter;
import com.bot.modules.discord.commands.music.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CommandManager extends ListenerAdapter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);
    private Map<String, ISlashCommand> commandsMap;
    
    public CommandManager() {
        //...
        commandMapper();
    }
    
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("command manager ready");
    }
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        
        ISlashCommand command;
        if ((command = commandsMap.get(commandName)) != null) {
            command.execute(event);
        }
    }
    
    private void commandMapper() {
        commandsMap = new ConcurrentHashMap<>();
        
        //Admin Commands
        commandsMap.put("random-song", new RandomSong());
        commandsMap.put("echo", new Echo());
        
        // other
        commandsMap.put("info", new Info());
        commandsMap.put("twitter", new Twitter());
        commandsMap.put("help", new Help());
        
        //Music Commands
        commandsMap.put("play", new Play());
        commandsMap.put("queue", new Queue());
        commandsMap.put("clear-queue", new ClearQueue());
        commandsMap.put("skip", new Skip());
        commandsMap.put("pause", new Pause());
        commandsMap.put("resume", new Resume());
        commandsMap.put("leave", new Leave());
        commandsMap.put("now-playing", new NowPlaying());
        commandsMap.put("shuffle-queue", new Shuffle());
        commandsMap.put("play-ratpartymix", new RatPartyMix());
        commandsMap.put("play-daily-song", new DailySong());
        commandsMap.put("yeahbuddy", new YeahBuddy());
    }
}
