PACKAGE_NAME = as1

SOURCE_PATH = ./src
OUTPUT_PATH = ./out/production/as2

PART1_MAIN = ./src/part1/webserver/ServerMain.java
PART2_MAIN = ./src/part2/webserver/ServerMain.java

all: java

java:
	@echo "[=====Building Sources=====]"
	mkdir -p $(OUTPUT_PATH)
	javac -sourcepath $(SOURCE_PATH) -d $(OUTPUT_PATH) $(PART1_MAIN) $(PART2_MAIN)

runWebServer: java
	@echo "[=====Running WebServer=====]"
	@java -cp $(OUTPUT_PATH) part1.webserver.ServerMain

runWebServerWithLogging: java
	@echo "[=====Running WebServer with Logging=====]"
	@java -cp $(OUTPUT_PATH) part2.webserver.ServerMain

clean:
	rm -rf $(OUTPUT_PATH)

package:
	@mkdir -p $(PACKAGE_NAME)
	@cp -r Makefile $(SOURCE_PATH) *.md $(PACKAGE_NAME)/
	@zip -r $(PACKAGE_NAME).zip $(PACKAGE_NAME)/
	@rm -rf $(PACKAGE_NAME)