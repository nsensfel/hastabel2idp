## Parameters ##################################################################
SRC_DIR ?= ${CURDIR}/src/
BIN_DIR ?= ${CURDIR}/bin/
LIB_DIR ?= ${CURDIR}/lib/
TMP_DIR ?= /tmp/hastabel2idp_standalone.jar.build/

TARGET ?= hastabel2idp.jar
STANDALONE ?= hastabel2idp_standalone.jar
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

###### HASTABEL
HASTABEL_JAR ?= $(LIB_DIR)/hastabel_standalone.jar

##### Downloader
DOWNLOADER ?= wget

## Parameters Sanity Check #####################################################
ifeq ($(strip $(JAVA)),)
$(error No Java executable defined as parameter.)
endif

ifeq ($(strip $(JAVAC)),)
$(error No Java compiler defined as parameter.)
endif

ifeq ($(strip $(HASTABEL_JAR)),)
$(error No HASTABEL_JAR defined as parameter.)
endif

## Java Config #################################################################
CLASSPATH = "$(SRC_DIR):$(BIN_DIR):$(ANTLR_JAR):$(HASTABEL_JAR)"

## Makefile Magic ##############################################################
MANIFEST = $(SRC_DIR)/Manifest.txt

JAVA_SOURCES = $(shell find $(SRC_DIR)/hastabel2idp/ -name "*.java")
CLASSES = $(patsubst $(SRC_DIR)/%,$(BIN_DIR)/%, $(JAVA_SOURCES:.java=.class))

## Makefile Rules ##############################################################
$(STANDALONE): $(TMP_DIR) $(TARGET) $(ANTLR_JAR)
	unzip -d $(TMP_DIR) -uo $(TARGET)
	unzip -d $(TMP_DIR) -uo $(HASTABEL_JAR)
	jar -cvfm $@ $(MANIFEST) -C $(TMP_DIR) .

$(TARGET): $(HASTABEL_JAR) $(JAVA_SOURCES) $(CLASSES) $(MANIFEST)
	rm -f $(TARGET) $(INSTALL_DIR)/$@
	$(JAR) cfm $@ $(MANIFEST) -C $(BIN_DIR) .
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

$(TMP_DIR):
	mkdir -p $@

$(LIB_DIR):
	mkdir -p $@

$(BIN_DIR):
	mkdir -p $@

##### For my private use...
publish: $(TARGET) $(STANDALONE)
	scp $^ dreamhost:~/noot-noot/tabellion/jar/

