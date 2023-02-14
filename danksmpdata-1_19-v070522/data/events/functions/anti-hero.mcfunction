# dank bank anti hero of the village enforcement

execute as @e[tag=dankbank] at @s run tellraw @p[nbt={ActiveEffects:[{Id:32b}]}, distance=..7] ["","<",{"selector":"@s"},"> ","No heroes please!"]
execute as @e[tag=dankbank] at @s run effect clear @p[nbt={ActiveEffects:[{Id:32b}]}, distance=..7] minecraft:hero_of_the_village

# will most likely teleport player away. 