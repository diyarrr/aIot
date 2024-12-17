#include "ESP8266WiFi.h"
#include "ESPAsyncWebServer.h"
#include <ArduinoJson.h> // Requires ArduinoJson library

#define RELAY_NO true
#define NUM_RELAYS 1

// Relay GPIOs
int relayGPIOs[NUM_RELAYS] = {4};

// Wi-Fi credentials
const char* ssid = "Wifi ID";
const char* password = "Wifi Password";

// AsyncWebServer object
AsyncWebServer server(80);

void setup() {
  // Start the serial port
  Serial.begin(115200);

  // Initialize relay pins
  for (int i = 0; i < NUM_RELAYS; i++) {
    pinMode(relayGPIOs[i], OUTPUT);
    if (RELAY_NO) {
      digitalWrite(relayGPIOs[i], HIGH); // Start relays as OFF (Normally Open)
    } else {
      digitalWrite(relayGPIOs[i], LOW);  // Start relays as ON (Normally Closed)
    }
  }

  // Connect to the Wi-Fi network
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to Wi-Fi...");
  }

  // Print the local IP address
  Serial.println("Wi-Fi connected!");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());

  // Control relays via POST request
  server.on("/relay", HTTP_POST, [](AsyncWebServerRequest *request){},
    NULL,
    [](AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total) {
      String body = String((char*)data); // Receive the incoming data
      Serial.println("Incoming POST request:");
      Serial.println(body);

      // Parse the JSON data
      StaticJsonDocument<200> jsonDoc;
      DeserializationError error = deserializeJson(jsonDoc, body);

      if (error) {
        Serial.println("JSON parsing error!");
        request->send(400, "application/json", "{\"error\":\"Invalid JSON\"}");
        return;
      }

      // Extract relay and state values
      int relay = jsonDoc["relay"];
      String state = jsonDoc["state"];

      if (relay > 0 && relay <= NUM_RELAYS) {
        if (state == "ON") {
          digitalWrite(relayGPIOs[relay - 1], RELAY_NO ? LOW : HIGH); // Turn the relay ON
          Serial.println("Relay Turned ON");
        } else if (state == "OFF") {
          digitalWrite(relayGPIOs[relay - 1], RELAY_NO ? HIGH : LOW); // Turn the relay OFF
          Serial.println("Relay Turned OFF");
        } else {
          request->send(400, "application/json", "{\"error\":\"Invalid state\"}");
          return;
        }
        request->send(200, "application/json", "{\"success\":\"Relay updated\"}");
      } else {
        request->send(400, "application/json", "{\"error\":\"Invalid relay number\"}");
      }
    });

  // Start the server
  server.begin();
}

void loop() {
}
