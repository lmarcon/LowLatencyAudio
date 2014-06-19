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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.IllegalAccessException;
import java.lang.NoSuchMethodException;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

public class PGPolyphonicVoice implements OnPreparedListener, OnCompletionListener
{

	private static final int INVALID = 0;//not loaded
	private static final int PREPARED = 1;//loaded, never played
	private static final int PENDING_PLAY = 2;//preparing, need to play once
	private static final int PLAYING = 3;//playing once
	private static final int PENDING_LOOP = 4;//preparing, need to loop
	private static final int LOOPING = 5;//looping
	private static final int STOPPED = 6;//loaded & played, now stopped
	private static final int PAUSED = 7;//paused during playback
	
	private MediaPlayer mp;
	private int state;
	private Callback loadCallback;
	
	public PGPolyphonicVoice( AssetFileDescriptor afd, Callback callback )  throws IOException
	{
		state = INVALID;
		loadCallback = callback;
		mp = new MediaPlayer();
		mp.setOnPreparedListener(this);
		mp.setOnCompletionListener(this);
		mp.setDataSource( afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);  
		mp.prepare();
	}
	
	public void play() throws IOException
	{
		invokePlay( false );
	}
	
	private void invokePlay( Boolean loop )
	{
		Boolean playing = ( mp.isLooping() || mp.isPlaying() );
		if ( playing )
		{
			mp.pause();
			if(loop)
				state = LOOPING;
			else
				state = PLAYING;
			mp.setLooping(loop);
			mp.seekTo(0);
			mp.start();
		}
		if ( !playing)
		{
			if(state == PREPARED )
			{
				if(loop)
					state = PENDING_LOOP;
				else
					state = PENDING_PLAY;
				onPrepared( mp );
			}
			else if (state == STOPPED || state == PAUSED)
			{
				if(loop)
					state = LOOPING;
				else
					state = PLAYING;
				mp.setLooping(loop);
				mp.start();
			}
			else if(state == INVALID)
			{
				if(loop)
					state = PENDING_LOOP;
				else
					state = PENDING_PLAY;
			}
		}
	}
	
	public void stop() throws IOException
	{
		if ( mp.isLooping() || mp.isPlaying() )
		{
			state = STOPPED;
			mp.pause();
			mp.seekTo(0);
		}
	}
	
	public void loop() throws IOException
	{
		invokePlay( true );
	}
	
	public void unload() throws IOException
	{
		this.stop();
		mp.release();
		state = INVALID;
	}

	public void setVolume(float volume) throws IOException
	{
		mp.setVolume(volume, volume);
	}

	public void pause() throws IOException
	{
		if ( mp.isLooping() || mp.isPlaying() )
		{
			state = PAUSED;
			mp.pause();
		}
	}

	public int getPosition() throws IOException
	{
		if(state == INVALID || state == STOPPED || state == PREPARED)
			return -1;
		else
			return mp.getCurrentPosition();
	}

	public int getDuration() throws IOException
	{
		if(state == INVALID)
			return 0;
		else
			return mp.getDuration();
	}
	
	public void onPrepared(MediaPlayer mPlayer)
	{
		if (state == PENDING_PLAY) 
		{
			mp.setLooping(false);
			mp.seekTo(0);
			mp.start();
			state = PLAYING;
		}
		else if ( state == PENDING_LOOP )
		{
			mp.setLooping(true);
			mp.seekTo(0);
			mp.start();
			state = LOOPING;
		}
		else
		{
			state = PREPARED;
			mp.seekTo(0);
		}
		if(loadCallback != null)
		{
			try
			{
				loadCallback.invoke();
			}
			catch(InvocationTargetException e)
			{
			}
			catch(IllegalAccessException e)
			{
			}
			catch(NoSuchMethodException e)
			{
			}
			loadCallback = null;
		}
	}
	
	public void onCompletion(MediaPlayer mPlayer)
	{
		if (state != LOOPING)
		{
			this.state = STOPPED;
			try {
				this.stop();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
