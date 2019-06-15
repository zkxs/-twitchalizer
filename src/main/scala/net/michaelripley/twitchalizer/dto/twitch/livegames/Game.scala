package net.michaelripley.twitchalizer.dto.twitch.livegames

object Game {
  /** Making this larger than one devalues games that have a large number of channels.
    * This makes it easier to focus on niche games. Too big though and anything with more than one
    * channel will get crazy devauled.
    */
  private val factor: Double = 1.6

  /** string representation of the rank formula */
  val rankFormula: String = f"viewers / channels^$factor%.1f"
}

case class Game(viewers: Int, channels: Int, game: GameMetaData) {
  override def toString: String = f"${game.prettyName}%-50s v=$viewers%6d    c=$channels%4d    rank=$rank%7.2f"

  /** bigger rank is better */
  def rank: Double = viewers.toDouble / Math.pow(channels.toDouble, Game.factor)
}
