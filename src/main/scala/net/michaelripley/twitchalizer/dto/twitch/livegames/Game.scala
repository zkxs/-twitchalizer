package net.michaelripley.twitchalizer.dto.twitch.livegames

case class Game(viewers: Int, channels: Int, game: GameMetaData) {
  override def toString: String = f"${game.name}%-50s v=$viewers%5d    c=$channels%4d    v/c=$viewersPerChannel%6.2f"
  def viewersPerChannel: Double = viewers.toDouble / channels.toDouble
}
