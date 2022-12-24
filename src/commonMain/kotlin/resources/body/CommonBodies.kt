package resources.body

import core.body.BodyResource
import traveling.location.network.networks

class CommonBodies : BodyResource {
    override val values = networks {
        network("Human") {
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

        network("Rat") {
            locationNode("Canine Head")
            locationNode("Canine Torso") {
                isRoot(true)
                connection("Canine Head", z = 1)
                connection("Small Claws", z = -1)
            }
            locationNode("Small Claws")
        }

        network("Tree") {
            locationNode("Branches"){
                material("Wood")
            }
            locationNode("Trunk") {
                isRoot(true)
                material("Wood")
                connection("Branches", z = 15)
            }
        }

        network("City Wall") {
            locationNode("Foundation") {
                isRoot(true)
                material("Stone")
                connection("Lower Shelf", z = 1)
                connection("Scraggy Lower Shelf", x = 1, z = 1)
            }
            locationNode("Lower Shelf") {
                material("Stone")
                connection("Wall Mid", z = 1)
            }
            locationNode("Scraggy Lower Shelf") {
                material("Stone")
                connection("Wall Mid", z = 1)
            }
            locationNode("Wall Mid") {
                material("Stone")
                connection("Wall Top", z = 1)
            }
            locationNode("Wall Top"){
                material("Stone")
            }
        }

        network("Stairs")
        network("Sack")
        network("Grain Bin")
        network("Grain Chute")
        network("Sack")
        network("Wall Crack") {
            locationNode {
                isRoot(true)
                connection("Top", z = 5)
            }
            locationNode("Top")
        }
        network("Medium Container")
        network("Dagger") {
            locationNode("Handle") {
                material("Leather")
                isRoot(true)
                connection("Pommel", z = -1)
                connection("Guard", z = 1)
            }
            locationNode("Pommel") {
                material("Iron")
            }
            locationNode("Guard") {
                material("Iron")
                connection("Blade", z = 1)
            }
            locationNode("Blade") {
                material("Iron")
            }
        }
    }
}