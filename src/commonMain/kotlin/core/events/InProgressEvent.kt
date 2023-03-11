package core.events

class InProgressEvent(val event: Event) {
    var timeRemaining: Int = event.gameTicks()
}