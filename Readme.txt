Hints: 
- Press load to pre-load a map (you still have to choose
who to hire and the path though)
- Instructions are in dialogue, so you can skip one of them if you want.

Missing Functionalities: 
- Only 1 type of bandit (killing type)
- Only 1 type of guard (only shooting type, no other attack patters)
- Decided to go with endless mode instead of multiplie levels 
(no win condition)

Additional Functionalities: 
- Made the game a lot more stat based (PR stat impacts how many customers & 
bandits come in, num of bandits impacts how fast customers move, etc.)

Known bugs / errors: 
- When we remove a monster from a collection (because they died, or got to
the exit), sometimes it will cause the ConcurrentModificationException--
tried to fix but it was already too integrated into program :-(
- Recalling guards (putting them on standby) sometimes doesn't work because of 
ConcurrentModificationException

Other: 
- You can scroll with your scroll wheel now :D