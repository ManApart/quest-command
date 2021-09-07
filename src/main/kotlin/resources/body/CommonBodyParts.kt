package resources.body

import core.body.BodyPartResource
import core.body.BodyResource
import traveling.location.location.bodies
import traveling.location.location.locations

class CommonBodyParts : BodyPartResource {
    override val values = locations {
        location("Head") {
            slot("Head Inner", "Head", "Head Outer")
        }

        location("Chest") {
            slot("Chest Inner", "Chest", "Chest Outer")
        }

        location("Right Arm") {
            slot("Right Arm Inner", "Right Arm", "Right Arm Outer")
        }

        location("Left Arm") {
            slot("Left Arm Inner", "Left Arm", "Left Arm Outer")
        }

        location("Right Hand") {
            slot("Right Hand", "Right Hand Grip")
        }

        location("Left Hand") {
            slot("Left Hand", "Left Hand Grip")
        }

        location("Waist") {
            slot("Waist Inner", "Waist", "Waist Outer", "Belt Front", "BeltLeft", "Belt Right", "Belt Back")
        }

        location("Right Leg") {
            slot("Right Leg Inner", "Right Leg", "Right Leg Outer")
        }


        location("Left Leg") {
            slot("Left Leg Inner", "Left Leg", "Left Leg Outer")
        }


        location("Right Foot") {
            slot("Right Foot")
        }

        location("Left Foot") {
            slot("Left Leg")
        }


        location("Canine Head") {
            slot("Canine Head")
        }

        location("Canine Body") {
            slot("Canine Body")
        }

        location("Small Claws") {
            slot("Small Claws")
        }

        location("Grain Chute") {
            props {
                tag("Open", "Container")
                value("size", 3)
            }
        }

        location("Grain Bin") {
            props {
                tag("Open","Container")
                value("size", 3)
                value("CanHold", "Grounded")
            }
        }

        location("Sack") {
            props {
                tag("Container")
                value("weight", 1)
                value("defense", 1)
            }
        }
    }
}