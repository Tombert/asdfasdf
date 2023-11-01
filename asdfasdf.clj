(import [eu.mihosoft.vrl.v3d STL CSG Cube Cylinder Extrude RoundedCube Sphere Vector3d Transform]
        [com.neuronrobotics.bowlerstudio.physics PhysicsCore CSGPhysicsManager MobileBasePhysicsManager PhysicsEngine]
        [ com.neuronrobotics.bowlerstudio.creature MobileBaseCadManager MobileBaseLoader]
        [com.neuronrobotics.bowlerstudio.scripting ScriptingEngine]
        [javax.vecmath Vector3f]
        [com.neuronrobotics.sdk.addons.kinematics DHLink MobileBase]
        [com.neuronrobotics.sdk.addons.kinematics.math RotationNR TransformNR]
        
 )

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
(def BATTERY_LENGTH 100)


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
    (.toCSG (Sphere. (double radius)  200 200)  )
)

(defn difference-all [& objs]
	(reduce (fn [a b] (.difference a b)) objs))

(defn cylinder [radius height]
   (.toCSG (Cylinder. (double radius) (double height) 200))
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
(defn top []
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


			(for [j (range 2)
				 i (range 3)
				]
				(let [x (+ (* -1 (+ BIG_BUTTON_WIDTH DIST_BETWEEN_BUTTONS (* -1 JOYSTICK_DIST_FROM_EDGE))) (* -1 i (+ DIST_BETWEEN_BUTTONS BIG_BUTTON_WIDTH)))
				      y (+ (* -1 (/ BIG_BUTTON_WIDTH 2)) (* j (+ DIST_BETWEEN_BUTTONS BIG_BUTTON_WIDTH)))
				      z -100]
				(-> (cylinder (/ BIG_BUTTON_WIDTH 2) 1000)
				    (move x y z) ))))))
(defn bottom [] 
   (difference-all 
	(cube LID_WIDTH LID_DEPTH LID_HEIGHT) 
        (move-z 
            (cube 
               (-  LID_WIDTH (* 1 WALL_THICKNESS))
               (- LID_DEPTH (* 1 WALL_THICKNESS)) 
               (- LID_HEIGHT (* 1 WALL_THICKNESS )))
               (/ WALL_THICKNESS 2))
         (-> (cube BATTERY_LENGTH LID_DEPTH 5 )
          	(move (/ LID_WIDTH 4) WALL_THICKNESS (+ (* 2 WALL_THICKNESS) (/ LID_HEIGHT -2)))
          )
               ))
(let []


 (top)
 (bottom)
 	


;(PhysicsEngine/add 
;  (CSGPhysicsManager.
;		(java.util.ArrayList. [(bottom)])
;		(Vector3f. 6 2, 180)
;		0.02
;		core
;		))
		
		
		)