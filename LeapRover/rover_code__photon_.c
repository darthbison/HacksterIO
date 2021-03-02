// This #include statement was automatically added by the Particle IDE.
#include "SoftPWM/SoftPWM.h"

// This #include statement was automatically added by the Particle IDE.
#include "HC_SR04/HC_SR04.h"

// This #include statement was automatically added by the Particle IDE.
#include "PhoBot/PhoBot.h"


//Declare instance of the PhoBot library
PhoBot p = PhoBot();

void setup() {

   Spark.function("control", control);
}

int control(String command) {
    return p.control(command);
}