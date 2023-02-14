(
    // this thing took forever to code, but i did it. it's kind of epic
    __config() -> (

		{'scope' -> 'global'}

	);

    global_furnaces = [];

    // EVENTS
    __on_start() -> (
		furnace_data = read_file('cfr_data', 'json');
        // as to why I have to do this, idek
        for(furnace_data,
             global_furnaces += _;
        );
        //print('drtdrc', global_furnaces);
	);

	__on_close() -> (
		delete_file('cfr_data', 'json');
        write_file('cfr_data', 'json', global_furnaces);
        //print('drtdrc', global_furnaces);
	);

    __on_player_places_block(player, items, hand, block)-> (
        if(
            block == 'furnace' || block == 'smoker' || block == 'blast_furnace'
        ,
            pos = pos(block);
            global_furnaces += {'type' -> block, 'x' -> pos:0, 'y' -> pos:1, 'z' -> pos:2, 'dim' -> player~'dimension', 'cti' -> 1};
            print('drtdrc', global_furnaces);
        );
    );
    
    __on_tick()-> (
        for(global_furnaces,
            in_dimension(_:'dim',
                block = block(_:'x', _:'y', _:'z'); 
                if(
                    (
                        block == 'furnace'
                        || block == 'smoker'
                        || block == 'blast_furnace'
                    )
                ,
                        egap_recipe(_); 
                ,
                    delete(global_furnaces, _i);
                );   
            );
        );
    );

    // RECIPE FUNCTIONS
    egap_recipe(f) -> (
        if(!has(f, 'golden_flame'), 
            put(f, 'golden_flame', false);
        );

        f_state = block_state(f:'x', f:'y', f:'z');
        f_data = parse_nbt(block_data(f:'x', f:'y', f:'z'));
        [rs_index, is_rs_empty] = result_slot_analysis(f_data); 

        if(
            (
                is_rs_empty 
                || (
                    !is_rs_empty
                    && f_data:'Items':rs_index:'id' == 'minecraft:enchanted_golden_apple' 
                    && f_data:'Items':rs_index:'Count' < 64
                )
            ) 
            && (
                f_data:'Items':0:'Slot' == 0
                && f_data:'Items':0:'id' == 'minecraft:apple'
            )
        ,
            if(
                f:'golden_flame' == true
            ,
                if(
                    f_data:'BurnTime' > 1
                ,
                    if(
                       f:'type' == 'furnace'
                    ,
                        if(
                            f:'cti' == 198
                        ,
                            f_data = egap_cooked_actions(f_data, rs_index, is_rs_empty);
                            f:'cti' = 1;
                        ,
                            f:'cti' < 198
                        ,
                            f_data:'CookTime' = f:'cti';
                            f:'cti' += 1;
                        );
                    ,
                        f:'type' == 'smoker'
                    ,
                        if(
                            f:'cti' == 98
                        ,
                            f_data = egap_cooked_actions(f_data, rs_index, is_rs_empty);
                            f:'cti' = 1;
                        ,
                            f:'cti' < 98
                        ,
                            f_data:'CookTime' = f:'cti';
                            f:'cti' += 1;
                        );
                    );
                ,
                    f:'golden_flame' = false;
                );
            ,  
                if(
                    f_data:'Items':1:'tag':'CustomModelData' == 44
                ,
                    f:'golden_flame' = true;
                    f_data:'Items':1:'Count' += -1;
                    if(
                        f:'type' == 'furnace'
                    ,
                        f_data:'BurnTime' = 200;
                    ,
                        f:'type' == 'smoker'
                    ,
                        f_data:'BurnTime' = 100;
                    );
                    f_state:'lit' = true;
                ,
                    f:'cti' > 1 
                ,
                    f:'cti' += -2;
                    f_data:'CookTime' = f:'cti';
                );
            );
            set(f:'x', f:'y', f:'z', f:'type', f_state, encode_nbt(f_data));
        ,
            f:'cti' = 1;
        );
    );


    // HELPER FUNCTIONS
    egap_cooked_actions(f_data, rs_index, is_rs_empty)-> (

        egap_data = {
            'Slot' -> 2, 
            'Count' -> 1,
            'id' -> 'minecraft:enchanted_golden_apple', 
        };

        f_data:'Items':0:'Count' += -1;
        f_data:'CookTime' = 0;

        if(
            is_rs_empty
        ,
            f_data:'Items' += egap_data;
        ,
            f_data:'Items':rs_index:'Count' += 1;
        );
        return(f_data);
    );

    result_slot_analysis(f_data) -> (
        for(f_data:'Items',
            if(
                _:'Slot' == 2
            ,
                return([_i, false]);
            );
        );
        return([null, true]);
    );

);

// if(is_valid_furnace_1000dank_egap(_:0, _:1, _:2, _:3), // nah break this up
//     // need to set burntime, cooktime, and lit
//     b_state = block_state(_:1, _:2, _:3); 
//     b_data = block_data(_:1, _:2, _:3);
//     // might want to add a tag indicating golden flame
//     if(cti <= 200,
//         if(b_state:'lit' == false, put(b_state, 'lit', true));
//         put(b_data, 'BurnTime', bti);  
//         put(b_data, 'CookTime', cti); // have to manually increment/increase cooktime since this is technically not a recipe. should also think about adding to recipe book.
//         set(_:1, _:2, _:3, _:0, b_state, b_data);
//         bti += -1; cti += 1;
//         ,
//         bti = 200; cti = 0;
//         // game will switch it to not lit at end of burn unless there is more to burn. so i dont need to worry about it
//         // subtract 1 apple and 1 1000 dank bill from furnace and add egap as result. decrementing to 0 removes from list
//         // atm this is not consistent with vanilla, as the fuel gets consumed at the end of the operation, instead of the beginning. need to fix that eventually.
//         b_data = parse_nbt(b_data);
//         b_data:'Items':0:'Count' += -1;
//         b_data:'Items':1:'Count' += -1;
//         if(length(b_data:'Items') == 2,
//             // add new item to end of items
//             b_data:'Items' += 
//             {
//                 'Slot' -> 2, 
//                 'Count' -> 1,
//                 'id' -> 'minecraft:paper', 
//                 'tag' -> {
//                     'CustomModelData' -> 44, 
//                     'display' -> {
//                         'Name' -> {
//                             '"text"' -> '"¢1000 Dank"',
//                             '"color"' -> '"gold"',
//                             '"italic"' -> false
//                         }
//                     }
//                 }
//             }
//             ,
//             // increase count of egap by 1
//             b_data:'Items':2:'Count' += 1;
//         );
//         set(_:1, _:2, _:3, _:0, b_state, encode_nbt(b_data));
//     );
// );

//     is_valid_furnace_1000dank_egap(furnace_type, x, y, z) -> (
//         b_data = parse_nbt(block_data(x, y, z));
//         return(    
//             (furnace_type == 'furnace' || furnace_type == 'smoker') 
//             && b_data:'BurnTime' == 0 
//             && b_data:'CookTime' == 0 
//             // assuming Items is sorted by slot number
//             && b_data:'Items':0:'id' == 'minecraft:apple' // in ingredient slot
//             && b_data:'Items':1:'tag':'CustomModelData' == 44 // 1000 dank bill in fuel slot
//             && ((length(b_data:'Items') == 2) || 
//                 (
//                     b_data:'Items':2:'Slot' == 2 
//                     && b_data:'Items':2:'id' == 'minecraft:enchanted_golden_apple' 
//                     && b_data:'Items':2:'Count' < 64
//                 )
//             ) // result slot is empty or has egap
//         );
//     );

// flow chart??:
// -> (result slot empty || (result slot is egap && result slot not full)) && ingredient is apple
//      -> golden_flame is true
//          -> burntime is not 0
//              -> its a furnace
//                  -> cooktime is 200
//                      * remove apple, add egap, set cooktime to 0
//                  -> cooktime < 200
//                      * increment cooktime
//              -> its a smoker
//                  -> cooktime is 100
//                      * remove apple, add egap, set cooktime to 0
//                  -> cooktime < 100
//                      * increment cooktime
//          -> burntime is 0
//              * set golden_flame false, lit property goes false automatically
//      -> golden_flame is false
//          -> fuel is 1kdank
//              * set golden_flame to true, remove 1kdank
//              -> its a furnace
//                  * set burntime to 200, light furnace
//              -> its a smoker
//                  * set burntime to 100, light smoker
//          -> fuel is not 1kdank
//              * do not begin cooking process