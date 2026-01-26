package core

object AIPackageKeys {
    const val CREATURE = "Creature"
    const val PREDATOR = "Predator"
    const val PEASANT = "Peasant"
}

object TagKey {
    const val CREATURE = "Creature"
    const val COMMONER = "Commoner"
    const val FOOD = "Food"
    const val FARMABLE = "Farmable"
    const val PREDATOR = "Predator"
    const val SOUND_DESCRIPTION = "Sound Description"
    const val SOUND_LEVEL = "Sound Level"
}

object ValueKey {
    const val GOAL = "Goal"
}

object GameStateKeys {
    const val AUTO_SAVE = "autosave"
    const val AUTO_LOAD = "autoload"
    const val DEBUG_PACKAGE = "debug package"
    const val VERBOSE_STARTUP = "verbose startup"
    const val TEST_MODE = "in testing mode"
    const val TEST_SAVE_FOLDER = "use test save folder"
    const val SKIP_SAVE_STATS = "skip save stats"
    const val LAST_SAVE_GAME_NAME = "last save character name"
    const val PRINT_WITHOUT_FLUSH = "print without needing to flush histories"
}

object HowToUse {
    const val ATTACK = "Attack"
    const val EAT = "Eat"
    const val INTERACT = "Interact"
    const val SLEEP = "Sleep"
}

object FactKind {
    const val AGGRO_TARGET = "AggroTarget"
    const val HOME = "Home"
    const val LOCATION = "Location"
    const val MY_BED = "MyBed"
    const val MY_HOME = "MyHome"
    const val MY_WORKPLACE = "MyWorkplace"
    const val RECIPE = "Recipe"
    const val USE_TARGET = "UseTarget"
    const val WORK_TAGS = "WorkTags"
}

object NetworkKeys {
    const val PLAYER_START_NETWORK = "Kanbara Countryside"
    const val PLAYER_START_LOCATION = "An Open Field"
}

object ParameterKeys {
    const val COUNT = "count"
    const val MESSAGE = "message"
    const val MESSAGE_TO_OTHERS = "messageToOthers"
    const val ITEM_NAME = "itemName"
}
