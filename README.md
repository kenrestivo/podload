# Podload

A utility/daemon for downloading podcasts periodically so they can be slotted into Airtime SmartBlocks.

Relies heavily upon [moments](https://github.com/Raynes/moments) and [chronicle](https://github.com/flatland/chronicle).

## What it does

You give it the addresses of RSS/Atom podcast feeds and a cron-like set of dates/times to check, and at those times it downloads the latest installment of each show, tags it with an album so you can easily identify it in Airtime (via Smart Block, etc), and saves it overwriting the previous installment of that show. If there is no new show to download at the time the job runs, the old show is blown away, so that you're not repeating the same show over and over if the podcaster doesn't update.

Because of what is probably an implementation detail of Airtime, you can create a playlist or Smart Block or even just add the file to a show, and when the file gets overwritten, the latest one will play the next time the show runs. This keeps you from having to clog up your disk or VPS with old shows.

## What it does not do

Doesn't integrate very tightly with Airtime, nor does it know whether it has already download that show before, so you have to be careful about when to schedule the downloads. Best to check the feed and get a sense of when the podcaster usuall uploads their shows, and keep track of when you want them to run in Airtime, and set your download times to be sometime after the podcaster usually uploads and sufficiently before you want the show to run on your station.

## Who would want this?

Probably nobody except the one client who did, but, just in case it's useful, here it is.

## Usage

Edit the config file, there's an example config file included.
```clojure
[{;; The name of the show
  :name "This Island Earth"
  ;; The URL at which to find the podcast feed
  :feed-url "http://www.islaearth.org/radio/feed.rss"
  ;; The schedule! See chronicle docs
  ;; This example is every weekday. Note hours are localized to local timezone.
  :frequency {:minute [0]
              :hour [0] ;; midnight
              :day-of-week [2 3 4 5 6]}
  ;; File to save the podcast as. The file will get overwritten at each run!
  :filename "islaearth.mp3"
  ;; Tag for ALBUM in ID3V2.4. Use this to build SmartBlocks in Airtime for the show
  :tag "IslaEarth"
  ;; Where to save the show once it's done downloading and has been tagged.
  :dest-dir "/home/imports/podcasts"
  }
  ...
  ]
```
Run it with:
```shell
podload your-config-file.edn
```

## Building

Build the binary with:

```shell
lein bin 
```

It'll be in target/podload as an executable "binary" (like all 15MB of it). Copy it wherever you like.

## License

Copyright © 2014 ken restivo <ken@restivo.org>

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
