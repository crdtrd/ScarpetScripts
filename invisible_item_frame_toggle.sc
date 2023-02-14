// Invisible Item Frame Toggle v1
// by crdtrd

(
	__on_player_attacks_entity(p, e) -> (
		if(p~'sneaking' && (e~'type' == 'item_frame' || e~'type' == 'glow_item_frame'), 
			(
				//print(e~'type');
				modify(e, 'remove');
				new_frame = spawn(e~'type', e~'pos', e~'nbt');
				//print(parse_nbt(query(new_frame, 'nbt', 'Invisible')) == true);
				if(parse_nbt(query(new_frame, 'nbt', 'Invisible')) == true,
					modify(new_frame, 'nbt_merge', nbt('{Invisible:0b}')),
					modify(new_frame, 'nbt_merge', nbt('{Invisible:1b}'))
				);
			)
		)
	);
)