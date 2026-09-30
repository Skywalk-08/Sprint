#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$ROOT_DIR/test"
TOMCAT_DIR="/home/windyadr/tomcat10"
WEBAPPS_DIR="$TOMCAT_DIR/webapps"
APP_NAME="TestFramework"
WAR_FILE="$PROJECT_DIR/target/$APP_NAME.war"
DEST_WAR="$WEBAPPS_DIR/$APP_NAME.war"
DEST_DIR="$WEBAPPS_DIR/$APP_NAME"

echo "Compilation du framework..."
cd "$ROOT_DIR/framework"
./compile.sh

FRAMEWORK_JAR="$ROOT_DIR/framework/framework.jar"
if [ ! -f "$FRAMEWORK_JAR" ]; then
  echo "Erreur: framework.jar introuvable apres compilation." >&2
  exit 1
fi

echo "Compilation du projet..."
cd "$PROJECT_DIR"
mvn clean package

if [ ! -f "$WAR_FILE" ]; then
  echo "Erreur: WAR introuvable: $WAR_FILE" >&2
  exit 1
fi

echo "Arret de Tomcat..."
if [ -x "$TOMCAT_DIR/bin/shutdown.sh" ]; then
  "$TOMCAT_DIR/bin/shutdown.sh" || true
else
  echo "Erreur: shutdown.sh introuvable ou non executable." >&2
  exit 1
fi

sleep 2

echo "Deploiement de $APP_NAME.war vers $WEBAPPS_DIR..."
rm -rf "$DEST_DIR" "$DEST_WAR"
cp "$WAR_FILE" "$DEST_WAR"

echo "Demarrage de Tomcat..."
if [ -x "$TOMCAT_DIR/bin/startup.sh" ]; then
  "$TOMCAT_DIR/bin/startup.sh"
else
  echo "Erreur: startup.sh introuvable ou non executable." >&2
  exit 1
fi

echo
echo "Application deployee."
echo "Lien a ouvrir: http://localhost:8080/$APP_NAME/"
