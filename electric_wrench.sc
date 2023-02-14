p = player();

		name = p~'command_name';

		run('/player ' + name + ' shadow');// ELECTRIC WRENCH v1

// requires /afk



//give @p carrot_on_a_stick{display:{Name:'{"text":"Electric Wrench","color":"aqua","italic":false}',Lore:['{"text":"Short circuits","color":"gray"}','{"text":"(kicks) afk bots","color":"gray"}']},CustomModelData:401,Damage:0,ElectricWrench:1,Charge:0} 1





(

	__config() -> (

		{'scope' -> 'global'}

	);



	//global_largest_wrench_id = 0;

	

	// EVENTS

	__on_player_interacts_with_entity(player, entity, hand) -> (

		item = query(player, 'holds', hand);

		itemNBT = parse_nbt(item:2);

		if(itemNBT:'Damage' == 0 && itemNBT:'ElectricWrench' && query(entity, 'team') == 'AFK',

			run('player ' + entity + ' kill');

			slot = null;

			if(hand=='mainhand', slot = query(player, 'selected_slot'),

				hand=='offhand', slot = -1);

			inventory_set(player, slot, 1, 'carrot_on_a_stick', get_wrench_nbt(0, 25));

		);

	);

	

	__on_tick() -> (

		update_wrenches();

	);

	

	update_wrenches() -> ( 

		ps = player('all');

		for(ps, 

			p = _;

			inv = parse_nbt(query(p, 'nbt', 'Inventory'));

			for(inv,

				ewId = _:'tag':'ElectricWrench';

				slot = _:'Slot';

				dmg = _:'tag':'Damage';

				charge = _:'tag':'Charge';

				if(ewId != null && (query(p, 'player_type') == 'multiplayer'),

					charge_wrench(p, slot, dmg, charge);

				);

			);

		);

	);

	

	

	charge_wrench(p, slot, dmg, charge) -> (

		if(dmg > 0,

			if(charge < 2880,

				charge += 1

				,

				charge >= 2880,

				charge = 0;

				dmg = (dmg - 1);

			);

			inventory_set(p, slot, 1, 'carrot_on_a_stick', get_wrench_nbt(charge, dmg));

		);

	);

	

	get_wrench_nbt(charge, damage) -> (

		return(

			'{display:{Name:' + escape_nbt('{"text":"Electric Wrench","color":"aqua","italic":false}') + ',Lore:[' + escape_nbt('{"text":"Short circuits","color":"gray"}') + ',' + escape_nbt('{"text":"(kicks) afk bots","color":"gray"}') + ']},CustomModelData:401,Damage:' + damage + ',ElectricWrench:1,Charge:' + charge + '}';

		);

	);

)

