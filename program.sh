#!/bin/bash

repl(){
  # lein repl :headless :host 0.0.0.0 :port 35543
  lein repl :start :host 0.0.0.0 :port 35543
}

main(){

  clojure -m fiber.main

}

push(){
  ORIGIN=$(git remote get-url origin)
  rm -rf .git
  git init -b main
  git remote add origin $ORIGIN
  git config --local include.path ../.gitconfig
  git add .
  git commit -m "i am fiber program"
  git push -f -u origin main
}


"$@"
