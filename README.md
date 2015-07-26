## About this Application

Nothing else like this software exists for platforms running Linux. This software enables the user to have text spoken aloud from any text that can be highlighted including ebooks, email, web pages, use your imagination. Along with that are features I demonstrated in screenshots and a youtube video.

## JSpeak Demo

http://www.youtube.com/embed/raEUJraXvwY

## JSpeak in Action

![Compact GUI](http://i.imagebanana.com/img/8bqnnbah/JSpeak_025.png)

![Button Hints](http://i.imagebanana.com/img/s31ofq39/JSpeak_024.png)

![Voices](http://i.imagebanana.com/img/i36ea15x/JSpeak_026.png)

## Installation and Usage

### Ubuntu/Debian/Mint:

*Note:* Do not copy and paste the *$* at the beginning of the commands or the *#*. The *$* means run as your user. The *#* means run as the root user or use sudo if on an Ubuntu/Mint box.

    $ sudo apt-get install espeak mbrola

Choose your voices. There are many, but for all english ones, do

    $ sudo apt-get install mbrola-us1 mbrola-us2 mbrola-us3 mbrola-en1

Many mbrola voices can be installed through apt-get in Ubuntu/Mint. Some such as mbrola-mx1 are not available through apt. If you wish to install those. Follow the manual installation below for them.

### Fedora/Suse:

    # yum install espeak

Fedora and other rpm based systems do not have mbrola and mbrola packages afaik. However this is not a problem. Continue to follow the manual installation for them.

### Manual installation of mbrola and mbrola voices (From the espeak/mbrola docs)

*Linux Installation*

From eSpeak version 1.44 onwards, eSpeak calls the mbrola program directly, rather than passing phoneme data to it using a pipe.

1. To install the Linux Mbrola binary, download:
http://www.tcts.fpms.ac.be/synthesis/mbrola/bin/pclinux/mbr301h.zip
Unpack the archive, and copy and rename the file from: mbrola-linux-i386 to
mbrola somewhere in your executable path (eg. /usr/local/bin/mbrola ).

2. Get the English 1 (en1) voice from:
http://www.tcts.fpms.ac.be/synthesis/mbrola/mbrcopybin.html
Unpack the archive, and copy the *en1* data file (not the whole "en1"
directory) to /usr/share/mbrola/en1 .

  eSpeak will look for mbrola voices firstly in espeak-data/mbrola and then in /usr/share/mbrola

  *Note:* Get as many voices as you like. Each will show in the voice selection combo box.

3. If you use the eSpeak voice such as "*mb-en1*" then eSpeak will use the mbrola "en1" voice, eg:

   $ espeak -v mb-en1 "Hello world"

  *Note:* This step is just for testing that everything is setup and working correctly.


### Obtaining and running the app.

    $ wget https://github.com/downloads/BullShark/JSpeak/JSpeak.tbz

    $ tar -xf JSpeak.tbz

    $ cd JSpeak

    $ java -jar JSpeak.jar

### OR Obtain the app from git and run.

    $ git clone git://github.com/BullShark/JSpeak.git

    $ cd JSpeak/dist

    $ java -jar JSpeak.jar

### Usage

1. Toggle on the scan button (has a diamond icon). Hover your mouse over other buttons for descriptions.

2. (Optional) Change the voice from the drop down menu of the combo box to set a better sounding mbrola voice.

3. Start copying text from your favorite ebook, the web, email, etc. to begin having the text read to you.

### Windows Users

![Email me](mailto:christopher.lemire@gmail.com)

### Help/Support

![Email](mailto:christopher.lemire@gmail.com) or submit a bug report
