// AFK v071021



(

	global_last_session_info = ['No Info','-','-'];

	

	__config() -> {

		'command_permission' -> 'all',

		

		'commands' -> {

			'' -> _() -> (afk_command()),

			

			'last_session_info' -> _() -> (
				// this would break if you could run the command as your shadow, which shouldn't be possible
				global_last_session_info = read_file(str('afk_data_' + player()), 'json');

				print('Last Session Info for ' + player() + ':');

				for(global_last_session_info,

					print(_);

				);

			);

			

		};

	};

	

	__on_start() -> (

		team_add('AFK');

		team_property('AFK','color', 'gray');

	);

	

	afk_command() -> (

		p = player();

		name = p~'command_name';

		run('player ' + name + ' shadow');
		
		p=player(name);
		
		// probably don't need this conditional, but I guess it helps show what's going on
		if(query(p, 'player_type') == 'shadow', 
				//print('drtdrc','detected bot');
				team_add('AFK', p);

				put(global_last_session_info, 0, str('Bot connected at ' + get_time_str()));
		);

	);
	
	// __on_player_connects(p)-> (
	// 	global_last_session_info = read_file(str('afk_data_' + p), 'json');
	// )

	__on_player_disconnects(p, reason) -> (

		if(query(p, 'player_type') == 'shadow',

			put(global_last_session_info, 1, str('Bot disconnected at ' + get_time_str()));

			save_info_to_file();

			team_leave(p);

		);

	);

	

	get_time_str() -> (

		dt = convert_date(unix_time());

		return(str('%02d:%02d, %d/%d/%d', dt:3, dt:4, dt:1, dt:2, dt:0));

	);

	

	save_info_to_file() -> (

		delete_file(str('afk_data_' + player()), 'json');

		write_file(str('afk_data_' + player()), 'json', global_last_session_info);

	);

);