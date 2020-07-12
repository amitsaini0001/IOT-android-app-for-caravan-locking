
#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>

BLECharacteristic *pCharacteristic;
bool deviceConnected = false;
bool pleasechangepass = false;
bool alarming = false;
bool lock ; //CAN USE THIS VARIABLE TO OUTSOURCE COMMAND TO OTHER DEVICE PARTS EASILY
char unlockpass1[2] = "0";/////
char lockpass1[2] = "1";///////
char resetpass1[2] = "2";//////             LOGICAL COMMANDS FOR DIFFERENT OPERANDS.
char forgotpass1[2] = "3";/////
char dissableAlarm1[2]= "4";/////
char dissableAlarm30[2]= "5";/////

char hashpass[7] = "2315";//DEFAULT PASSWORD
char fakealarm[7] = "9876";//FAKE ALARAM PASSWORD
char temphashpass[7] = "0000";//TEMP PASSWORD CHAR USED WHILE CHANGING PASSWORDS
char uniquereset[7] = "#8*6";//UNIQUDE ID/PASSWORD FOR RESETTING THE PASSWORD
char forgotpass[7] = "1234";//DEFAULT PASSWORD SET AFTER THE FORGOT PASSWORD REQUEST
char rstpass[4];
float txValue = 0;//TX VALUE{TO BE USED FOR SENDING DATA IN FUTURE}
const int readPin = 32; // ESP32 READPIN
const int LED = 22; // ESP32 OUT PIN FOR LOCK --{IN THIS CASE A LIGHT}
const int LED1 = 23;//ESP32 OUT PIN FOR CONFIRMATION SOUND/LIGHT --{IN THIS CASE A LIGHT}

//std::string rxValue; // Could also make this a global var to access it in loop()

// CAN GENERATE UNIQUE UUID'S AT:
// https://www.uuidgenerator.net/

#define SERVICE_UUID           "6E400001-B5A3-F393-E0A9-E50E24DCCA9E" // UART service UUID
#define CHARACTERISTIC_UUID_RX "6E400002-B5A3-F393-E0A9-E50E24DCCA9E"
#define CHARACTERISTIC_UUID_TX "6E400003-B5A3-F393-E0A9-E50E24DCCA9E"

class MyServerCallbacks: public BLEServerCallbacks {
    void onConnect(BLEServer* pServer) {
      deviceConnected = true;
    };

    void onDisconnect(BLEServer* pServer) {
      deviceConnected = false;
    };
};

class MyCallbacks: public BLECharacteristicCallbacks {
    void onWrite(BLECharacteristic *pCharacteristic) {

      std::string rxValue = pCharacteristic->getValue();

      if (rxValue.length() > 0) {
        Serial.print("*********");


        int a = rxValue.length();
        Serial.println("Rx Length: ");
        Serial.print(a);



        if (a == 5) {//limiting incoming characters to 5 only
          for (int i = 0; i < rxValue.length(); i++) {
            Serial.println("Value recieved ");
            Serial.print(rxValue[i]);

            
          }
          Serial.println("rxvalueon4: ");
          Serial.print(rxValue[4]);

          // This particular if clause is to LOCKING the WULI device and show the confirmation via LED flashing
          if (hashpass[0] == rxValue[0] && hashpass[1] == rxValue[1] && hashpass[2] == rxValue[2] && hashpass[3] == rxValue[3] && rxValue[4] == lockpass1[0]) {
            Serial.println("Locking!");
            digitalWrite(LED1, HIGH);
            delay(500);
            digitalWrite(LED1, LOW);
            delay(500);
            digitalWrite(LED, HIGH);
            lock = true;
            Serial.print("Lock Status: ");
            Serial.println(lock);

            

            // This particular if clause is to UNLOCKING the WULI device and show the confirmation via LED flashing
          } else if (hashpass[0] == rxValue[0] && hashpass[1] == rxValue[1] && hashpass[2] == rxValue[2] && hashpass[3] == rxValue[3] && rxValue[4] == unlockpass1[0] ) {
            Serial.println("Unlocking!");
            digitalWrite(LED1, HIGH);
            delay(500);
            digitalWrite(LED1, LOW);
            delay(500);
            digitalWrite(LED, LOW);
            lock = false;
            Serial.print("Lock Status: ");
            Serial.println(lock);

            // This particular if clause is to RESETING the WULI device and show the confirmation via LED flashing
          } else if (uniquereset[0] == rxValue[0] && uniquereset[1] == rxValue[1] && uniquereset[2] == rxValue[2] && uniquereset[3] == rxValue[3] && rxValue[4] == forgotpass1[0]) {
            Serial.println("Reseting Password");
            digitalWrite(LED1, HIGH);
            delay(200);
            digitalWrite(LED1, LOW);
            delay(200);
            digitalWrite(LED1, HIGH);
            delay(200);
            digitalWrite(LED1, LOW);
            delay(100);
            digitalWrite(LED1, HIGH);
            delay(500);
            digitalWrite(LED1, LOW);
            delay(500);
            digitalWrite(LED1, HIGH);
            delay(500);
            digitalWrite(LED1, LOW);
            lock = 0;
            lock = 0;
            hashpass[0] = forgotpass[0];
            hashpass[1] = forgotpass[1];
            hashpass[2] = forgotpass[2];
            hashpass[3] = forgotpass[3];


          }else if (fakealarm[0] == rxValue[0] && fakealarm[1] == rxValue[1] && fakealarm[2] == rxValue[2] && fakealarm[3] == rxValue[3] && rxValue[4] == dissableAlarm1[0]) {
            Serial.println("Enabling Alarm!");
            alarming = true;
            
            
            
            
            }else if (hashpass[0] == rxValue[0] && hashpass[1] == rxValue[1] && hashpass[2] == rxValue[2] && hashpass[3] == rxValue[3] && rxValue[4] == dissableAlarm1[0] ) {
            Serial.println("Disabling Alarm!");
            alarming = false;
            
            digitalWrite(LED1, HIGH);
            delay(2000);
            digitalWrite(LED1, LOW);
            delay(1000);
            digitalWrite(LED1, HIGH);
            delay(2000);
            digitalWrite(LED1, LOW);
            
            Serial.print("Lock Status: ");
            Serial.println(lock);
            
            
            
            
            }}else if (a == 9) {//limiting incoming characters to 9 only
          Serial.print("Recieved Value for chngps: ");
          for (int i = 0; i < rxValue.length(); i++) {
            Serial.print(rxValue[i]);
          }
          Serial.println("*******");
          Serial.print("Rx on 7: ");
          Serial.println(rxValue[6]);


           // This particular if clause is to CHANGING the WULI device's password and show the confirmation via LED flashing
          if (hashpass[0] == rxValue[0] && hashpass[1] == rxValue[1] && hashpass[2] == rxValue[2] && hashpass[3] == rxValue[3] && rxValue[4] == resetpass1[0]) {

            Serial.println("CHANGING PASSWORD ");
          digitalWrite(LED1, HIGH);
          delay(200);
          digitalWrite(LED1, LOW);
          delay(200);
          digitalWrite(LED1, HIGH);
          delay (200);
          digitalWrite(LED1, LOW);
          delay (200);
          digitalWrite(LED1, HIGH);
          delay (1500);
          digitalWrite(LED1, LOW);

          //SAVING AS TEMP PASSWORD
          temphashpass[0] = rxValue[5];
            Serial.println("~temphashpas0- rxval5");
            temphashpass[1] = rxValue[6];
            Serial.println("~temphashpas1- rxval6");
            temphashpass[2] = rxValue[7];
            Serial.println("~temphashpas2- rxval7");
            temphashpass[3] = rxValue[8];
            Serial.println("~temphashpas3- rxval8");

            pleasechangepass = true;

          }

        } else {
          Serial.print("Invalid Command recieved from Application");

        }
      }
    }

};

//swapping temp pass with the original one
void changeps() {
  for (int i = 0; i < 7; i++) {
    
    hashpass[i] = temphashpass[i];
    
    }pleasechangepass = false;
    }
    
    
    


void setup() {
  Serial.begin(115200);

  pinMode(LED, OUTPUT);
  pinMode(LED1, OUTPUT);

  // Create the BLE Device
  BLEDevice::init("Wuli Test device"); // Give it a name

  // Create the BLE Server
  BLEServer *pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallbacks());

  // Create the BLE Service
  BLEService *pService = pServer->createService(SERVICE_UUID);

  // Create a BLE Characteristic
  pCharacteristic = pService->createCharacteristic(
                      CHARACTERISTIC_UUID_TX,
                      BLECharacteristic::PROPERTY_NOTIFY
                    );

  pCharacteristic->addDescriptor(new BLE2902());

  BLECharacteristic *pCharacteristic = pService->createCharacteristic(
                                         CHARACTERISTIC_UUID_RX,
                                         BLECharacteristic::PROPERTY_WRITE
                                       );

  pCharacteristic->setCallbacks(new MyCallbacks());

  // Start the service
  pService->start();

  // Start advertising
  pServer->getAdvertising()->start();
  Serial.println("Waiting a client connection to notify...");
}

void loop() {
  if (pleasechangepass == true) {
    changeps();
  }else if(alarming ==true){
    digitalWrite(LED1, HIGH);
    delay(200);
    digitalWrite(LED1, LOW);
    delay(200);
    digitalWrite(LED1, HIGH);
    delay (200);
    digitalWrite(LED1, LOW);
    delay (200);
    digitalWrite(LED1, HIGH);
    delay(200);
    digitalWrite(LED1, LOW);
    delay(200);
    digitalWrite(LED1, HIGH);
    delay (200);
    digitalWrite(LED1, LOW);
    delay (200);
    digitalWrite(LED1, HIGH);
    
    
    }

///THIS DEALS WITH SENDING DATA {CAN BE IMPLEMENTED IN FUTURE}

  else if (deviceConnected) {
    // Fabricate some arbitrary junk for now...
    //txValue = analogRead(readPin) / 3.456; // This could be an actual sensor reading!

    // Let's convert the value to a char array:
    //char txString[8]; // make sure this is big enuffz
    //dtostrf(txValue, 1, 2, txString); // float_val, min_width, digits_after_decimal, char_buffer

    //    pCharacteristic->setValue(&txValue, 1); // To send the integer value
    //    pCharacteristic->setValue("Hello!"); // Sending a test message
    //pCharacteristic->setValue(txString);

    //pCharacteristic->notify(); // Send the value to the app!
    //    Serial.print("*** Sent Value: ");
    //    Serial.print(txString);
    //    Serial.println(" ***");

    // You can add the rxValue checks down here instead
    // if you set "rxValue" as a global var at the top!
    // Note you will have to delete "std::string" declaration
    // of "rxValue" in the callback function.
    //    if (rxValue.find("A") != -1) {
    //      Serial.println("Turning ON!");
    //      digitalWrite(LED, HIGH);
    //    }
    //    else if (rxValue.find("B") != -1) {
    //      Serial.println("Turning OFF!");
    //      digitalWrite(LED, LOW);
    //    }
  }
  delay(1000);
}
