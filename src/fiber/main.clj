(ns app.main
  (:require [tools.nrepl]
            [tools.core]
            [fiber.impl]
            [app.srv.server]
            [tools.core]
            [fiber.core]
            [app.data.core]
            [app.data.usda]
            [app.db.core]
            [fiber.setup]

   ;
            )
  ;
  )

(defn -main  [& args]
  (tools.nrepl/-main)
  (fiber.setup/init!)
  (app.srv.server/run-dev))

#_(defn -main  [& args]
  (tools.nrepl/-main)
  (app.srv.server/-main)
  )

(comment

  (tools.core/try-parse-int "3")

  (tools.core/version)

  
  (fiber.core/priority)

  ;
  )

