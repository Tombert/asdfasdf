(import '[eu.mihosoft.vrl.v3d STL Cube Cylinder Extrude RoundedCube Sphere Vector3d Transform])

(defn move-x
  [obj n]
  (.transformed obj (.translateX (Transform/unity) n)))

(defn move-y
  [obj n]
  (.transformed obj (.translateY (Transform/unity) n)))

(defn union-all [& objs]
  (reduce (fn [a b] (.union a b)) objs))

(let [size 40.0
      cube (.toCSG (Cube. size size size))
      cylinder (.toCSG (Cylinder. (/ size 4) (/ size 2) size 80))
      sphere (.toCSG (Sphere. (* 12.5 (/ size 20))))
      rounded-cube (.toCSG 
                     (doto (RoundedCube. size size size)
                       (.cornerRadius (/ size 10))))]
  (union-all 
	    cube
	)
)