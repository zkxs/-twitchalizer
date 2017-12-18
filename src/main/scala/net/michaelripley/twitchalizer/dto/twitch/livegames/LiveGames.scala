package net.michaelripley.twitchalizer.dto.twitch.livegames

case class LiveGames(
  _total: Int,
  follows: Seq[Game]
)
