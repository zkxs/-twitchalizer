package net.michaelripley.twitchalizer.dto.twitch.livegames;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {
    private int viewers;
    private int channels;
    @JsonProperty("game") private GameMetaData metadata;

    public int getViewers() {
        return viewers;
    }

    public int getChannels() {
        return channels;
    }

    public GameMetaData getMetadata() {
        return metadata;
    }
}
