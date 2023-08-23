(ns kentbull.keri-clj
  (:require [cli-matic.core :refer [run-cmd]])
  (:gen-class))



(defn greet
  "Callable entry point to the application."
  [{:keys [protocol bootport]}]
  (println (str "Hello, " (or protocol "World") " on port " (or bootport "3901") "!")))

(def CONFIGURATION
  {:app {:command "keri-clj"
         :description "An example for greeting the user."
         :version "0.0.1"}
   :global-opts [{:option "bootport"
                  :as "The port to run the agent boot TCP server on"
                  :type :int
                  :default :3901}]
   :commands [{:command "greet"
               :description ["Greets the user"
                             (str cli-matic.optionals/with-orchestra?)
                             ""
                             "Does this look good?"]
               :opts [{:option "protocol" :short "p" :env "PROTO" :as "Protocol TCP or HTTP" :type :string :default "TCP"}]
               :runs greet}]})

(defn -main
  "KERI-clj main function"
  [& args]
  (run-cmd args CONFIGURATION)
  )
