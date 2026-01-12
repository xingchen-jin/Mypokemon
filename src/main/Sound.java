package main;

import javax.sound.sampled.Clip;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;

public class Sound {
    int MaxSound = 30;
    Clip clip;       //音乐剪辑对象
    public int maxIndex = 7; //最大音乐索引

    URL soundURL[] = new URL[MaxSound];   //存放音效文件路径的数组

    // 音量大小默认为0.8f
    private float volume = 0.8f;
    public int volumeIndex = 3; 
    public float[] volumeValues = {0.0f, 0.5f, 0.7f, 0.8f, 1.0f};

    public Sound(){
        
        soundURL[0] = getClass().getResource("/res/sound/Littleroot_Town.wav");
        soundURL[1] = getClass().getResource("/res/sound/coin.wav");
        soundURL[2] = getClass().getResource("/res/sound/powerup.wav");
        soundURL[3] = getClass().getResource("/res/sound/unlock.wav");
        soundURL[4] = getClass().getResource("/res/sound/fanfare.wav");
        soundURL[5] = getClass().getResource("/res/sound/cursor.wav");  //cursor
        soundURL[6] = getClass().getResource("/res/sound/battle_wild.wav");  //野外战斗音乐

    }


    // 设置音频文件
    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

            setVolume(volume); // 加载后立即应用当前音量    
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void setFile(URL url){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);
            setVolume(volume);  
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void play(){
        clip.start();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        if(clip!=null)
            clip.stop();
    }

    // 设置音量，value范围0.0f~1.0f
    public void setVolume(float value){ 
        volume = Math.max(0.0f, Math.min(1.0f, value));

        if (clip != null) {
            try {
                FloatControl gainControl =
                        (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); //音量控制
                float min = gainControl.getMinimum();
                float max = gainControl.getMaximum();
                float gain = min + (max - min) * volume;
                gainControl.setValue(gain);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
