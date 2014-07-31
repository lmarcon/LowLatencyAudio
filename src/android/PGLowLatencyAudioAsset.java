/*
THIS SOFTWARE IS PROVIDED BY ANDREW TRICE "AS IS" AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
EVENT SHALL ANDREW TRICE OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.phonegap;

import java.lang.reflect.InvocationTargetException;
import java.lang.IllegalAccessException;
import java.lang.NoSuchMethodException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.res.AssetFileDescriptor;

public class PGLowLatencyAudioAsset
{

	private ArrayList<PGPolyphonicVoice> voices;
	private int playIndex = 0;

	private int voicesLoaded = 0;
	private Callback loadCallback;
	
	public PGLowLatencyAudioAsset(AssetFileDescriptor afd, int numVoices, Callback callback) throws IOException
	{
		voices = new ArrayList<PGPolyphonicVoice>();
		
		if ( numVoices < 0 )
			numVoices = 0;
		
		loadCallback = callback;
		Callback voiceCallback = new Callback(this, "onVoiceLoaded");
		for ( int x=0; x<numVoices; x++) 
		{
			PGPolyphonicVoice voice = new PGPolyphonicVoice(afd, voiceCallback);
			voices.add( voice );
		}
	}

	public void onVoiceLoaded() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
	{
		if(++voicesLoaded >= voices.size())
		{
			if(loadCallback != null)
			{
				loadCallback.invoke();
				loadCallback = null;
			}
		}
	}
	
	public int play() throws IOException
	{
		PGPolyphonicVoice voice = voices.get(playIndex);
		voice.play();
		playIndex++;
		playIndex = playIndex % voices.size();
		return playIndex;
	}
	
	public void stop() throws IOException
	{
		for ( int x=0; x<voices.size(); x++) 
		{
			PGPolyphonicVoice voice = voices.get(x);
			voice.stop();
		}
	}

	public void stop(int index) throws IOException
	{
		if(index >= 0 && index < voices.size())
		{
			PGPolyphonicVoice voice = voices.get(index);
			voice.stop();
		}
	}
	
	public int loop() throws IOException
	{
		PGPolyphonicVoice voice = voices.get(playIndex);
		voice.loop();
		playIndex++;
		playIndex = playIndex % voices.size();
		return playIndex;
	}
	
	public void unload() throws IOException
	{
		this.stop();
		for ( int x=0; x<voices.size(); x++)
		{
			PGPolyphonicVoice voice = voices.get(x);
			voice.unload();
		}
		voices.removeAll(voices);
	}

	public void setVolume(float volume) throws IOException
	{
		for ( int x=0; x<voices.size(); x++)
		{
			PGPolyphonicVoice voice = voices.get(x);
			voice.setVolume(volume);
		}
	}

	public void setVolume(int index, float volume) throws IOException
	{
		if(index >= 0 && index < voices.size())
		{
			PGPolyphonicVoice voice = voices.get(index);
			voice.setVolume(volume);
		}
	}
	
	public void pause() throws IOException
	{
		for ( int x=0; x<voices.size(); x++) 
		{
			PGPolyphonicVoice voice = voices.get(x);
			voice.pause();
		}
	}

	public void pause(int index) throws IOException
	{
		if(index >= 0 && index < voices.size())
		{
			PGPolyphonicVoice voice = voices.get(index);
			voice.pause();
		}
	}

	public int getDuration() throws IOException
	{
		PGPolyphonicVoice voice = voices.get(0);
		return voice.getDuration();
	}

	public int getPosition(int index) throws IOException
	{
		if(index >= 0 && index < voices.size())
		{
			PGPolyphonicVoice voice = voices.get(index);
			return voice.getPosition();
		}
		return 0;
	}
}
