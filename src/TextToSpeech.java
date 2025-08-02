// src/TextToSpeech.java

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TextToSpeech {
    private static final String VOICE_NAME = "kevin16";
    private Voice voice;

    public TextToSpeech() {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice(VOICE_NAME);
        if (voice != null) {
            voice.allocate();
        }
    }

    public void speak(String message) {
        if (voice != null) {
            voice.speak(message);
        }
    }

    public void close() {
        if (voice != null) {
            voice.deallocate();
        }
    }
}