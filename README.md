# Podload

A utility/daemon for downloading podcasts periodically so they can be slotted into Airtime.

## Usage

Edit the config file, see docs on moments.

There's an example config file included.

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
