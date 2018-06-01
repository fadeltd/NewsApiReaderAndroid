# News API (https://newsapi.org) Reader

[![Build Status](https://travis-ci.org/fadeltd/NewsApiReaderAndroid.svg?branch=master)](https://travis-ci.org/fadeltd/NewsApiReaderAndroid)

This is a mobile application built with Android Studio to consume API from the [News API](https://newsapi.org/) and give you latest news from several Indonesian domains such as [Kompas.com](https://www.kompas.com), [Detik.com](https://www.detik.com), [Liputan6.com](https://www.liputan6.com), etc.


## Feature
* **News List**
  * user can see list of news with title and image
  * user can infinite scroll list of news
  * user can see 10 news per page with different content for every page
* **Headline News**
  * user can see headline news in the middle of list of news
  * user can scroll horizontally headline news
  * user can see 5 headline news per page 
* **News Detail** 
  * user can click one of the news and see the news on webview
* **Favorite News**
  * user can click “love” icon and make the news favorite
  * if user already already favorited the news, the icon will change from empty to full and vice versa
  * user also can un favorite the news
  * favorite will be persistent to that particular news, even though user already closed the app

## Preview
![Screenshot](./screenshots/screenshot-1.png?raw=true)
![Screenshot](./screenshots/screenshot-2.png?raw=true)

#### Try this Application
- Clone this [Repo](https://github.com/fadeltd/NewsApiReaderAndroid)
```
git clone https://github.com/fadeltd/NewsApiReaderAndroid.git
```
- [Android APK](./release/app-debug.apk) 

#### Generate Releases APK
- Run command
  ```
  $ ./gradlew clean assembleRelease
  ```
- Your apk will located at `app/build/outputs/apk/release/`

## Author
**Fadel Trivandi Dipantara** (https://github.com/fadeltd)

### License

Released under the MIT License. Check [`LICENSE.md`](LICENSE.md) for more info.