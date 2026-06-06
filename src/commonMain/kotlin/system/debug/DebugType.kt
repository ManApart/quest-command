package system.debug

enum class DebugType(val commandName: String, val description: String) {
    LEVEL_REQ("levelreq", "Ignore Level Requirements"),
    STAT_CHANGES("statchanges", "Ignore Stat Changes"),
    CLARITY("clarity", "Total Clarity"),
    RANDOM_SUCCEED("random", "Ignore Random"),
    RANDOM_FAIL("randomfail", "Random Always Fails"),
    RANDOM_RESPONSE("randomresp", "Random Returns Number"),
    DISPLAY_UPDATES("displayupdates", "Display Updates"),
    MAP_SHOW_ALL_LOCATIONS("map", "Show All Map Locations"),
    RECIPE_SHOW_ALL("recipe", "Show All Recipes"),
    VERBOSE_AI("aiupdates", "Verbose AI"),
    VERBOSE_EVENT_QUEUE("vevents", "Verbose Event Queue"),
    VERBOSE_TIME("vtime", "Verbose Time"),
    VERBOSE_WEATHER("vweather", "Verbose Weather"),
    POLL_CONNECTION("poll", "Poll server on cadence for updates"),
    SHOW_PROMPT("prompt", "Show command prompt"),
}
