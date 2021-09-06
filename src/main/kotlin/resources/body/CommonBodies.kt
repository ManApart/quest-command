package resources.body

import core.body.BodyResource
import traveling.location.location.bodies

class CommonBodies : BodyResource {
    override val values = bodies {
        body("Human") {
            locationNode("Head")
            locationNode("Chest") {
                isRoot(true)
                connection("Head", z = 2)
                connection("Right Arm", x = 1)
                connection("Left Arm", x = -1)
                connection("Waist", z = -2)
            }
            locationNode("Right Arm") {
                connection("Right Hand", z = -3)
            }
            locationNode("Left Arm") {
                connection("Left Hand", z = -3)
            }
            locationNode("Waist") {
                connection("Right Leg", x = 1, z = -1)
                connection("Left Leg", x = -1, z = -1)
            }
            locationNode("Right Hand")
            locationNode("Left Hand")
            locationNode("Right Leg") {
                connection("Right Foot", z = -4)
            }
            locationNode("Left Leg") {
                connection("Left Foot", z = -4)
            }
            locationNode("Right Foot")
            locationNode("Left Foot")
        }

        body("Rat") {
            locationNode("Canine Head")
            locationNode("Canine Body") {
                isRoot(true)
                connection("Canine Head", z = 1)
                connection("Small Claws", z = -1)
            }
            locationNode("Small Claws")
        }

        body("Tree") {
            locationNode("Branches")
            locationNode("Trunk") {
                isRoot(true)
                connection("Branches", z = 15)
            }
        }

        body("City Wall") {
            locationNode("Foundation") {
                isRoot(true)
                connection("Lower Shelf", z = 1)
                connection("Scraggy Lower Shelf", x = 1, z = 1)
            }
            locationNode("Lower Shelf") {
                connection("Wall Mid", z = 1)
            }
            locationNode("Scraggy Lower Shelf") {
                connection("Wall Mid", z = 1)
            }
            locationNode("Wall Mid") {
                connection("Wall Top", z = 1)
            }
            locationNode("Wall Top")
        }

        body("Stairs")
        body("Grain Bin")
        body("Sack")
        body("Grain Chute") {
            locationNode {
                isRoot(true)
            }
        }
        body("Sack") {
            locationNode {
                isRoot(true)
            }
        }
    }
}