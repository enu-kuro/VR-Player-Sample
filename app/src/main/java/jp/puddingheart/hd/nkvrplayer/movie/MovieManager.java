/* Copyright 2015 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.puddingheart.hd.nkvrplayer.movie;

import android.content.res.AssetFileDescriptor;
import android.media.MediaCodec;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;

import org.gearvrf.GVRActivity;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRVersion;
import org.gearvrf.scene_objects.GVRVideoSceneObject;
import org.gearvrf.scene_objects.GVRVideoSceneObjectPlayer;
import org.gearvrf.utility.Log;

import java.io.IOException;

public class MovieManager {

    private static final String FILE_NAME = "videos_s_3.mp4";

    private static final String TAG = "MovieManager";

    // 現状MediaPlayer使う前提の実装になっている
    private boolean USE_EXO_PLAYER = false;

    private Object mMediaPlayer = null;
    private GVRVideoSceneObjectPlayer videoSceneObjectPlayer = null;

    public MovieManager (GVRContext context, GVRActivity mActivity) {
        if (!USE_EXO_PLAYER) {
            videoSceneObjectPlayer = makeMediaPlayer(context);
        } else {
            videoSceneObjectPlayer = makeExoPlayer(mActivity);
        }
        mMediaPlayer = videoSceneObjectPlayer.getPlayer();
    }

    public Object getMediaPlayer() {
        return videoSceneObjectPlayer.getPlayer();
    }

    public GVRVideoSceneObjectPlayer getVideoSceneObjectPlayer() {
        return videoSceneObjectPlayer;
    }

    private GVRVideoSceneObjectPlayer<MediaPlayer> makeMediaPlayer(GVRContext context) {
        final MediaPlayer mediaPlayer = new MediaPlayer();

        final AssetFileDescriptor afd;

        try {
            afd = context.getContext().getAssets().openFd(FILE_NAME);
            android.util.Log.d(TAG, "Assets was found.");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            android.util.Log.d(TAG, "DataSource was set.");
            afd.close();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "onPrepared");
                    //mediaPlayer.start();
                }
            });
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            android.util.Log.e(TAG, "Assets were not loaded. Stopping application!");
            return null;
        }

        mediaPlayer.setLooping(true);
        android.util.Log.d(TAG, "starting player.");

        return GVRVideoSceneObject.makePlayerInstance(mediaPlayer);
    }

    private GVRVideoSceneObjectPlayer<ExoPlayer> makeExoPlayer(GVRActivity context) {
        final ExoPlayer player = ExoPlayer.Factory.newInstance(2);

        final DefaultUriDataSource dataSource = new DefaultUriDataSource(context, "GVRf " + GVRVersion.CURRENT);
        final ExtractorSampleSource sampleSource = new ExtractorSampleSource(Uri.parse("asset:///" + FILE_NAME),
                dataSource, new DefaultAllocator(64 * 1024), 64 * 1024 * 256);

        final MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(context, sampleSource,
                MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        final MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource,
                MediaCodecSelector.DEFAULT);
        player.prepare(videoRenderer, audioRenderer);
        player.setPlayWhenReady(true);

        return new GVRVideoSceneObjectPlayer<ExoPlayer>() {
            @Override
            public ExoPlayer getPlayer() {
                return player;
            }

            @Override
            public void setSurface(final Surface surface) {
                player.addListener(new ExoPlayer.Listener() {
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        switch (playbackState) {
                            case ExoPlayer.STATE_BUFFERING:
                                break;
                            case ExoPlayer.STATE_ENDED:
                                player.seekTo(0);
                                break;
                            case ExoPlayer.STATE_IDLE:
                                break;
                            case ExoPlayer.STATE_PREPARING:
                                break;
                            case ExoPlayer.STATE_READY:
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onPlayWhenReadyCommitted() {
                        surface.release();
                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException error) {
                    }
                });

                player.sendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
            }

            @Override
            public void release() {
                player.release();
            }

            @Override
            public boolean canReleaseSurfaceImmediately() {
                return false;
            }

            @Override
            public void pause() {
                player.setPlayWhenReady(false);
            }

            @Override
            public void start() {
                player.setPlayWhenReady(true);
            }
        };
    }


    public void changeTrack(GVRContext context, String trackName) {
        final AssetFileDescriptor afd;

        MediaPlayer mediaPlayer = (MediaPlayer) mMediaPlayer;
        mediaPlayer.reset();
        try {
            afd = context.getContext().getAssets().openFd(trackName);
            android.util.Log.d(TAG, "Assets was found.");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            android.util.Log.d(TAG, "DataSource was set.");
            afd.close();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "onPrepared");
                    //mediaPlayer.start();
                }
            });
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            android.util.Log.e(TAG, "Assets were not loaded. Stopping application!");
        }
    }


    public boolean isPlaying() {
        if (!USE_EXO_PLAYER) {
            return ((MediaPlayer) mMediaPlayer).isPlaying();
        } else {
            return ((ExoPlayer) mMediaPlayer).getPlaybackState() > ExoPlayer.STATE_IDLE &&
                    ((ExoPlayer) mMediaPlayer).getPlaybackState() < ExoPlayer.STATE_ENDED &&
                    ((ExoPlayer) mMediaPlayer).getPlayWhenReady();
        }
    }

    public void playerStart() {
        if (!USE_EXO_PLAYER) {
            ((MediaPlayer) mMediaPlayer).start();
        } else {
            ((ExoPlayer) mMediaPlayer).setPlayWhenReady(true);
        }
        android.util.Log.d(TAG, "player start.");
    }

    public void playerPause() {
        if (!USE_EXO_PLAYER) {
            ((MediaPlayer) mMediaPlayer).pause();
        } else {
            ((ExoPlayer) mMediaPlayer).setPlayWhenReady(false);
        }
        android.util.Log.d(TAG, "player pause.");
    }

    public long playerGetDuration() {
//        android.util.Log.d(TAG, "player getDuration.");
        if (!USE_EXO_PLAYER) {
            return ((MediaPlayer) mMediaPlayer).getDuration();
        } else {
            return ((ExoPlayer) mMediaPlayer).getDuration();
        }
    }

    public long playerGetCurrentPosition() {
//        android.util.Log.d(TAG, "player getCurrentPosition.");
        if (!USE_EXO_PLAYER) {
            return ((MediaPlayer) mMediaPlayer).getCurrentPosition();
        } else {
            return ((ExoPlayer) mMediaPlayer).getCurrentPosition();
        }
    }

    public void playerSeekTo(long l) {
        android.util.Log.d(TAG, "player seekTo.");
        if (!USE_EXO_PLAYER) {
            ((MediaPlayer) mMediaPlayer).seekTo((int)l);
        } else {
            ((ExoPlayer) mMediaPlayer).seekTo(l);
        }
    }
}
