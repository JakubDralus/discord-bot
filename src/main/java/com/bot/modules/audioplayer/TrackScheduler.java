package com.bot.modules.audioplayer;

import com.bot.shared.Util;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Getter
@Setter
public class TrackScheduler extends AudioEventAdapter {
    
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private boolean isRepeat = false;
    
    protected SlashCommandInteractionEvent event;
    
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }
    
    public void queueTrack(AudioTrack track, boolean reply) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
            
            if (reply && !event.isAcknowledged()) {
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("Added to queue: ")
                        .setDescription(track.getInfo().title + "\n")
                        .build()
                ).queue();
            }
        }
    }
    
    public void fastQueueTrack(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }
    
    public void clearQueue() {
        queue.clear();
    }
    
    public void shuffleQueue() {
        List<AudioTrack> trackList = new ArrayList<>(queue);
        Collections.shuffle(trackList);
        queue.clear();
        queue.addAll(trackList);
    }
    
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        if (!event.isAcknowledged()) {
//            event.replyEmbeds(new EmbedBuilder()
//                    .setTitle("Now playing: ")
//                    .setDescription(track.getInfo().title + "\n")
//                    .appendDescription(Util.durationFormat(track.getDuration()/1000))
//                    .setThumbnail("https://img.youtube.com/vi/" + track.getIdentifier() + "/hqdefault.jpg") // icon
//                    .build())
//            .queue();
            Util.displayCurrentPlayingTrackEmbed(event, false);
        }
    }
    
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (isRepeat) {
            player.startTrack(track.makeClone(), false);
        }
        else {
            player.startTrack(queue.poll(), false);
        }
    }
}
