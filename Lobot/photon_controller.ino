int greenPin = D7;
int redPin = D6;
int bluePin = D5;
int goldPin = D4;

void setup() {
  pinMode(greenPin, OUTPUT);
  pinMode(redPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  pinMode(goldPin, OUTPUT);
  
  
  Spark.function("ledGreen", greenSwitch);
  Spark.function("ledRed", redSwitch);
  Spark.function("ledBlue", blueSwitch);
  Spark.function("ledGold", goldSwitch);
  Spark.function("ledOff", offSwitch);
}


int greenSwitch(String command)
{
    if (command.equalsIgnoreCase("on"))
    {
        digitalWrite(greenPin, HIGH);
        digitalWrite(redPin, LOW);
        digitalWrite(bluePin, LOW);
        digitalWrite(goldPin, LOW);
        return 1;
    }
    return -1;
}

int redSwitch(String command)
{
    if (command.equalsIgnoreCase("on"))
    {
        digitalWrite(greenPin, LOW);
        digitalWrite(redPin, HIGH);
        digitalWrite(bluePin, LOW);
        digitalWrite(goldPin, LOW);
        return 1;
    }
    return -1;
}

int blueSwitch(String command)
{
    if (command.equalsIgnoreCase("on"))
    {
        digitalWrite(greenPin, LOW);
        digitalWrite(redPin, LOW);
        digitalWrite(bluePin, HIGH);
        digitalWrite(goldPin, LOW);
        return 1;
    }
    return -1;
}

int goldSwitch(String command)
{
    if (command.equalsIgnoreCase("on"))
    {
        digitalWrite(greenPin, LOW);
        digitalWrite(redPin, LOW);
        digitalWrite(bluePin, LOW);
        digitalWrite(goldPin, HIGH);
        return 1;
    }
    return -1;
}

int offSwitch(String command)
{
    if (command.equalsIgnoreCase("off"))
    {
        digitalWrite(greenPin, LOW);
        digitalWrite(redPin, LOW);
        digitalWrite(bluePin, LOW);
        digitalWrite(goldPin, LOW);
        return 1;
    }
    return -1;
}