package resources.body

import core.body.BodyResource
import traveling.location.location.bodies

class CommonBodies : BodyResource {
    override val values = bodies {
        body("Human") {
            location("Head")
            location("Chest") {
                isRoot(true)
                connection("Head", z = 2)
                connection("Right Arm", x = 1)
                connection("Left Arm", x = -1)
                connection("Waist", z = -2)
            }
            location("Right Arm") {
                connection("Right Hand", z = -3)
            }
            location("Left Arm") {
                connection("Left Hand", z = -3)
            }
            location("Waist") {
                connection("Right Leg", x = 1, z = -1)
                connection("Left Leg", x = -1, z = -1)
            }
            location("Right Hand")
            location("Left Hand")
            location("Right Leg") {
                connection("Right Foot", z = -4)
            }
            location("Left Leg") {
                connection("Left Foot", z = -4)
            }
            location("Right Foot")
            location("Left Foot")
        }

        body("Rat") {
            location("Canine Head")
            location("Canine Body") {
                isRoot(true)
                connection("Canine Head", z = 1)
                connection("Small Claws", z = -1)
            }
            location("Small Claws")
        }

        body("Tree") {
            location("Branches")
            location("Trunk") {
                isRoot(true)
                connection("Branches", z = 15)
            }
        }

        body("City Wall") {
            location("Foundation") {
                isRoot(true)
                connection("Lower Shelf", z = 1)
                connection("Scraggy Lower Shelf", x = 1, z = 1)
            }
            location("Lower Shelf") {
                connection("Wall Mid", z = 1)
            }
            location("Scraggy Lower Shelf") {
                connection("Wall Mid", z = 1)
            }
            location("Wall Mid") {
                connection("Wall Top", z = 1)
            }
            location("Wall Top")
        }

        body("Stairs")
        body("Grain Bin")
        body("Sack")
    }
}