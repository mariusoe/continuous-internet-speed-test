continuous-internet-speed-test
==============================

[![build status](https://travis-ci.org/mariusoe/continuous-internet-speed-test.png)](https://travis-ci.org/mariusoe/continuous-internet-speed-test)

Continuous Internet Speed Test is a little tool to continuously test the speed of the download bandwidth. For that purpose, a dummy file is automatically downloaded in a specified periode.

### Starting the tool
You can start the tool with an argument that specifies a custom config-file:
`java -jar cist.jar /path/to/your/setting.ini`. If no configuration file is specified, the default `settings.ini` will be loaded.

### Example configuration file
```
# The result file
RESULT_FILE=result.csv

# Overwrite result file
OVERWRITE_RESULT_FILE=false

# Format of the result file. Only 'csv' supported yet
RESULT_FORMAT=csv

# Delay between two measurements
MEASUREMENT_DELAY=5000

# This is the maximal amount of measurements which are executed (0=unlimited)
MAX_MEASUREMENTS=5

# The maximum duration a measurement can running (0=unlimited)
MAX_MEASUREMENT_DURATION=10000

# The maximum amount of bytes which are downloaded in a single measurement before (0=unlimited)
MAX_DOWNLOAD_VOLUME=50000000

# URL of the test-file. You can add $CURRENTTIMEMILIS$ placeholder that will be replaced by current timestamp
TEST_FILE_URL=http://speedtest.reliableservers.com/100MBtest.bin

# Whether the current download speed of a speed-test should be shown
SHOW_SPEEDLISTENER=true

# The delay between two outputs of the speedlistener
SPEEDLISTENER_DELAY=1000

# Whether the first speed-test should be delayed or not
DELAY_FIRST_MEASUREMENT=false

## More settings
# CSV settings
# LINE_SEPARATOR=\n
# VALUE_SEPARATOR=;
```