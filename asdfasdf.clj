(import '[eu.mihosoft.vrl.v3d STL Cube Cylinder Extrude RoundedCube Sphere Vector3d Transform])

(def LID_WIDTH 285)
(def LID_DEPTH 175)
(def LID_HEIGHT 30)
(def WALL_THICKNESS 4)
(def CIRCLE_PIECE_DIAMETER 100)
(def JOYSTICK_DIST_FROM_EDGE 20)
(def START_BUTTON_DIST_FROM_EDGE 10)
(def BIG_BUTTON_WIDTH 31.8)
(def DIST_BETWEEN_BUTTONS 10)
(def PEG_DIAMETER  8)
(def SMALL_BUTTON_WIDTH 25.5)



(defn move-x
  [obj n]
  (.transformed obj (.translateX (Transform/unity) n)))

(defn move-y
  [obj n]
  (.transformed obj (.translateY (Transform/unity) n)))
  
(defn move-z
  [obj n]
  (.transformed obj (.translateZ (Transform/unity) n)))

(defn move [obj x y z]
    (-> obj
        (move-x x)
        (move-y y)
        (move-z z)		
    )
)
  
(defn union-all [& objs]
  (reduce (fn [a b] (.union a b)) objs))

  
(defn cube [x y z]
   (.toCSG (Cube. (double x) (double y) (double z)))
)

(defn sphere [radius]
    (.toCSG (Sphere. (double radius)  100 100)  )
)

(defn difference-all [& objs]
(reduce (fn [a b] (.difference a b)) objs)
)

(defn cylinder [radius height]
   (.toCSG (Cylinder. (double radius) (double height) 100))
)
(def NUM_PEGS 8)
(defn joyhole []

     
     (move-z 
      (union-all
      (cylinder (/ CIRCLE_PIECE_DIAMETER 2) 100.0)
          
      
       
       (->> (range NUM_PEGS)
           (mapv (fn [i]
           	 (.rotz (move-x (cylinder (/ PEG_DIAMETER 2) 100) 60) (* i (/ 360 NUM_PEGS))))))
           	 
       
       ) -20)   
) 
(defn box []
	 (union-all 
		    (difference-all 
		        (cube LID_WIDTH LID_DEPTH LID_HEIGHT) 
		        (move-z 
		            (cube 
		               (-  LID_WIDTH (* 1 WALL_THICKNESS))
		               (- LID_DEPTH (* 1 WALL_THICKNESS)) 
		               (- LID_HEIGHT(* 1 WALL_THICKNESS )))
		               (/ WALL_THICKNESS 2)
		               )
			(move-x (joyhole) (+ (/ LID_WIDTH 2) (/ CIRCLE_PIECE_DIAMETER -2) (* -1 JOYSTICK_DIST_FROM_EDGE)))

			(move-y (move-x (move-z (cylinder (/ SMALL_BUTTON_WIDTH 2) 100) -30) (+ SMALL_BUTTON_WIDTH (/ LID_WIDTH -4))) (- (/ LID_DEPTH 2) (/ SMALL_BUTTON_WIDTH 2) START_BUTTON_DIST_FROM_EDGE) )
			(move-y (move-x (move-z (cylinder (/ SMALL_BUTTON_WIDTH 2) 100) -30) (+ (* -1 DIST_BETWEEN_BUTTONS ) (/ LID_WIDTH -4))) (- (/ LID_DEPTH 2) (/ SMALL_BUTTON_WIDTH 2) START_BUTTON_DIST_FROM_EDGE) )


			
			 (->> (range 2)
				(mapv (fn [j] 
					(->> (range 3) 
				 	 (mapv (fn [i]  
				 	 	 	(move-y 
				 	 	 	   (move-x (move-z (cylinder (/ BIG_BUTTON_WIDTH 2) 1000) -100) 
				 	 	 	        (+ (* -1 (+ BIG_BUTTON_WIDTH DIST_BETWEEN_BUTTONS (* -1 JOYSTICK_DIST_FROM_EDGE))) (* -1 i (+ DIST_BETWEEN_BUTTONS BIG_BUTTON_WIDTH))) ) 
				 	 	 	        (+ (* -1 (/ BIG_BUTTON_WIDTH 2)) (* j (+ DIST_BETWEEN_BUTTONS BIG_BUTTON_WIDTH)))
				 	 	 	        )
				 	 	 )))))
			 (flatten)
			 (union-all)))))
			 
(let [size 40.0
      rounded-cube (.toCSG 
                     (doto (RoundedCube. size size size)
                       (.cornerRadius (/ size 10))))]

    ;(box)

    ;(sphere 10)

                  
 ;(joyhole)
 (box)
	 
	 
	 )