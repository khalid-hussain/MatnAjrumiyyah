About the App
==============
This app is about providing [Matn Ajrumiyyah](http://en.wikipedia.org/wiki/Aj%C4%81r%C5%ABm%C4%ABya) in a digital form. There are a couple of other apps on the Play Store for this text but I was not satisfied with their quality and the lack of updates with Google Material Design guidelines. May Allah bless them.

![](https://raw.githubusercontent.com/khalid-hussain/MatnAjrumiyyah/master/screenshot1.png)
![](https://raw.githubusercontent.com/khalid-hussain/MatnAjrumiyyah/master/screenshot2.png)

About the Content
==================
Matn Ajrumiyyah is a text which explains Arabic grammar. The text is divided into sections explaining different aspects of Arabic grammar. The text also exists in poem form but this app is about the original text since [kashida](http://en.wikipedia.org/wiki/Kashida) support is quite poor digitally let alone supporting different screen sizes.

(To Be)Features
==================
1. Google Material Design
	1. Good Navigation drawer presenting the topics in the book.
	2. Navigation drawer should have something like a ViewPager which shows the contents and user bookmarks. For reference, please see [Tae Kim's Japanese Learning app](https://play.google.com/store/apps/details?id=com.alexisblaze.japanese_grammar).
	3. A WebView which is performance compliant by loading in a separate thread instead of the UI thread.
2. Clear Arabic Support. Current recommendation is [Scheherazade](http://openfontlibrary.org/en/font/scheherazade).
3. Add a bookmarking feature.
4. (Maybe) Add font size option.
5. (Maybe) Add a search option.

Libraries
==========
- Android Support Library
- [Calligraphy](https://github.com/chrisjenx/Calligraphy)

Credits
==========
- [AlOloom Salafi Network](http://aloloom.net/vb/showthread.php?t=8833)
- Hussaini Zulkifli([GitHub](https://github.com/the1375),[Twitter](https://twitter.com/HussainiZul))

To Do
======
1. ~~Type out the text into different chapter files. Organize the files and filenames.~~
2. Design the navigation drawer better.
3. Ensure the WebView is loaded correctly, performance wise.
4. Check layout on devices with different screen sizes.
5. ~~App crashes on Android 4.4.4 (GenyMotion Emulator). I have no idea why.~~
6. ~~Add the remaining chapters.~~