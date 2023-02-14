// DIFFICULTY PROGRESSION v1

// by crdtrd

// When Ender Dragon is killed for the first time, difficulty is set to normal. When Wither is defeated for the first time, difficulty is set to hard.

// 

(	

	__config() -> (

		{'scope' -> 'global'}

	);



	__on_start() -> (

		//print(read_file('difficulty_progression_data', 'json'));

		run(str('difficulty %s', read_file('difficulty_progression_data', 'json')));

	);

	

	__on_close() -> (

		delete_file('difficulty_progression_data', 'json');

		write_file('difficulty_progression_data', 'json', system_info('game_difficulty'));

	);

	

	__on_player_deals_damage(p, dmg, e) -> (

		//print('punch');

		if((e~'type' == 'ender_dragon') 

			&& (system_info('game_difficulty') == 'easy'), 

			entity_event(e, 'on_death', 'killed_dragon'),

			

			(e~'type' == 'wither') 

			&& (system_info('game_difficulty') != 'hard'), 

			entity_event(e, 'on_death', 'killed_wither'),

		);

	);



	killed_dragon(entity, reason) -> (

		run('difficulty normal');

		display_title(player('all'), 'title', format('m Ender Dragon Defeated!'), 20, 80, 20);

		schedule(120, _()->(display_title(player('all'), 'title', format('m Difficulty Set To Normal!'), 20, 80, 20)));

	);

	

	

	killed_wither(entity, reason) -> (

		run('difficulty hard');

		display_title(player('all'), 'title', format('g Wither Defeated!'), 20, 80, 20);

		schedule(120, _()->(display_title(player('all'), 'title', format('g Difficulty Set To Hard!'), 20, 80, 20)));

	);

)