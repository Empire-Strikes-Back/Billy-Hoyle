#!/bin/bash

repl(){
  # lein repl :headless :host 0.0.0.0 :port 35543
  lein repl :start :host 0.0.0.0 :port 35543
}

main(){

  clojure -m fiber.main

}

"$@"
