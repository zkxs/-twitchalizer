package net.michaelripley.twitchalizer.dto.twitch.livegames

private object GameMetaData {
  /** used to map stupid game names in to less stupid game names */
  private val nameMap = Map(
    "Cadence of Hyrule: Crypt of the NecroDancer Featuring The Legend of Zelda" -> "Cadence of Hyrule",
    "Earth Defense Force 4.1: The Shadow of New Despair" -> "Earth Defense Force 4.1",
    "ABZÛ" -> "ABZU", // all the windows terminals I've seen shit themselves on 'Û'
  ).withDefault(identity)

}

import GameMetaData._

case class GameMetaData(name: String) {
  def prettyName: String = nameMap(name)
}
