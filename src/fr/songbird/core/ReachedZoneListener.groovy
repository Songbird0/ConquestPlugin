package fr.songbird.core

import java.util.EventListener

public interface ReachedZoneListener extends EventListener
{
	void whenZoneHasBeenReached();
}