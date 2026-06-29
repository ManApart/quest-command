package resources.body

import core.body.BodyPartResource
import core.properties.TagStrings.CONTAINER
import core.properties.TagStrings.OPEN
import crafting.material.MaterialStrings.CLOTH
import crafting.material.MaterialStrings.FLESH
import crafting.material.MaterialStrings.STONE
import traveling.location.location.locations

class CommonBodyParts : BodyPartResource {
    override val values = locations {
        location("Head") {
            material(FLESH)
            slot("Head Inner", "Head", "Head Outer")
        }

        location("Chest") {
            material(FLESH)
            slot("Chest Inner", "Chest", "Chest Outer")
        }

        location("Right Arm") {
            material(FLESH)
            slot("Right Arm Inner", "Right Arm", "Right Arm Outer")
        }

        location("Left Arm") {
            material(FLESH)
            slot("Left Arm Inner", "Left Arm", "Left Arm Outer")
        }

        location("Right Hand") {
            material(FLESH)
            slot("Right Hand", "Right Hand Grip")
        }

        location("Left Hand") {
            material(FLESH)
            slot("Left Hand", "Left Hand Grip")
        }

        location("Waist") {
            material(FLESH)
            slot("Waist Inner", "Waist", "Waist Outer", "Belt Front", "BeltLeft", "Belt Right", "Belt Back")
        }

        location("Right Leg") {
            material(FLESH)
            slot("Right Leg Inner", "Right Leg", "Right Leg Outer")
        }


        location("Left Leg") {
            material(FLESH)
            slot("Left Leg Inner", "Left Leg", "Left Leg Outer")
        }


        location("Right Foot") {
            material(FLESH)
            slot("Right Foot")
        }

        location("Left Foot") {
            material(FLESH)
            slot("Left Leg")
        }

        location("Canine Head") {
            material(FLESH)
            slot("Canine Head")
        }

        location("Canine Body") {
            material(FLESH)
            slot("Canine Body")
        }

        location("Small Claws") {
            material(FLESH)
            slot("Small Claws")
        }

        location("Grain Chute") {
            material(STONE)
            props {
                tag(OPEN, CONTAINER)
                value("size", 3)
            }
        }

        location("Grain Bin") {
            material(STONE)
            props {
                tag(OPEN,CONTAINER)
                value("size", 3)
                value("CanHold", "Grounded")
            }
        }

        location("Sack") {
            material(CLOTH)
            props {
                tag(CONTAINER)
                value("weight", 1)
                value("defense", 1)
            }
        }

        location("Medium Container") {
            props {
                tag(CONTAINER, OPEN)
            }
        }
    }
}
