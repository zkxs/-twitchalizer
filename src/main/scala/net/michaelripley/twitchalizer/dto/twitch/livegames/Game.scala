package net.michaelripley.twitchalizer.dto.twitch.livegames

import com.fasterxml.jackson.annotation.JsonProperty

case class Game(
   viewers: Int,
   channels: Int,
   @JsonProperty("game") metadata: GameMetaData
 )
