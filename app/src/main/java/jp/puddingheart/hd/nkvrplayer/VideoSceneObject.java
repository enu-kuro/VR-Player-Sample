package jp.puddingheart.hd.nkvrplayer;

import org.gearvrf.GVRActivity;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMesh;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.scene_objects.GVRSphereSceneObject;
import org.gearvrf.scene_objects.GVRVideoSceneObject;

import jp.puddingheart.hd.nkvrplayer.focus.FocusableController;
import jp.puddingheart.hd.nkvrplayer.focus.OnClickListener;
import jp.puddingheart.hd.nkvrplayer.movie.MovieManager;
import jp.puddingheart.hd.nkvrplayer.overlay.OverlayUI;

/**
 * Created by Nobuhito Kurose on 2017/04/27.
 */

public class VideoSceneObject extends GVRSceneObject {
    private static final String TAG = "VideoSceneObject";

    private MovieManager mMovieManager = null;

    private OverlayUI mOverlayUI = null;

    public VideoSceneObject(GVRContext gvrContext, GVRActivity activity, final Main mainScene) {
        super(gvrContext);

        mMovieManager = new MovieManager(gvrContext, activity);

        GVRSphereSceneObject sphere = new GVRSphereSceneObject(gvrContext, 72, 144, false);

        GVRMesh mesh = sphere.getRenderData().getMesh();
        GVRVideoSceneObject mMovieSceneObject = new GVRVideoSceneObject( gvrContext, mesh, mMovieManager.getVideoSceneObjectPlayer(), GVRVideoSceneObject.GVRVideoType.MONO );

        float newRadius = 10;
        mMovieSceneObject.getTransform().setScale(newRadius,newRadius,newRadius);


        mOverlayUI = new OverlayUI(gvrContext, mMovieManager);
        addChildObject(mOverlayUI);

        addChildObject(mMovieSceneObject);

        mOverlayUI.mGoTopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
//               MediaPlayer mediaPlayer = (MediaPlayer)mMovieManager.getMediaPlayer();
//                mediaPlayer.reset();
               mainScene.showTopSceneObject();
            }
        });

    }

    public void init(GVRContext context, String trackName) {
        mMovieManager.changeTrack(context, trackName);
        mOverlayUI.playVideo();
        mOverlayUI.hide();
        //mOverlayUI.mGoTopButton.hide();
    }

    public void onTap(GVRContext gvrContext, FocusableController focusableController) {
        Boolean isClicked = focusableController.processClick(gvrContext);
        if (mOverlayUI.isVisible && !isClicked && !mOverlayUI.isOverlayPointed(gvrContext)) {
            mOverlayUI.hide();
        } else {
            mOverlayUI.show();
        }
    }

    public void onTouch(GVRContext gvrContext) {
        mOverlayUI.processTouch(gvrContext);
    }

    public void onPause(){
        mOverlayUI.pauseVideo();
    }

    public void onStep(GVRContext gvrContext) {
        if (mOverlayUI.isVisible) {
            mOverlayUI.updateOverlayUI(gvrContext);
        }
    }
}
