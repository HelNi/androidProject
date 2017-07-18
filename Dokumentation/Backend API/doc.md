# API

## Übersicht
Die Backend-API ist eine Rest-Schnittstelle, zu der sich alle Worktime-Apps verbinden. 

Sie bietet Authentisierung (im ersten Schritt über benutzername oder E-Mail und Passwort) und dann über ein API-Token.

Mithilfe dieses Tokens bietet sie dann eine Verwaltung (CRUD) der Verschiedenen Entitäten.

## Repository
Die API wird getrennt von der App Entwickelt unter https://github.com/nSchick/worktimeBackend/ .

## Verfügbarkeit
Die API ist fürs erste unter https://nsh.webuse.de/worktime/ zu erreichen. Zur API gehört eine kleine Web-Anwendung, mit welcher die Benutzer und Aktivätstypen verwaltet werden können.

Außerdem gibt es dort eine Dokumentation inklusive Sandbox, wo authorisierte Benutzer die API-Funktionen ausprobieren können.

Der Basis-Pfad der API ist https://nsh.webuse.de/worktime/api .

## Unterstützte Protokolle
Die PI basiert auf Rest, sie unterstützt also primär JSON, was auch von der Worktime-APP verwendet wird.

Alternativ wird aber auch XML unterstützt.

## Funktionen
Die genauen Funktionen inklusive der Benötigten Parameter sind in der API-Dokumentation (https://nsh.webuse.de/worktime/documentation) dokumentiert.

Hiervon gibt es auch eine Offline-Version (ohne Sandbox) in diesem Ordner unter der Datei **api.html**

Im Wesentlichen Gibt es eine Funktion zur Authoriserung mithilfe von Benutzername und Passwort, welche ein API-Token zurück gibt.

Dieses Token muss dann für alle anderen Requests als Query-Parameter "token" (oder als header "X-API-TOKEN") mitgesendet werden.

Die anderen Funktionen sind CRUD für Benutzer, Aktivitäten und Zeiterfassungseinträge. Der Zugriff auf Daten von anderen Benutzern ist Administratoren Vorbehalten.

## Verwendung in der App
Die Backend-API wird in der App mithilfe der Rest-Bibliothek "Retrofit" (https://github.com/square/retrofit) verwendet. Die Serialisierung und Deserialisierung erfolg durch GSON (https://github.com/google/gson)

## Offene Punkte
- Die API-Keys haben derzeit kein Ablaufdatum. Außerdem gibt es nur einen gleichzeitig pro Benutzer. 
- Die Admin-Only Methoden (Verwalten und Einsehen von Einträgen anderer Benutzer) wird derzeit nicht von der Anwendung benutzt
- Das Web-Anwendungs Frontend ist Rudimentär, außerdem fehlt das vollständige Editieren von Benutzern.
