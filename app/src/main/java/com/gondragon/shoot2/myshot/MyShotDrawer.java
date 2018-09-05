package com.gondragon.shoot2.myshot;

public class MyShotDrawer {

    private Rect screenLimit = new Rect();
    protected AnimationManager animationManager;

    protected PointF drawCenter = new PointF();
    private RectF drawRect = new RectF();

    protected int totalAnimeFrame;
    protected int animeFrame;
    protected AnimationManager.AnimationSet animeSet;
    protected AnimeKind animeKind = AnimeKind.NORMAL;
}
