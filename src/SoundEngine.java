import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * SoundEngine class handles the loading, playing, and controlling of sound
 * effects.
 */
public class SoundEngine {

	/*
	 * Initalize volume
	 */
	float volume;
	/*
	 * Initalize forrestSounds
	 */
	List<Clip> forrestSounds;
	/*
	 * Initalize cameraSounds
	 */
	List<Clip> cameraSounds;
	/*
	 * Initalize ambientSounds
	 */
	List<Clip> ambientSounds;
	/*
	 * Initalize scarySounds
	 */
	List<Clip> scarySounds;

	/**
	 * Constructor for the SoundEngine. Initializes sound lists and default volume.
	 */
	public SoundEngine() {
		volume = (float) 1.0;
		forrestSounds = new ArrayList<Clip>();
		cameraSounds = new ArrayList<Clip>();
		ambientSounds = new ArrayList<Clip>();
		scarySounds = new ArrayList<Clip>();
	}

	// Getter for forrestSounds
	public List<Clip> getForrestSounds() {
		return forrestSounds;
	}

	// Getter for cameraSounds
	public List<Clip> getCameraSounds() {
		return cameraSounds;
	}

	// Getter for ambientSounds
	public List<Clip> getAmbientSounds() {
		return ambientSounds;
	}

	// Getter for scarySounds
	public List<Clip> getScarySounds() {
		return scarySounds;
	}

	/**
	 * Initializes and loads all the sound effects into their respective lists.
	 */
	public void constructSounds() {
		ambientSounds.add(loadSound("mainmenu.wav")); // 0
		ambientSounds.add(loadSound("office_ambience_reduced.wav")); // 1
		ambientSounds.add(loadSound("cheer.wav")); // 2
		ambientSounds.add(loadSound("night1phone.wav")); // 3
		ambientSounds.add(loadSound("night2phone.wav")); // 4
		ambientSounds.add(loadSound("night3phone.wav")); // 5
		ambientSounds.add(loadSound("night4phone.wav")); // 6

		scarySounds.add(loadSound("laugh1.wav")); // 0
		scarySounds.add(loadSound("laugh2.wav")); // 1
		scarySounds.add(loadSound("laugh3.wav")); // 2
		scarySounds.add(loadSound("laugh4.wav")); // 3
		scarySounds.add(loadSound("circus.wav")); // 4
		scarySounds.add(loadSound("robotvoice.wav")); // 5
		scarySounds.add(loadSound("ambience2.wav")); // 6
		scarySounds.add(loadSound("estellemeow.wav")); // 7
		scarySounds.add(loadSound("estellehiss.wav")); // 8
		scarySounds.add(loadSound("estellegrowl.wav")); // 9
		scarySounds.add(loadSound("forrestfreddy.wav")); // 10
		scarySounds.add(loadSound("forrestrobot.wav")); // 11
		scarySounds.add(loadSound("forrestpa.wav")); // 12
		scarySounds.add(loadSound("forresthello.wav")); // 13
		scarySounds.add(loadSound("forrestfuckyou.wav")); // 14
		scarySounds.add(loadSound("forrestgoldenscream.wav")); // 15
		scarySounds.add(loadSound("forrestscooby.wav")); // 16
		scarySounds.add(loadSound("forrestscreamwah.wav")); // 17
		scarySounds.add(loadSound("forrestwhat.wav")); // 18
		scarySounds.add(loadSound("forrestwoop.wav")); // 19
		scarySounds.add(loadSound("tiktokaudio.wav")); // 20
		scarySounds.add(loadSound("estellegiggle.wav")); // 21
		scarySounds.add(loadSound("estellemeow2.wav")); // 22
		scarySounds.add(loadSound("babygirl.wav")); // 23
		scarySounds.add(loadSound("scarysound1.wav")); // 24
		scarySounds.add(loadSound("scarysound2.wav")); // 25
		scarySounds.add(loadSound("scarysound3.wav")); // 26
		scarySounds.add(loadSound("scarysound4.wav")); // 27
		scarySounds.add(loadSound("scarysound5.wav")); // 28
		scarySounds.add(loadSound("scarysound6.wav")); // 29

		cameraSounds.add(loadSound("blip.wav")); // 0
		cameraSounds.add(loadSound("door.wav")); // 1
		cameraSounds.add(loadSound("light.wav")); // 2
		cameraSounds.add(loadSound("camera_ambience.wav")); // 3
		cameraSounds.add(loadSound("camera_open.wav")); // 4
		cameraSounds.add(loadSound("camera_close.wav")); // 5
		cameraSounds.add(loadSound("kitchen1.wav")); // 6
		cameraSounds.add(loadSound("kitchen2.wav")); // 7
		cameraSounds.add(loadSound("kitchen3.wav")); // 8
		cameraSounds.add(loadSound("kitchen4.wav")); // 9
		cameraSounds.add(loadSound("disabled.wav")); // 10

		forrestSounds.add(loadSound("door_pound.wav")); // 0
		forrestSounds.add(loadSound("small_scare.wav")); // 1
		forrestSounds.add(loadSound("movement1.wav")); // 2
		forrestSounds.add(loadSound("movement2.wav")); // 3
		forrestSounds.add(loadSound("jumpscare.wav")); // 4
		forrestSounds.add(loadSound("sprinting.wav")); // 5
		forrestSounds.add(loadSound("fartgun.wav")); // 6
		forrestSounds.add(loadSound("griddy.wav")); // 7
	}

	/**
	 * Loads a sound clip from a file.
	 * 
	 * @param soundFileName Name of the sound file to load.
	 * @return The loaded sound clip.
	 */
	private Clip loadSound(String soundFileName) {
		Clip clip = null;
		try {
			InputStream soundInputStream = this.getClass().getClassLoader()
					.getResourceAsStream("sounds/" + soundFileName);

			if (soundInputStream != null) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundInputStream);
				clip = AudioSystem.getClip();
				clip.open(audioInput);

				clip = repeatSound(clip);
			} else {
				System.err.println(soundFileName + " could not be found.");
			}
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			System.err.println(soundFileName + " could not be loaded due to exception");
			e.printStackTrace();
		}
		return clip;
	}

	/**
	 * Makes a clip repeat from the beginning when it reaches the end.
	 * 
	 * @param clip The clip to be set for repetition.
	 * @return The clip with repetition set.
	 */
	private Clip repeatSound(Clip clip) {
		clip.addLineListener(event -> {
			if (event.getType() == LineEvent.Type.STOP) {
				clip.stop();
				clip.setFramePosition(0);
			}
		});
		return clip;
	}

	/**
	 * Plays a given sound clip with specified volume and repetition settings.
	 * 
	 * @param clip     The clip to be played.
	 * @param volume   The volume at which the clip should be played.
	 * @param playOnce Flag to determine if the sound should be played once or
	 *                 looped continuously.
	 */
	private void playSound(Clip clip, float volume, boolean playOnce) {
		if (clip != null) {
			try {
				clip.stop();
				clip.setFramePosition(0);
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
				gainControl.setValue(dB);
				if (playOnce) {
					clip.start();
				} else {
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				}
			} catch (Exception e) {
				System.out.println("Tried to play sound but exception occured with " + clip + " !");
			}
		} else {
			System.out.println("Tried to play sound but clip is null.");
		}
	}

	/**
	 * Retrieves the volume of the sound engine.
	 * 
	 * @return The volume as an integer (multiplied by 100 for precision).
	 */
	public int getVolume() {
		return (int) (volume * 100);
	}

	/**
	 * Sets the volume for all loaded sounds.
	 * 
	 * @param sound The desired volume (out of 100).
	 */
	public void setVolume(int sound) {
		volume = sound / 100f;

		changeVolumeForAllSounds(forrestSounds);
		changeVolumeForAllSounds(cameraSounds);
		changeVolumeForAllSounds(ambientSounds);
		changeVolumeForAllSounds(scarySounds);
	}

	/**
	 * Changes the volume of all sound clips.
	 * 
	 * @param soundList List of all sound clips.
	 */
	private void changeVolumeForAllSounds(List<Clip> soundList) {
		for (Clip clip : soundList) {
			changeVolume(clip, volume);
		}
	}

	/**
	 * Changes the volume of a specified sound clip.
	 * 
	 * @param clip   The sound clip for which the volume needs to be adjusted.
	 * @param volume The desired volume.
	 */
	private void changeVolume(Clip clip, float volume) {
		if (clip != null && clip.isRunning()) {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
		}
	}

	/**
	 * Plays an ambientSound.
	 * 
	 * @param sound the sound to be played
	 * @flag true = play once, false = played repeatedly
	 */
	public void playAmbientSound(int sound, boolean flag) {
		playSound(ambientSounds.get(sound), volume, flag);
	}

	/**
	 * Plays a forrestSound.
	 * 
	 * @param sound the sound to be played
	 * @flag true = play once, false = played repeatedly
	 */
	public void playForrestSound(int sound, boolean flag) {
		playSound(forrestSounds.get(sound), volume, flag);
	}

	/**
	 * Plays a cameraSound.
	 * 
	 * @param sound the sound to be played
	 * @flag true = play once, false = played repeatedly
	 */
	public void playCameraSound(int sound, boolean flag) {
		playSound(cameraSounds.get(sound), volume, flag);
	}

	/**
	 * Plays a scarySound.
	 * 
	 * @param sound the sound to be played
	 * @flag true = play once, false = played repeatedly
	 */
	public void playScarySound(int sound, boolean flag) {
		playSound(scarySounds.get(sound), volume, flag);
	}

	/**
	 * Stops a currently playing ambientSound.
	 * 
	 * @param number The sound to be stopped
	 */
	public void stopAmbientSound(int number) {
		ambientSounds.get(number).stop();
	}

	/**
	 * Stops a currently playing cameraSound.
	 * 
	 * @param number The sound to be stopped
	 */
	public void stopCameraSound(int number) {
		cameraSounds.get(number).stop();
	}

	/**
	 * Stops a currently playing scarySound.
	 * 
	 * @param number The sound to be stopped
	 */
	public void stopScarySound(int number) {
		scarySounds.get(number).stop();
	}

	/**
	 * Stops a currently playing forrestSound.
	 * 
	 * @param number The sound to be stopped
	 */
	public void stopForrestSound(int number) {
		forrestSounds.get(number).stop();
	}

	/**
	 * Stops all currently playing sounds across all categories.
	 */
	public void stopAllSounds() {
		stopAllSounds(forrestSounds);
		stopAllSounds(cameraSounds);
		stopAllSounds(ambientSounds);
		stopAllSounds(scarySounds);
	}

	/**
	 * Stops all currently playing sounds from a specific list of sound clips.
	 * 
	 * @param soundList The list of sound clips to be stopped.
	 */
	public void stopAllSounds(List<Clip> soundList) {
		for (Clip clip : soundList) {
			if (clip.isRunning()) {
				clip.stop();
			}
		}
	}

}
