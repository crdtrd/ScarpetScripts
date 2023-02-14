// LOADERSTONE v1



//give @p player_head{display:{Name:'{"text":"Loaderstone","color":"light_purple","italic":false}',Lore:['{"text":"Loads the chunk","color":"gray"}','{"text":"it is placed in","color":"gray"}']},SkullOwner:{Id:[I;-1603067143,-88585229,-1777822630,-164656805],Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7ImlkIjoiYmRkNmU1MzM2NDNkNDhmN2E5YWYwNjBkMzFkYmM3NmUiLCJ0eXBlIjoiU0tJTiIsInVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJjNjE3ODM1OTBiNmU2ODQ3MzAwNWIzMmVjMGEwOGJjM2I5NWQ2NDkwMzZiMzU4YWZlNzdhZDhhZjg4NDgwMCIsInByb2ZpbGVJZCI6IjgwMThhYjAwYjJhZTQ0Y2FhYzliZjYwZWY5MGY0NWU1IiwidGV4dHVyZUlkIjoiZjJjNjE3ODM1OTBiNmU2ODQ3MzAwNWIzMmVjMGEwOGJjM2I5NWQ2NDkwMzZiMzU4YWZlNzdhZDhhZjg4NDgwMCJ9fSwic2tpbiI6eyJpZCI6ImJkZDZlNTMzNjQzZDQ4ZjdhOWFmMDYwZDMxZGJjNzZlIiwidHlwZSI6IlNLSU4iLCJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2YyYzYxNzgzNTkwYjZlNjg0NzMwMDViMzJlYzBhMDhiYzNiOTVkNjQ5MDM2YjM1OGFmZTc3YWQ4YWY4ODQ4MDAiLCJwcm9maWxlSWQiOiI4MDE4YWIwMGIyYWU0NGNhYWM5YmY2MGVmOTBmNDVlNSIsInRleHR1cmVJZCI6ImYyYzYxNzgzNTkwYjZlNjg0NzMwMDViMzJlYzBhMDhiYzNiOTVkNjQ5MDM2YjM1OGFmZTc3YWQ4YWY4ODQ4MDAifSwiY2FwZSI6bnVsbH0="}]}}} 1



(

	__config() -> (

		{'scope' -> 'global'}

	);

	

	// 0 = Overworld, 1 = Nether, 2 = End

	global_loaderstones = l(l(), l(), l());

	global_dim_strs = l('overworld', 'the_nether', 'the_end');





	// EVENTS

	

	__on_start() -> (

		loaderstones_data = read_file('loaderstone_data', 'json');

		

		for(loaderstones_data,

			dim = _;

			dI = _i;

			//print(dI);

			for(dim, 

				global_loaderstones:dI += _;

			)

		);

	);

	

	__on_close() -> (

		// for every loaderstone in each dimension

		for(global_loaderstones,

			in_dimension(global_dim_strs:_i,

				unload_all_loaderstone_chunks(_);

			)

		

		);

		delete_file('loaderstone_data', 'json');

		write_file('loaderstone_data', 'json', global_loaderstones);

	);

	

	__on_tick() -> (

		// epic item drop

		fix_loaderstone_item_display();

	

		//for every loaderstone in each dimension

		for (global_loaderstones,

			in_dimension(global_dim_strs:_i,

				clean_up_gone_loaderstones(_, _i);

				spawn_particles(_);

				load_chunk(_);

			)

		);

		

	);



	__on_player_places_block(player, item_tuple, hand, block) -> (

		if (is_block_loaderstone(block), 

			loaderstone_placed_actions(block)

		)

	);







	// GENERAL FUNCTIONS

	is_block_loaderstone(block) -> (

		blockData = parse_nbt(block_data(pos(block)));



		// uses the lodestone head texture to identify. as far as I know this is the only way

		headItemID = blockData:'SkullOwner':'Properties':'textures':0:'Value';



		if(headItemID == 'eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7ImlkIjoiYmRkNmU1MzM2NDNkNDhmN2E5YWYwNjBkMzFkYmM3NmUiLCJ0eXBlIjoiU0tJTiIsInVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJjNjE3ODM1OTBiNmU2ODQ3MzAwNWIzMmVjMGEwOGJjM2I5NWQ2NDkwMzZiMzU4YWZlNzdhZDhhZjg4NDgwMCIsInByb2ZpbGVJZCI6IjgwMThhYjAwYjJhZTQ0Y2FhYzliZjYwZWY5MGY0NWU1IiwidGV4dHVyZUlkIjoiZjJjNjE3ODM1OTBiNmU2ODQ3MzAwNWIzMmVjMGEwOGJjM2I5NWQ2NDkwMzZiMzU4YWZlNzdhZDhhZjg4NDgwMCJ9fSwic2tpbiI6eyJpZCI6ImJkZDZlNTMzNjQzZDQ4ZjdhOWFmMDYwZDMxZGJjNzZlIiwidHlwZSI6IlNLSU4iLCJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2YyYzYxNzgzNTkwYjZlNjg0NzMwMDViMzJlYzBhMDhiYzNiOTVkNjQ5MDM2YjM1OGFmZTc3YWQ4YWY4ODQ4MDAiLCJwcm9maWxlSWQiOiI4MDE4YWIwMGIyYWU0NGNhYWM5YmY2MGVmOTBmNDVlNSIsInRleHR1cmVJZCI6ImYyYzYxNzgzNTkwYjZlNjg0NzMwMDViMzJlYzBhMDhiYzNiOTVkNjQ5MDM2YjM1OGFmZTc3YWQ4YWY4ODQ4MDAifSwiY2FwZSI6bnVsbH0=',

			return(true), return(false));

	);



	get_loaderstone_pos_index(lsPos, dI) -> (

		for(global_loaderstones:dI, 

			if(_ == lsPos,

				return(_i)

			)

		)

	);



	get_dimension_index(block) -> (

		if(current_dimension() == 'overworld', return(0),

			current_dimension() == 'the_nether', return(1),

			current_dimension() == 'the_end', return(2)

		)

	);

	

	get_dimension_string(dI) -> (

		if(dI == 0, return('overworld'),

			dI == 1, return('the_nether'),

			dI == 2, return('the_end')

		)

	);







	// LOADERSTONE REMOVAL FUNCTIONS

	clean_up_gone_loaderstones(dim, dI) -> (

		for(dim,

			ls = block(_);

			if(!is_block_loaderstone(ls),

				x = _:0;

				z = _:2;

				run('forceload remove ' + x + ' ' + z);

				delete(global_loaderstones:dI, _i);

			);

		)

	);

	

	unload_all_loaderstone_chunks(dim) -> (

		for(dim,

			x = _:0;

			z = _:2;

			run('forceload remove ' + x + ' ' + z);

		)

	);

	

	// LOADERSTONE ADD FUNCTIONS

	load_chunk(dim) -> (

		for(dim,

			x = _:0;

			z = _:2;

			run('forceload add ' + x + ' ' + z);

		)

		

	);

	

	spawn_particles(dim) -> (

		for (dim,

			x = _:0;

			y = _:1 + 0.5;

			z = _:2;

			run('particle minecraft:portal ' + x + ' ' + y + ' ' + z + ' 0.1 0.1 0.1 0.2 1');

		)

	);

	

	

	

	// PLACE FUNCTIONS

	loaderstone_placed_actions(ls) -> (

		dI = get_dimension_index(ls);

		lsPos = pos(ls);

		(global_loaderstones:dI) += lsPos;

		//print(global_loaderstones);

	);







	// FIXING ITEM DROPS FUNCTIONS

	fix_loaderstone_item_display() -> (

		run(

			'data modify entity @e[type=item, nbt={Item:{tag:{SkullOwner:{Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7ImlkIjoiYmRkNmU1MzM2NDNkNDhmN2E5YWYwNjBkMzFkYmM3NmUiLCJ0eXBlIjoiU0tJTiIsInVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJjNjE3ODM1OTBiNmU2ODQ3MzAwNWIzMmVjMGEwOGJjM2I5NWQ2NDkwMzZiMzU4YWZlNzdhZDhhZjg4NDgwMCIsInByb2ZpbGVJZCI6IjgwMThhYjAwYjJhZTQ0Y2FhYzliZjYwZWY5MGY0NWU1IiwidGV4dHVyZUlkIjoiZjJjNjE3ODM1OTBiNmU2ODQ3MzAwNWIzMmVjMGEwOGJjM2I5NWQ2NDkwMzZiMzU4YWZlNzdhZDhhZjg4NDgwMCJ9fSwic2tpbiI6eyJpZCI6ImJkZDZlNTMzNjQzZDQ4ZjdhOWFmMDYwZDMxZGJjNzZlIiwidHlwZSI6IlNLSU4iLCJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2YyYzYxNzgzNTkwYjZlNjg0NzMwMDViMzJlYzBhMDhiYzNiOTVkNjQ5MDM2YjM1OGFmZTc3YWQ4YWY4ODQ4MDAiLCJwcm9maWxlSWQiOiI4MDE4YWIwMGIyYWU0NGNhYWM5YmY2MGVmOTBmNDVlNSIsInRleHR1cmVJZCI6ImYyYzYxNzgzNTkwYjZlNjg0NzMwMDViMzJlYzBhMDhiYzNiOTVkNjQ5MDM2YjM1OGFmZTc3YWQ4YWY4ODQ4MDAifSwiY2FwZSI6bnVsbH0="}]}}}}}, limit=1, tag=!Loaderstone] Item.tag.display.Name set value ' + escape_nbt('{"text":"Loaderstone","color":"light_purple","italic":false}')

		);

		

		run(

			'data modify entity @e[type=item, nbt={Item:{tag:{SkullOwner:{Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7ImlkIjoiYmRkNmU1MzM2NDNkNDhmN2E5YWYwNjBkMzFkYmM3NmUiLCJ0eXBlIjoiU0tJTiIsInVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJjNjE3ODM1OTBiNmU2ODQ3MzAwNWIzMmVjMGEwOGJjM2I5NWQ2NDkwMzZiMzU4YWZlNzdhZDhhZjg4NDgwMCIsInByb2ZpbGVJZCI6IjgwMThhYjAwYjJhZTQ0Y2FhYzliZjYwZWY5MGY0NWU1IiwidGV4dHVyZUlkIjoiZjJjNjE3ODM1OTBiNmU2ODQ3MzAwNWIzMmVjMGEwOGJjM2I5NWQ2NDkwMzZiMzU4YWZlNzdhZDhhZjg4NDgwMCJ9fSwic2tpbiI6eyJpZCI6ImJkZDZlNTMzNjQzZDQ4ZjdhOWFmMDYwZDMxZGJjNzZlIiwidHlwZSI6IlNLSU4iLCJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2YyYzYxNzgzNTkwYjZlNjg0NzMwMDViMzJlYzBhMDhiYzNiOTVkNjQ5MDM2YjM1OGFmZTc3YWQ4YWY4ODQ4MDAiLCJwcm9maWxlSWQiOiI4MDE4YWIwMGIyYWU0NGNhYWM5YmY2MGVmOTBmNDVlNSIsInRleHR1cmVJZCI6ImYyYzYxNzgzNTkwYjZlNjg0NzMwMDViMzJlYzBhMDhiYzNiOTVkNjQ5MDM2YjM1OGFmZTc3YWQ4YWY4ODQ4MDAifSwiY2FwZSI6bnVsbH0="}]}}}}}, limit=1, tag=!Loaderstone] Item.tag.display.Lore set value ' + '[' + escape_nbt('{"text":"Loads the chunk","color":"gray"}') + ',' + escape_nbt('{"text":"it is placed in","color":"gray"}') + ']'

		);



		run(

			'tag @e[type=item, nbt={Item:{tag:{SkullOwner:{Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7ImlkIjoiYmRkNmU1MzM2NDNkNDhmN2E5YWYwNjBkMzFkYmM3NmUiLCJ0eXBlIjoiU0tJTiIsInVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJjNjE3ODM1OTBiNmU2ODQ3MzAwNWIzMmVjMGEwOGJjM2I5NWQ2NDkwMzZiMzU4YWZlNzdhZDhhZjg4NDgwMCIsInByb2ZpbGVJZCI6IjgwMThhYjAwYjJhZTQ0Y2FhYzliZjYwZWY5MGY0NWU1IiwidGV4dHVyZUlkIjoiZjJjNjE3ODM1OTBiNmU2ODQ3MzAwNWIzMmVjMGEwOGJjM2I5NWQ2NDkwMzZiMzU4YWZlNzdhZDhhZjg4NDgwMCJ9fSwic2tpbiI6eyJpZCI6ImJkZDZlNTMzNjQzZDQ4ZjdhOWFmMDYwZDMxZGJjNzZlIiwidHlwZSI6IlNLSU4iLCJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2YyYzYxNzgzNTkwYjZlNjg0NzMwMDViMzJlYzBhMDhiYzNiOTVkNjQ5MDM2YjM1OGFmZTc3YWQ4YWY4ODQ4MDAiLCJwcm9maWxlSWQiOiI4MDE4YWIwMGIyYWU0NGNhYWM5YmY2MGVmOTBmNDVlNSIsInRleHR1cmVJZCI6ImYyYzYxNzgzNTkwYjZlNjg0NzMwMDViMzJlYzBhMDhiYzNiOTVkNjQ5MDM2YjM1OGFmZTc3YWQ4YWY4ODQ4MDAifSwiY2FwZSI6bnVsbH0="}]}}}}}, limit=1, tag=!Loaderstone] add Loaderstone'	

		)

	)

)