// detects wanering trader spawns and adds 2 random dankbuck exchanges, one going down and one going up.



__config()->( 

    { 'scope'-> 'global' };

);



entity_load_handler('wandering_trader', 

    _(e,new) -> (

        if(new, 

            trader_nbt = query(e, 'nbt');

            upward_trade = random_upward_trade(e);

            downward_trade = random_downward_trade(e);

            put(trader_nbt, 'Offers.Recipes', upward_trade, -1);

            put(trader_nbt, 'Offers.Recipes', downward_trade, -1);

            //print('drtdrc', trader_nbt);

            modify(e, 'nbt', trader_nbt);

        );



    );

);



random_upward_trade(e) -> (

    //Offers.Recipes

    upward_trade_list = [

        nbt('{rewardExp: 0b, maxUses: 2147483647, buy: {id: "minecraft:paper", Count: 10b, tag: {display: {Name: ' + escape_nbt('{"text":"¢1 Dank","color":"green","italic":false}') + '}, CustomModelData: 41b}}, sell: {id: "minecraft:paper", Count: 1b, tag: {display: {Name: ' + escape_nbt('{"text":"¢10 Dank","color":"yellow","italic":false}') + '}, CustomModelData: 42b}}}'),

        nbt('{rewardExp: 0b, maxUses: 2147483647, buy: {id: "minecraft:paper", Count: 10b, tag: {display: {Name: ' + escape_nbt('{"text":"¢10 Dank","color":"yellow","italic":false}') + '}, CustomModelData: 42b}}, sell: {id: "minecraft:paper", Count: 1b, tag: {display: {Name: ' + escape_nbt('{"text":"¢100 Dank","color":"aqua","italic":false}') + '}, CustomModelData: 43b}}}'),

        nbt('{rewardExp: 0b, maxUses: 2147483647, buy: {id: "minecraft:paper", Count: 10b, tag: {display: {Name: ' + escape_nbt('{"text":"¢100 Dank","color":"aqua","italic":false}') + '}, CustomModelData: 43b}}, sell: {id: "minecraft:paper", Count: 1b, tag: {display: {Name: ' + escape_nbt('{"text":"¢1000 Dank","color":"gold","italic":false}') + '}, CustomModelData: 44b}}}')

    ];

    return(upward_trade_list:rand(3));

);



random_downward_trade(e) -> (

    downward_trade_list = [

        nbt('{rewardExp: 0b, maxUses: 2147483647, buy: {id: "minecraft:paper", Count: 1b, tag: {display: {Name: ' + escape_nbt('{"text":"¢1000 Dank","color":"gold","italic":false}') + '}, CustomModelData: 44b}}, sell: {id: "minecraft:paper", Count: 10b, tag: {display: {Name: ' + escape_nbt('{"text":"¢100 Dank","color":"aqua","italic":false}') + '}, CustomModelData: 43b}}}'),

        nbt('{rewardExp: 0b, maxUses: 2147483647, buy: {id: "minecraft:paper", Count: 1b, tag: {display: {Name: ' + escape_nbt('{"text":"¢100 Dank","color":"aqua","italic":false}') + '}, CustomModelData: 43b}}, sell: {id: "minecraft:paper", Count: 10b, tag: {display: {Name: ' + escape_nbt('{"text":"¢10 Dank","color":"yellow","italic":false}') + '}, CustomModelData: 42b}}}'),

        nbt('{rewardExp: 0b, maxUses: 2147483647, buy: {id: "minecraft:paper", Count: 1b, tag: {display: {Name: ' + escape_nbt('{"text":"¢10 Dank","color":"yellow","italic":false}') + '}, CustomModelData: 42b}}, sell: {id: "minecraft:paper", Count: 10b, tag: {display: {Name: ' + escape_nbt('{"text":"¢1 Dank","color":"green","italic":false}') + '}, CustomModelData: 41b}}}')

    ];

    return(downward_trade_list:rand(3));

);