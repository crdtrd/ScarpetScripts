# Eat a cookie detector
scoreboard objectives add DS_CookieE minecraft.used:minecraft.cookie
# Time score
scoreboard objectives add Time dummy
# look detection range
scoreboard objectives add LookRange dummy

# gamerules
gamerule spawnRadius 0
gamerule doFireTick false
gamerule playersSleepingPercentage 0

# Manually loading scarpet scripts cuz buggy i guess
script load afk
script load custom_furnace_recipes
script load danktrader
script load difficulty_progression
script load electric_wrench
script load invisible_item_frame_toggle
script load loaderstone