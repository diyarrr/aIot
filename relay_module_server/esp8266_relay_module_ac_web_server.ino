#include "ESP8266WiFi.h"
#include "ESPAsyncWebServer.h"
#include <ArduinoJson.h> // ArduinoJson kütüphanesi gerekiyor

#define RELAY_NO true
#define NUM_RELAYS 1

// Röle GPIO tanımları
int relayGPIOs[NUM_RELAYS] = {4};

// Wi-Fi bilgileri
const char* ssid = "Wifi ID";
const char* password = "Wifi Password";

// AsyncWebServer nesnesi
AsyncWebServer server(80);

void setup() {
  // Seri port başlat
  Serial.begin(115200);

  // Röle pinlerini ayarla
  for (int i = 0; i < NUM_RELAYS; i++) {
    pinMode(relayGPIOs[i], OUTPUT);
    if (RELAY_NO) {
      digitalWrite(relayGPIOs[i], HIGH); // Röleyi kapalı başlat
    } else {
      digitalWrite(relayGPIOs[i], LOW); // Röleyi açık başlat
    }
  }

  // Wi-Fi ağına bağlan
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Wi-Fi ağına bağlanılıyor...");
  }

  Serial.println("Wi-Fi bağlı!");
  Serial.print("IP Adresi: ");
  Serial.println(WiFi.localIP());

  // POST isteği ile röle kontrolü
  server.on("/relay", HTTP_POST, [](AsyncWebServerRequest *request){},
    NULL,
    [](AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total) {
      String body = String((char*)data); // Gelen veriyi al
      Serial.println("Gelen POST isteği:");
      Serial.println(body);

      // JSON verisini ayrıştır
      StaticJsonDocument<200> jsonDoc;
      DeserializationError error = deserializeJson(jsonDoc, body);

      if (error) {
        Serial.println("JSON ayrıştırma hatası!");
        request->send(400, "application/json", "{\"error\":\"Invalid JSON\"}");
        return;
      }

      // Röle ve durum bilgilerini al
      int relay = jsonDoc["relay"];
      String state = jsonDoc["state"];

      if (relay > 0 && relay <= NUM_RELAYS) {
        if (state == "ON") {
          digitalWrite(relayGPIOs[relay - 1], RELAY_NO ? LOW : HIGH); // Röleyi aç
          Serial.println("Röle Açıldı");
        } else if (state == "OFF") {
          digitalWrite(relayGPIOs[relay - 1], RELAY_NO ? HIGH : LOW); // Röleyi kapat
          Serial.println("Röle Kapandı");
        } else {
          request->send(400, "application/json", "{\"error\":\"Invalid state\"}");
          return;
        }
        request->send(200, "application/json", "{\"success\":\"Röle güncellendi\"}");
      } else {
        request->send(400, "application/json", "{\"error\":\"Invalid relay number\"}");
      }
    });

  // Sunucuyu başlat
  server.begin();
}

void loop() {
}