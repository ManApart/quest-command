package resources.body

import core.body.BodyPartResource
import core.properties.TagKey.CONTAINER
import core.properties.TagKey.OPEN
import traveling.location.location.locations

class CommonBodyParts : BodyPartResource {
    override val values = locations {
        location("Head") {
            material("Flesh")
            slot("Head Inner", "Head", "Head Outer")
        }

        location("Chest") {
            material("Flesh")
            slot("Chest Inner", "Chest", "Chest Outer")
        }

        location("Right Arm") {
            material("Flesh")
            slot("Right Arm Inner", "Right Arm", "Right Arm Outer")
        }

        location("Left Arm") {
            material("Flesh")
            slot("Left Arm Inner", "Left Arm", "Left Arm Outer")
        }

        location("Right Hand") {
            material("Flesh")
            slot("Right Hand", "Right Hand Grip")
        }

        location("Left Hand") {
            material("Flesh")
            slot("Left Hand", "Left Hand Grip")
        }

        location("Waist") {
            material("Flesh")
            slot("Waist Inner", "Waist", "Waist Outer", "Belt Front", "BeltLeft", "Belt Right", "Belt Back")
        }

        location("Right Leg") {
            material("Flesh")
            slot("Right Leg Inner", "Right Leg", "Right Leg Outer")
        }


        location("Left Leg") {
            material("Flesh")
            slot("Left Leg Inner", "Left Leg", "Left Leg Outer")
        }


        location("Right Foot") {
            material("Flesh")
            slot("Right Foot")
        }

        location("Left Foot") {
            material("Flesh")
            slot("Left Leg")
        }

        location("Canine Head") {
            material("Flesh")
            slot("Canine Head")
        }

        location("Canine Body") {
            material("Flesh")
            slot("Canine Body")
        }

        location("Small Claws") {
            material("Flesh")
            slot("Small Claws")
        }

        location("Grain Chute") {
            material("Stone")
            props {
                tag("Open", "Container")
                value("size", 3)
            }
        }

        location("Grain Bin") {
            material("Stone")
            props {
                tag("Open","Container")
                value("size", 3)
                value("CanHold", "Grounded")
            }
        }

        location("Sack") {
            material("Cloth")
            props {
                tag("Container")
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