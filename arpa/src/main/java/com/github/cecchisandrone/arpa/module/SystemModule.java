package com.github.cecchisandrone.arpa.module;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.cecchisandrone.raspio.input.JoypadController;
import com.github.cecchisandrone.raspio.input.JoypadController.Analog;
import com.github.cecchisandrone.raspio.input.JoypadController.Button;
import com.github.cecchisandrone.raspio.input.JoypadEvent;
import com.github.cecchisandrone.raspio.input.JoypadEventListener;

public class SystemModule extends AbstractAgentModule implements JoypadEventListener {

	@Autowired
	private JoypadController joypadController;

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemModule.class);

	@Override
	public void init() {
		super.init();

		// Joypad initialization
		joypadController.addEventListener(this);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				joypadController.disconnect();
			}
		});
		joypadController.connect();
	}

	@Override
	public void executeWork() {
	}

	public List<Button> getButtonsToNotify() {
		return new ArrayList<JoypadController.Button>();
	}

	public List<Analog> getAnalogsToNotify() {
		return new ArrayList<JoypadController.Analog>();
	}

	@Override
	public void joypadEventTriggered(JoypadEvent event) {

	}
}
