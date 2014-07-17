# Podload

A utility/daemon for downloading podcasts periodically so they can be slotted into Airtime SmartBlocks.

Relies heavily upon [moments](https://github.com/Raynes/moments) and [chronicle](https://github.com/flatland/chronicle).

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

It'll be in target/podload

## License

Copyright © 2014 ken restivo <ken@restivo.org>

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
