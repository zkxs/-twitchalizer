package net.michaelripley.twitchalizer.dto.twitch.livegames

import com.fasterxml.jackson.annotation.JsonProperty

case class LiveGames(
  @JsonProperty("_total") total: Int,
  @JsonProperty("follows") games: Array[Game]
)
