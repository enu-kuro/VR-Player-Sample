package jp.puddingheart.hd.nkvrplayer;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;
import org.gearvrf.scene_objects.GVRSphereSceneObject;

import java.util.ArrayList;
import java.util.concurrent.Future;

import jp.puddingheart.hd.nkvrplayer.focus.OnClickListener;
import jp.puddingheart.hd.nkvrplayer.overlay.Button;

/**
 * Created by Nobuhito Kurose on 2017/04/27.
 */
public class TopSceneObject extends GVRSceneObject {
    private static final String TAG = "TopSceneOjectb";

    public Button mPlayButton = null;
    public ArrayList<Button> mVideoButtons = new ArrayList<>();

    private String[]  mTrackList = {"videos_s_3.mp4", "videos_s_3.mp4", "videos_s_3.mp4"};

    private static final float VIDEO_BUTTON_WIDTH = 0.541f * 5;
    private static final float VIDEO_BUTTON_HEIGHT = 0.310f * 5;
    private static final float VIDEO_BUTTON_MARGIN = 0.5f;

    public TopSceneObject(GVRContext gvrContext, final Main mainScene) {
        super(gvrContext);

        Future<GVRTexture> texture = gvrContext.loadFutureTexture(new GVRAndroidResource(gvrContext, R.drawable.shibuya_photo));

        GVRSphereSceneObject sphere = new GVRSphereSceneObject(gvrContext, 72, 144, false, texture);

        float newRadius = 10;
        sphere.getTransform().setScale(newRadius,newRadius,newRadius);
        addChildObject(sphere);

        float start = -(VIDEO_BUTTON_WIDTH * 3 + VIDEO_BUTTON_MARGIN * 2) / 2 + VIDEO_BUTTON_WIDTH / 2;

        Button video1 = new Button(gvrContext, gvrContext.createQuad(VIDEO_BUTTON_WIDTH, VIDEO_BUTTON_HEIGHT),
                gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.drawable.sr_vr_top)),
                gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.drawable.sr_vr_top)));
        video1.setPosition(start , 0.0f, -8.0f);
        video1.getRenderData().setRenderingOrder(
                GVRRenderData.GVRRenderingOrder.TRANSPARENT);
        video1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mainScene.showMovieSceneObject(mTrackList[0]);
            }
        });
        addChildObject(video1);
        mVideoButtons.add(video1);

        Button video2 = new Button(gvrContext, gvrContext.createQuad(VIDEO_BUTTON_WIDTH, VIDEO_BUTTON_HEIGHT),
                gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.drawable.sr_vr_top)),
                gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.drawable.sr_vr_top)));
        video2.setPosition(start + VIDEO_BUTTON_WIDTH + VIDEO_BUTTON_MARGIN, 0.0f, -8.0f);
        video2.getRenderData().setRenderingOrder(
                GVRRenderData.GVRRenderingOrder.TRANSPARENT);
        video2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mainScene.showMovieSceneObject(mTrackList[1]);
            }
        });
        addChildObject(video2);
        mVideoButtons.add(video2);

        Button video3 = new Button(gvrContext, gvrContext.createQuad(VIDEO_BUTTON_WIDTH, VIDEO_BUTTON_HEIGHT),
                gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.drawable.sr_vr_top)),
                gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.drawable.sr_vr_top)));
        video3.setPosition(start + (VIDEO_BUTTON_WIDTH + VIDEO_BUTTON_MARGIN) * 2 , 0.0f, -8.0f);
        video3.getRenderData().setRenderingOrder(
                GVRRenderData.GVRRenderingOrder.TRANSPARENT);
        video3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                mainScene.showMovieSceneObject(mTrackList[2]);
            }
        });
        addChildObject(video3);
        mVideoButtons.add(video3);
    }


}
