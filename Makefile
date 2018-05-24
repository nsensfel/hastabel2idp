## Parameters ##################################################################
SRC_DIR ?= ${CURDIR}/src/
BIN_DIR ?= ${CURDIR}/bin/
LIB_DIR ?= ${CURDIR}/lib/

TARGET ?= hastabel2idp.jar
RUN_SCRIPT ?= hastabel2idp.sh
INSTALL_DIR ?= $(LIB_DIR)

#### Where to get the missing Jar files.
JAR_SOURCE ?= "https://noot-noot.org/tabellion/jar/"

#### Binaries
###### JAR binary
JAR ?= jar

###### JRE binary
JAVA ?= java

###### JDK binary
JAVAC ?= javac

###### ANTLR
ANTLR_JAR ?= $(LIB_DIR)/antlr-4.7-complete.jar

###### HASTABEL
HASTABEL_JAR ?= $(LIB_DIR)/hastabel.jar

##### Downloader
DOWNLOADER ?= wget

## Parameters Sanity Check #####################################################
ifeq ($(strip $(JAVA)),)
$(error No Java executable defined as parameter.)
endif

ifeq ($(strip $(JAVAC)),)
$(error No Java compiler defined as parameter.)
endif

ifeq ($(strip $(ANTLR_JAR)),)
$(error No ANTLR_JAR defined as parameter.)
endif

ifeq ($(strip $(HASTABEL_JAR)),)
$(error No HASTABEL_JAR defined as parameter.)
endif

## Java Config #################################################################
CLASSPATH = "$(SRC_DIR):$(BIN_DIR):$(ANTLR_JAR):$(HASTABEL_JAR)"

## Makefile Magic ##############################################################
JAVA_SOURCES = \
	$(wildcard $(SRC_DIR)/hastabel2idp/*.java) \
	$(wildcard $(SRC_DIR)/hastabel2idp/*/*.java)
CLASSES = $(patsubst $(SRC_DIR)/%,$(BIN_DIR)/%, $(JAVA_SOURCES:.java=.class))

## Makefile Rules ##############################################################
$(TARGET): $(ANTLR_JAR) $(HASTABEL_JAR) $(JAVA_SOURCES) $(CLASSES) $(RUN_SCRIPT)
	rm -f $(TARGET) $(INSTALL_DIR)/$@
	$(JAR) cf $@ -C $(BIN_DIR) .
	cp $@ $(INSTALL_DIR)/$@

clean:
	rm -rf $(BIN_DIR)/*
	rm -f $(TARGET)

$(CLASSES): $(BIN_DIR)/%.class: $(SRC_DIR)/%.java $(BIN_DIR)
	$(JAVAC) -cp $(CLASSPATH) -d $(BIN_DIR) $<

%.jar:
	$(MAKE) $(LIB_DIR)
	echo "Attempting to download missing jar '$@'..."
	cd $(LIB_DIR); $(DOWNLOADER) "$(JAR_SOURCE)/$(notdir $@)"

$(LIB_DIR):
	mkdir -p $@

$(BIN_DIR):
	mkdir -p $@

$(RUN_SCRIPT): Makefile
	echo "#!/bin/sh" > $@
	echo "$(JAVA) -cp \"$(CLASSPATH)\" hastabel2idp.Main $$*" >> $@
	chmod +x $@

##### For my private use...
publish: $(TARGET)
	scp $< dreamhost:~/noot-noot/tabellion/jar/

