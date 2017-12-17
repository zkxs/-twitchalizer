package net.michaelripley.twitchalizer.dto.twitch.livegames;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LiveGames {
    @JsonProperty("_total") private int total;
    @JsonProperty("follows") private Game[] games;

    public int getTotal() {
        return total;
    }

    public Game[] getGames() {
        return games;
    }
}
